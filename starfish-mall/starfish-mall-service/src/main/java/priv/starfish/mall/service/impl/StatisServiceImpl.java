package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.goods.dto.ProductDto;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.service.StatisService;
import priv.starfish.mall.dao.settle.DistSettleRecDao;
import priv.starfish.mall.dao.shop.DistShopDao;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.dao.statis.DistShopSumDao;
import priv.starfish.mall.dao.statis.GoodsBrowseSumDao;
import priv.starfish.mall.dao.statis.GoodsBuySumDao;
import priv.starfish.mall.dao.statis.ShopBrowseSumDao;
import priv.starfish.mall.statis.dto.DistShopSumDto;
import priv.starfish.mall.statis.entity.GoodsBrowseSum;
import priv.starfish.mall.statis.entity.ShopBrowseSum;

@Service("statisService")
public class StatisServiceImpl extends BaseServiceImpl implements StatisService {

	@Resource
	GoodsBuySumDao goodsBuySumDao;

	@Resource
	GoodsBrowseSumDao goodsBrowseSumDao;

	@Resource
	ShopBrowseSumDao shopBrowseSumDao;

	@Resource
	MemberDao memberDao;
	
	@Resource
	DistShopDao distShopDao;
	
	@Resource
	DistShopSumDao distShopSumDao;
	
	@Resource
	DistSettleRecDao distSettleRecDao;

	// ---------------------------------------会员购买汇总--------------------------------------------

	@Override
	public List<ProductDto> getProductsBuySummary(List<Long> productIds) {
		//
		if (productIds == null || productIds.isEmpty())
			return null;
		//
		List<ProductDto> productDtos = TypeUtil.newEmptyList(ProductDto.class);
		for (Long productId : productIds) {
			ProductDto productDto = ProductDto.newOne();
			productDto.setId(productId);
			Long summaryCount = goodsBuySumDao.selectBuyCountByProductId(productId);
			productDto.setBuySum(summaryCount);
			productDtos.add(productDto);
		}
		return productDtos;
	}

	// --------------------------------------- 商品浏览次数 --------------------------------------------
	@Override
	public boolean addGoodsBrowseCount(Long productId, Integer userId, int count) {
		boolean success = false;
		GoodsBrowseSum browseSum = goodsBrowseSumDao.selectByUserIdAndProductId(userId, productId);
		if (browseSum == null) {
			// 新增一条
			browseSum = new GoodsBrowseSum();
			browseSum.setUserId(userId);
			browseSum.setProductId(productId);
			browseSum.setCount(Long.valueOf(count));
			browseSum.setLastTime(new Date());

			success = goodsBrowseSumDao.insert(browseSum) > 0;
		} else {
			// 增加次数
			success = goodsBrowseSumDao.addBrowseCountById(browseSum.getId(), count);
		}
		return success;
	}

	@Override
	public boolean addGoodsBrowseCount(Long productId, Integer userId) {
		return addGoodsBrowseCount(productId, userId, 1);
	}

	@Override
	public long getGoodsBrowseCount(Long productId) {
		return goodsBrowseSumDao.selectBrowseCountByProductId(productId);
	}

	@Override
	public Map<Long, Long> getGoodsBrowseCounts(List<Long> productIds) {
		return goodsBrowseSumDao.selectBrowseCountByProductIds(productIds);
	}

	// --------------------------------------- 店铺浏览次数 --------------------------------------------
	@Override
	public boolean addShopBrowseCount(Integer shopId, Integer userId, int count) {
		boolean success = false;
		ShopBrowseSum browseSum = shopBrowseSumDao.selectByUserIdAndShopId(userId, shopId);
		if (browseSum == null) {
			// 新增一条
			browseSum = new ShopBrowseSum();
			browseSum.setUserId(userId);
			browseSum.setShopId(shopId);
			browseSum.setCount(Long.valueOf(count));
			browseSum.setLastTime(new Date());

			success = shopBrowseSumDao.insert(browseSum) > 0;
		} else {
			// 增加次数
			success = shopBrowseSumDao.addBrowseCountById(browseSum.getId(), count);
		}
		return success;
	}

	@Override
	public boolean addShopBrowseCount(Integer shopId, Integer userId) {
		return addShopBrowseCount(shopId, userId, 1);
	}

	@Override
	public long getShopBrowseCount(Integer shopId) {
		return shopBrowseSumDao.selectBrowseCountByShopId(shopId);
	}

	@Override
	public Map<Integer, Long> getShopBrowseCounts(List<Integer> shopIds) {
		return shopBrowseSumDao.selectBrowseCountByShopIds(shopIds);
	}

	// -------------------------------- 卫星店统计方法 ---------------------------------------
	
	@Override
	public DistShopSumDto getDistShopStatis(MapContext filter) {
		DistShopSumDto distShopSumDto = new DistShopSumDto();
		return getDistShopStatisToDto(filter, distShopSumDto);
	}

	@Override
	public PaginatedList<Map<String, Long>> getDistShopOrderGroupTime(PaginatedFilter paginatedFilter) {
		return distShopSumDao.selectDistShopOrderCountGroupTime(paginatedFilter);
	}

	@Override
	public PaginatedList<Map<String, Long>> getDistShopSvcGroupTime(PaginatedFilter paginatedFilter) {
		return distShopSumDao.selectDistShopScvCountGroupTime(paginatedFilter);
	}
	
	@Override
	public PaginatedList<DistShopSumDto> selectDistShopStatisByFilter(PaginatedFilter paginatedFilter) {
		MapContext filter = paginatedFilter.getFilterItems();
		
		PaginatedList<DistShop> paginatedList = distShopDao.selectDistShops(paginatedFilter);
		PaginatedList<DistShopSumDto> newPaginatedList = new PaginatedList<DistShopSumDto>();
		List<DistShopSumDto> newRows = new ArrayList<DistShopSumDto>();
		
		List<DistShop> list = paginatedList.getRows();
		for (DistShop distShop : list) {
			DistShopSumDto distShopSumDto = new DistShopSumDto();
			filter.put("distributorId", distShop.getId());
			getDistShopStatisToDto(filter, distShopSumDto);
			distShopSumDto.setId(distShop.getId());
			distShopSumDto.setName(distShop.getName());
			distShopSumDto.setOwnerShopId(distShop.getOwnerShopId());
			distShopSumDto.setOwnerShopName(distShop.getOwnerShopName());
			distShopSumDto.setRegionName(distShop.getRegionName());
			newRows.add(distShopSumDto);
		}
		newPaginatedList.setPagination(paginatedList.getPagination());
		newPaginatedList.setRows(newRows);
		return newPaginatedList;
	}

	@Override
	public Long getDistShopOrderStatis(MapContext filter) {
		return distShopSumDao.getDistShopOrderStatis(filter);
	}

	@Override
	public Long getDistShopSvcStatis(MapContext filter) {
		return distShopSumDao.getDistShopSvcStatis(filter);
	}

	@Override
	public Long getDistShopVisitorStatis(MapContext filter) {
		return distShopSumDao.getDistShopVisitorStatis(filter);
	}

	@Override
	public BigDecimal getDistShopAmountStatis(MapContext filter) {
		return distShopSumDao.getDistShopAmountStatis(filter);
	}

	@Override
	public BigDecimal getDistShopNoAmountStatis(MapContext filter) {
		return distShopSumDao.getDistShopNoAmountStatis(filter);
	}
	
	/**  卫星店统计信息并装载数据 */
	private  DistShopSumDto getDistShopStatisToDto(MapContext filter, DistShopSumDto distShopSumDto) {
		// 获取访客数
		Long visitorCount = distShopSumDao.getDistShopVisitorStatis(filter);
		// 获取收入金额
		BigDecimal incomeAmount = distShopSumDao.getDistShopAmountStatis(filter);
		// 获取代理订单数
		filter.put("flagCreator", "agent");
		Long agentCount = distShopSumDao.getDistShopOrderStatis(filter);
		// 获取承接订单数
		filter.put("flagCreator", "allocate");
		Long allocateCount = distShopSumDao.getDistShopOrderStatis(filter);
		// 获取服务次数
		filter.put("flagCreator", "");
		Long svcCount = distShopSumDao.getDistShopSvcStatis(filter);
		distShopSumDto.setIncomeAmount(incomeAmount);
		distShopSumDto.setAgentCount(agentCount);
		distShopSumDto.setAllocateCount(allocateCount);
		distShopSumDto.setVisitorCount(visitorCount);
		distShopSumDto.setSvcCount(svcCount);
		return distShopSumDto;
	}
}
