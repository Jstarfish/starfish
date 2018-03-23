package priv.starfish.mall.service.impl;

import net.sf.json.JSONObject;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Pagination;
import priv.starfish.common.util.DistanceUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.xsearch.EsSearcher;
import priv.starfish.mall.dao.comn.BizLicenseDao;
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.dao.shop.DistShopSvcDao;
import priv.starfish.mall.dao.shop.ShopAlbumImgDao;
import priv.starfish.mall.dao.shop.ShopSvcDao;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.service.*;
import priv.starfish.mall.shop.doc.DistShopDoc;
import priv.starfish.mall.shop.doc.ShopDoc;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopSvc;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.svcx.entity.Svcx;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.geoDistanceSort;

@Service("searchService")
public class SearchServiceImpl extends BaseServiceImpl implements SearchService, IRefreshable, BaseService {

	@Resource
	ShopService shopService;

	@Resource
	DistShopService distShopService;

	@Resource
	SvcxDao svcxDao;

	@Resource
	DistShopSvcDao distShopSvcDao;

	@Resource
	MerchantDao merchantDao;

	@Resource
	BizLicenseDao bizLicenseDao;

	@Resource
	ShopAlbumImgDao shopAlbumImgDao;

	@Resource
	ShopSvcDao shopSvcDao;
	
	@Resource
	CarSvcService carSvcService;

	protected static class DocType {
		public static final String SHOP = "shop";
		public static final String DISTSHOP = "distShop";
		public static final String PRODUCT = "product";

	}

	@Override
	public void refresh() {
		EsSearcher.destroy();
		//
		EsSearcher.init();
	}

	@Override
	public void echoSearcherInfo() {
		System.out.println(">> ElasticSearch 搜索服务索引库名称：" + EsSearcher.getIndexDbName());
	}

	@Override
	public void indexShopDocs(boolean scanAll) {
		// 记录各种数量
		int indexedCount = 0;
		int createdCount = 0;
		int updatedCount = 0;
		// 索引文档时间检查
		GetRequest checkRequest = EsSearcher.newGetRequest(DocType.SHOP);
		checkRequest.fields(EsSearcher.indexTimeLongFieldName);
		GetResponse checkResponse;

		PaginatedList<ShopDto> pagedList;
		PaginatedFilter pager = PaginatedFilter.newOne();

		while (true) {
			if (scanAll) {
				pagedList = shopService.getShops(pager);
			} else {
				pagedList = shopService.getShopsByLatestChanges(pager);
			}

			List<ShopDto> entityList = pagedList.getRows();
			if (entityList.size() <= 0) {
				// 没有了
				break;
			}
			for (int i = 0; i < entityList.size(); i++) {
				Shop entity = entityList.get(i).getShop();
				Integer id = entity.getId();
				ShopDoc docSource = ShopDoc.from(entity);

				// 索引店铺下的可用服务
				//List<Svcx> svcxs = svcxDao.selectByShopId(id);
				List<ShopSvc> svcList = shopSvcDao.selectByShopId(id);
				for (ShopSvc svcx : svcList) {
					docSource.svcxIds.add(svcx.getSvcId());
				}

				// 索引店铺下的可用商品
				docSource.productIds = shopService.getProductIdsByShopIdAndLackFlag(id);

				Date newIndexTime = null;
				// 检查elastic 中的最后索引时间
				checkResponse = EsSearcher.doGet(checkRequest, id);
				if (checkResponse.isExists()) {
					// 根据情况更新索引文档
					GetField field = checkResponse.getField(EsSearcher.indexTimeLongFieldName);
					if (field != null) {
						// doc中存在值
						Long indexTimeLong = (Long) field.getValue();
						Date changeTime = entity.getChangeTime();
						Date indexTime = entity.getIndexTime();
						if (indexTime == null || changeTime != null && (indexTime.getTime() < changeTime.getTime() || indexTimeLong < changeTime.getTime())) {
							newIndexTime = new Date();
						}
					} else {
						// doc中不存在值
						newIndexTime = new Date();
					}
					//
					if (newIndexTime != null) {
						docSource.indexTime = newIndexTime;
						docSource.indexTimeLong = newIndexTime.getTime();
						// 更新索引文档
						UpdateRequest request = EsSearcher.newUpdateRequest(DocType.SHOP);

						UpdateResponse response = EsSearcher.doUpdate(request, docSource);

						if (response.isCreated()) {
							createdCount++;
							indexedCount++;

							this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
							// 同步数据库索引时间
							shopService.updateShopAsIndexed(id, newIndexTime);
						} else {
							updatedCount++;
							indexedCount++;

							this.logger.info(updatedCount + " 个文档被更新 ::>>\n" + JsonUtil.toFormattedJson(response));
							// 同步数据库索引时间
							shopService.updateShopAsIndexed(id, newIndexTime);
						}
					}
				} else {
					newIndexTime = new Date();
					docSource.indexTime = newIndexTime;
					docSource.indexTimeLong = newIndexTime.getTime();
					// 新增索引文档
					IndexRequest request = EsSearcher.newIndexRequest(DocType.SHOP);

					IndexResponse response = EsSearcher.doIndex(request, docSource);

					if (response.isCreated()) {
						createdCount++;
						indexedCount++;

						this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
						// 同步数据库索引时间
						shopService.updateShopAsIndexed(id, newIndexTime);
					} else {
						this.logger.info(JsonUtil.toFormattedJson(response));
					}
				}
			}
			// 处理页码
			Pagination pagination = pagedList.getPagination();
			if (scanAll) {
				// 增加页码
				pagination.setPageNumber(pagination.getPageNumber() + 1);
			} else {
				// 重置页码
				pagination.setPageNumber(1);
			}
			pager.setPagination(pagination);
		}

		if (indexedCount == 0) {
			this.logger.info("没有需要索引的文档");
		} else {
			this.logger.info(createdCount + " 个文档被创建");
			this.logger.info(updatedCount + " 个文档被更新");
		}

	}

	@Override
	public void indexShopDocById(Integer shopId) {
		// 记录各种数量
		int indexedCount = 0;
		int createdCount = 0;
		int updatedCount = 0;
		GetRequest checkRequest = EsSearcher.newGetRequest(DocType.SHOP);
		checkRequest.fields(EsSearcher.indexTimeLongFieldName);
		GetResponse checkResponse;
		ShopDto shopDto = shopService.getShopInfoById(shopId);
		Shop entity = shopDto.getShop();
		Integer id = entity.getId();
		ShopDoc docSource = ShopDoc.from(entity);

		// 索引店铺下的可用服务
		List<ShopSvc> svcList = shopSvcDao.selectByShopId(id);
		for (ShopSvc svcx : svcList) {
			docSource.svcxIds.add(svcx.getSvcId());
		}
//		List<Svcx> svcxs = svcxDao.selectByShopId(id);
//		for (Svcx svcx : svcxs) {
//			if (!svcx.getDisabled()) {
//				docSource.svcxIds.add(svcx.getId());
//			}
//		}

		// 索引店铺下的可用商品
		docSource.productIds = shopService.getProductIdsByShopIdAndLackFlag(id);

		// 检查elastic 中的最后索引时间
		checkResponse = EsSearcher.doGet(checkRequest, id);
		Date newIndexTime = new Date();
		docSource.indexTime = newIndexTime;
		docSource.indexTimeLong = newIndexTime.getTime();
		if (checkResponse.isExists()) {
			// 更新索引文档
			UpdateRequest request = EsSearcher.newUpdateRequest(DocType.SHOP);
			UpdateResponse response = EsSearcher.doUpdate(request, docSource);

			updatedCount++;
			indexedCount++;

			this.logger.info(updatedCount + " 个文档被更新 ::>>\n" + JsonUtil.toFormattedJson(response));
			// 同步数据库索引时间
			shopService.updateShopAsIndexed(id, newIndexTime);

		} else {

			// 新增索引文档
			IndexRequest request = EsSearcher.newIndexRequest(DocType.SHOP);

			IndexResponse response = EsSearcher.doIndex(request, docSource);

			if (response.isCreated()) {
				createdCount++;
				indexedCount++;

				this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
				// 同步数据库索引时间
				shopService.updateShopAsIndexed(id, newIndexTime);
			} else {
				this.logger.info(JsonUtil.toFormattedJson(response));
			}
		}
		if (indexedCount == 0) {
			this.logger.info("没有需要索引的文档");
		} else {
			this.logger.info(createdCount + " 个文档被创建");
			this.logger.info(updatedCount + " 个文档被更新");
		}

	}

	@Override
	public void indexDistShopDocs(boolean scanAll) {
		// 记录各种数量
		int indexedCount = 0;
		int createdCount = 0;
		int updatedCount = 0;
		// 索引文档时间检查
		GetRequest checkRequest = EsSearcher.newGetRequest(DocType.DISTSHOP);
		checkRequest.fields(EsSearcher.indexTimeLongFieldName);
		GetResponse checkResponse;

		PaginatedList<DistShop> pagedList;
		PaginatedFilter pager = PaginatedFilter.newOne();

		while (true) {
			if (scanAll) {
				pagedList = distShopService.getDistShopsByFilter(pager);
			} else {
				pagedList = distShopService.getDistShopsByLatestChanges(pager);
			}

			List<DistShop> entityList = pagedList.getRows();
			if (entityList.size() <= 0) {
				// 没有了
				break;
			}
			for (int i = 0; i < entityList.size(); i++) {
				DistShop entity = entityList.get(i);
				Integer id = entity.getId();
				DistShopDoc docSource = DistShopDoc.from(entity);
				MapContext mapContext = MapContext.newOne();
				mapContext.put("distShopId", id);
				mapContext.put("auditStatus", 1);
				List<DistShopSvc> distShopSvcs = distShopSvcDao.selectByFilter(mapContext);
				for (DistShopSvc distShopSvc : distShopSvcs) {
					docSource.svcxIds.add(distShopSvc.getSvcId());
				}

				Date newIndexTime = null;
				// 检查elastic 中的最后索引时间
				checkResponse = EsSearcher.doGet(checkRequest, id);
				if (checkResponse.isExists()) {
					// 根据情况更新索引文档
					GetField field = checkResponse.getField(EsSearcher.indexTimeLongFieldName);
					if (field != null) {
						// doc中存在值
						Long indexTimeLong = (Long) field.getValue();
						Date changeTime = entity.getChangeTime();
						Date indexTime = entity.getIndexTime();
						if (indexTime == null || changeTime != null && (indexTime.getTime() < changeTime.getTime() || indexTimeLong < changeTime.getTime())) {
							newIndexTime = new Date();
						}
					} else {
						// doc中不存在值
						newIndexTime = new Date();
					}
					//
					if (newIndexTime != null) {
						docSource.indexTime = newIndexTime;
						docSource.indexTimeLong = newIndexTime.getTime();
						// 更新索引文档
						UpdateRequest request = EsSearcher.newUpdateRequest(DocType.DISTSHOP);

						UpdateResponse response = EsSearcher.doUpdate(request, docSource);

						if (response.isCreated()) {
							createdCount++;
							indexedCount++;

							this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
							// 同步数据库索引时间
							distShopService.updateDistShopAsIndexed(id, newIndexTime);
						} else {
							updatedCount++;
							indexedCount++;

							this.logger.info(updatedCount + " 个文档被更新 ::>>\n" + JsonUtil.toFormattedJson(response));
							// 同步数据库索引时间
							distShopService.updateDistShopAsIndexed(id, newIndexTime);
						}
					}
				} else {
					newIndexTime = new Date();
					docSource.indexTime = newIndexTime;
					docSource.indexTimeLong = newIndexTime.getTime();
					// 新增索引文档
					IndexRequest request = EsSearcher.newIndexRequest(DocType.DISTSHOP);

					IndexResponse response = EsSearcher.doIndex(request, docSource);

					if (response.isCreated()) {
						createdCount++;
						indexedCount++;

						this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
						// 同步数据库索引时间
						distShopService.updateDistShopAsIndexed(id, newIndexTime);
					} else {
						this.logger.info(JsonUtil.toFormattedJson(response));
					}
				}
			}
			// 处理页码
			Pagination pagination = pagedList.getPagination();
			if (scanAll) {
				// 增加页码
				pagination.setPageNumber(pagination.getPageNumber() + 1);
			} else {
				// 重置页码
				pagination.setPageNumber(1);
			}
			pager.setPagination(pagination);
		}

		if (indexedCount == 0) {
			this.logger.info("没有需要索引的文档");
		} else {
			this.logger.info(createdCount + " 个文档被创建");
			this.logger.info(updatedCount + " 个文档被更新");
		}

	}

	@Override
	public void indexDistShopDocById(Integer distShopId) {
		// 记录各种数量
		int indexedCount = 0;
		int createdCount = 0;
		int updatedCount = 0;
		GetRequest checkRequest = EsSearcher.newGetRequest(DocType.DISTSHOP);
		checkRequest.fields(EsSearcher.indexTimeLongFieldName);
		GetResponse checkResponse;

		DistShop entity = distShopService.getById(distShopId);
		Integer id = entity.getId();
		DistShopDoc docSource = DistShopDoc.from(entity);

		MapContext mapContext = MapContext.newOne();
		mapContext.put("distShopId", id);
		mapContext.put("auditStatus", 1);
		List<DistShopSvc> distShopSvcs = distShopSvcDao.selectByFilter(mapContext);
		for (DistShopSvc distShopSvc : distShopSvcs) {
			docSource.svcxIds.add(distShopSvc.getSvcId());
		}

		checkResponse = EsSearcher.doGet(checkRequest, id);

		Date newIndexTime = new Date();
		docSource.indexTime = newIndexTime;
		docSource.indexTimeLong = newIndexTime.getTime();
		if (checkResponse.isExists()) {
			// 更新索引文档
			UpdateRequest request = EsSearcher.newUpdateRequest(DocType.DISTSHOP);

			UpdateResponse response = EsSearcher.doUpdate(request, docSource);

			if (response.isCreated()) {
				createdCount++;
				indexedCount++;

				this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
				// 同步数据库索引时间
				distShopService.updateDistShopAsIndexed(id, newIndexTime);
			} else {
				updatedCount++;
				indexedCount++;

				this.logger.info(updatedCount + " 个文档被更新 ::>>\n" + JsonUtil.toFormattedJson(response));
				// 同步数据库索引时间
				distShopService.updateDistShopAsIndexed(id, newIndexTime);
			}

		} else {
			// 新增索引文档
			IndexRequest request = EsSearcher.newIndexRequest(DocType.DISTSHOP);

			IndexResponse response = EsSearcher.doIndex(request, docSource);

			if (response.isCreated()) {
				createdCount++;
				indexedCount++;

				this.logger.info(createdCount + " 个文档被创建 ::>>\n" + JsonUtil.toFormattedJson(response));
				// 同步数据库索引时间
				distShopService.updateDistShopAsIndexed(id, newIndexTime);
			} else {
				this.logger.info(JsonUtil.toFormattedJson(response));
			}
		}
		if (indexedCount == 0) {
			this.logger.info("没有需要索引的文档");
		} else {
			this.logger.info(createdCount + " 个文档被创建");
			this.logger.info(updatedCount + " 个文档被更新");
		}

	}

	@Override
	public PaginatedList<ShopDto> searchShopsByFilter(PaginatedFilter paginatedFilter) {
		SearchRequestBuilder builder = EsSearcher.newSearchRequestBuilder();
		builder.setTypes(DocType.SHOP);
		List<FilterBuilder> mustFilterBuilders = new ArrayList<FilterBuilder>();

		MapContext filterItems = paginatedFilter.getFilterItems();
		Integer auditStatus = filterItems.getTypedValue("auditStatus", Integer.class);
		Boolean disabled = filterItems.getTypedValue("disabled", Boolean.class);
		Boolean closed = filterItems.getTypedValue("closed", Boolean.class);
		List<Integer> shopIds = filterItems.getTypedValue("shopIds", List.class);
		List<Integer> svcxIds = filterItems.getTypedValue("svcxIds", List.class);
		List<Integer> productIds = filterItems.getTypedValue("productIds", List.class);

		if (shopIds != null) {
			for (Integer shopId : shopIds) {
				mustFilterBuilders.add(termFilter("id", shopId));
			}
		}

		// 过滤能够提供某些服务或者商品的店铺
		if (svcxIds != null) {
			for (Integer svcxId : svcxIds) {
				mustFilterBuilders.add(termFilter("svcxIds", svcxId));
			}
		}
		if (productIds != null) {
			for (Integer productId : productIds) {
				mustFilterBuilders.add(termFilter("productIds", productId));
			}
		}

		// 根据地区过滤店铺
		Integer regionId = filterItems.getTypedValue("regionId", Integer.class);
		Integer level = filterItems.getTypedValue("level", Integer.class);
		if (level != null && regionId != null) {
			switch (level) {
			case 1:
				mustFilterBuilders.add(termFilter("provinceId", regionId));
				break;
			case 2:
				mustFilterBuilders.add(termFilter("cityId", regionId));
				break;
			case 3:
				mustFilterBuilders.add(termFilter("countyId", regionId));
				break;
			case 4:
				mustFilterBuilders.add(termFilter("townId", regionId));
				break;
			}
		}

		// 根据审核状态过滤店铺
		if (auditStatus != null) {
			mustFilterBuilders.add(termFilter("auditStatus", auditStatus));
		}

		// 根据可用情况过滤店铺
		if (disabled != null) {
			mustFilterBuilders.add(termFilter("disabled", disabled));
		}

		// 根据关闭情况过滤店铺
		if (closed != null) {
			mustFilterBuilders.add(termFilter("closed", closed));
		}

		FilterBuilder filterBuilder = FilterBuilders.boolFilter().must(mustFilterBuilders.toArray(new FilterBuilder[mustFilterBuilders.size()]));

		// 根据距离当前位置远近过滤
		Double lat = (Double) filterItems.get("lat");
		Double lon = (Double) filterItems.get("lon");
		String distance = (String) filterItems.get("distance");
		if (lat != null && lon != null && !"-1".equals(distance)) {
			FilterBuilder distanceFb = geoDistanceFilter("location").point(lat, lon).distance(distance).geoDistance(GeoDistance.ARC);
			filterBuilder = andFilter(filterBuilder, distanceFb);
		}
		builder.setPostFilter(filterBuilder);
		// 排序 ================================
		if (lat != null && lon != null && !"-1".equals(distance)) {
			SortBuilder distanceSb = geoDistanceSort("location").point(lat, lon).geoDistance(GeoDistance.ARC).order(SortOrder.ASC);
			builder.addSort(distanceSb);
		}
		// builder.addSort("browserCount", SortOrder.DESC);
		builder.addSort("regTime", SortOrder.DESC);
		// 分页 ================================
		Pagination pagination = paginatedFilter.getPagination();
		builder.setSize(pagination.getPageSize()).setFrom((pagination.getPageNumber() - 1) * pagination.getPageSize());
		builder.setExplain(true);
		// 聚集（注意参照的全部文档而不仅仅是符合结果的，所以通常是跟matchAll连用）
		// SumBuilder sumBuilder =
		// AggregationBuilders.sum("totalBrowserCount").field("browserCount");
		// builder.addAggregation(sumBuilder);
		DateHistogramBuilder dateBuilder = AggregationBuilders.dateHistogram("regDate").field("regTime").format("yyyy-MM-dd").interval(DateHistogram.Interval.DAY);
		builder.addAggregation(dateBuilder);

		// 请求处理
		SearchResponse response = builder.get();

		// 结果处理
		SearchHits searchHits = response.getHits();
		Long totalCount = searchHits.getTotalHits();
		SearchHit[] hits = searchHits.getHits();
		List<ShopDto> listShopDto = new ArrayList<ShopDto>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String hitSrcStr = hit.getSourceAsString();
			JSONObject jsonObject = JSONObject.fromObject(hitSrcStr);
			jsonObject.remove("location");
			jsonObject.remove("svcxIds");
			jsonObject.remove("productIds");
			jsonObject.remove("shopAlbumImgs");
			Shop shop = (Shop) JSONObject.toBean(jsonObject, Shop.class);
			this.filterFileBrowseUrl(shop);

			if (lat != null && lon != null && shop.getLat() != null && shop.getLng() != null) {
				Double distanceBetweenShopAndUser = DistanceUtil.GetShortDistance(lon, lat, shop.getLng(), shop.getLat());
				shop.setDistance(distanceBetweenShopAndUser);
			}

			ShopDto shopDto = new ShopDto();
			shopDto.setId(shop.getId());
			shopDto.setShop(shop);

			// 查询门店提供的服务列表
			List<ShopSvc> shopSvcs = shopSvcDao.selectByShopId(shop.getId());
			for(ShopSvc shopSvc:shopSvcs){
				Svcx svcx=carSvcService.getCarSvc(shopSvc.getSvcId());
				shopSvc.setSvc(svcx);
			}
			shopDto.setShopSvcs(shopSvcs);
			listShopDto.add(shopDto);

		}
		PaginatedList<ShopDto> pagListShopDto = new PaginatedList<ShopDto>();
		pagListShopDto.setRows(listShopDto);
		paginatedFilter.getPagination().setTotalCount(totalCount.intValue());
		pagListShopDto.setPagination(paginatedFilter.getPagination());
		return pagListShopDto;
	}

	@Override
	public PaginatedList<DistShop> searchDistShopsByFilter(PaginatedFilter paginatedFilter) {
		SearchRequestBuilder builder = EsSearcher.newSearchRequestBuilder();
		builder.setTypes(DocType.DISTSHOP);
		List<FilterBuilder> mustFilterBuilders = new ArrayList<FilterBuilder>();

		MapContext filterItems = paginatedFilter.getFilterItems();
		Integer ownerShopId = filterItems.getTypedValue("ownerShopId", Integer.class);
		Integer auditStatus = filterItems.getTypedValue("auditStatus", Integer.class);
		Boolean disabled = filterItems.getTypedValue("disabled", Boolean.class);
		Boolean closed = filterItems.getTypedValue("closed", Boolean.class);
		List<Integer> shopIds = filterItems.getTypedValue("shopIds", List.class);
		List<Integer> svcxIds = filterItems.getTypedValue("svcxIds", List.class);

		// 根据隶属店铺过滤
		if (ownerShopId != null) {
			mustFilterBuilders.add(termFilter("ownerShopId", ownerShopId));
		}

		if (shopIds != null) {
			for (Integer shopId : shopIds) {
				mustFilterBuilders.add(termFilter("id", shopId));
			}
		}

		// 过滤能够提供某些服务或者商品的店铺
		if (svcxIds != null) {
			for (Integer svcxId : svcxIds) {
				mustFilterBuilders.add(termFilter("svcxIds", svcxId));
			}
		}

		// 根据地区过滤店铺
		Integer regionId = filterItems.getTypedValue("regionId", Integer.class);
		Integer level = filterItems.getTypedValue("level", Integer.class);
		if (level != null && regionId != null) {
			switch (level) {
			case 1:
				mustFilterBuilders.add(termFilter("provinceId", regionId));
				break;
			case 2:
				mustFilterBuilders.add(termFilter("cityId", regionId));
				break;
			case 3:
				mustFilterBuilders.add(termFilter("countyId", regionId));
				break;
			case 4:
				mustFilterBuilders.add(termFilter("townId", regionId));
				break;
			}
		}

		// 根据审核状态过滤店铺
		if (auditStatus != null) {
			mustFilterBuilders.add(termFilter("auditStatus", auditStatus));
		}

		// 根据可用情况过滤店铺
		if (disabled != null) {
			mustFilterBuilders.add(termFilter("disabled", disabled));
		}

		// 根据关闭情况过滤店铺
		if (closed != null) {
			mustFilterBuilders.add(termFilter("closed", closed));
		}

		FilterBuilder filterBuilder = FilterBuilders.boolFilter().must(mustFilterBuilders.toArray(new FilterBuilder[mustFilterBuilders.size()]));

		// 根据距离当前位置远近过滤
		Double lat = (Double) filterItems.get("lat");
		Double lon = (Double) filterItems.get("lon");
		String distance = (String) filterItems.get("distance");
		if (lat != null && lon != null && !"-1".equals(distance)) {
			FilterBuilder distanceFb = geoDistanceFilter("location").point(lat, lon).distance(distance).geoDistance(GeoDistance.ARC);
			filterBuilder = andFilter(filterBuilder, distanceFb);
		}
		builder.setPostFilter(filterBuilder);
		// 排序 ================================
		if (lat != null && lon != null && !"-1".equals(distance)) {
			SortBuilder distanceSb = geoDistanceSort("location").point(lat, lon).geoDistance(GeoDistance.ARC).order(SortOrder.ASC);
			builder.addSort(distanceSb);
		}
		// builder.addSort("browserCount", SortOrder.DESC);
		builder.addSort("regTime", SortOrder.DESC);
		// 分页 ================================
		Pagination pagination = paginatedFilter.getPagination();
		builder.setSize(pagination.getPageSize()).setFrom((pagination.getPageNumber() - 1) * pagination.getPageSize());
		builder.setExplain(true);
		// 聚集（注意参照的全部文档而不仅仅是符合结果的，所以通常是跟matchAll连用）
		// SumBuilder sumBuilder =
		// AggregationBuilders.sum("totalBrowserCount").field("browserCount");
		// builder.addAggregation(sumBuilder);
		DateHistogramBuilder dateBuilder = AggregationBuilders.dateHistogram("regDate").field("regTime").format("yyyy-MM-dd").interval(DateHistogram.Interval.DAY);
		builder.addAggregation(dateBuilder);

		// 请求处理
		SearchResponse response = builder.get();

		// 结果处理
		SearchHits searchHits = response.getHits();
		Long totalCount = searchHits.getTotalHits();
		SearchHit[] hits = searchHits.getHits();
		List<DistShop> listDistShop = new ArrayList<DistShop>();
		for (int i = 0; i < hits.length; i++) {
			SearchHit hit = hits[i];
			String hitSrcStr = hit.getSourceAsString();
			JSONObject jsonObject = JSONObject.fromObject(hitSrcStr);
			jsonObject.remove("location");
			jsonObject.remove("svcxIds");
			DistShop distShop = (DistShop) JSONObject.toBean(jsonObject, DistShop.class);
			this.filterFileBrowseUrl(distShop);

			if (lat != null && lon != null && distShop.getLat() != null && distShop.getLng() != null) {
				Double distanceBetweenDistShopAndUser = DistanceUtil.GetShortDistance(lon, lat, distShop.getLng(), distShop.getLat());
				distShop.setDistance(distanceBetweenDistShopAndUser);
			}
			listDistShop.add(distShop);
		}
		PaginatedList<DistShop> pagListDistShop = new PaginatedList<DistShop>();
		pagListDistShop.setRows(listDistShop);
		paginatedFilter.getPagination().setTotalCount(totalCount.intValue());
		pagListDistShop.setPagination(paginatedFilter.getPagination());
		return pagListDistShop;
	}

}
