package priv.starfish.mall.dao.shop.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.shop.entity.Shop;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("shopDao")
public class ShopDaoImpl extends BaseDaoImpl<Shop, Integer> implements ShopDao {
	@Override
	public Shop selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Shop selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public Shop selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(Shop shop) {
		// 初始化默认数据
		if (shop.getAuditStatus() == null) {
			shop.setAuditStatus(0);
		}
		if (shop.getClosed() == null) {
			shop.setClosed(false);
		}
		if (shop.getDisabled() == null) {
			shop.setDisabled(false);
		}
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shop);
	}

	@Override
	public int update(Shop shop) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shop);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<Shop> selectByMerchId(Integer merchId) {
		String sqlId = this.getNamedSqlId("selectByMerchId");
		return this.getSqlSession().selectList(sqlId, merchId);
	}

	@Override
	public int deleteByMerchId(Integer merchId) {
		String sqlId = this.getNamedSqlId("deleteByMerchId");
		//
		return this.getSqlSession().delete(sqlId, merchId);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<Shop> selectShops(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectShops");
		//
		MapContext filter = paginatedFilter.getFilterItems();

		// 根据店铺名查询使用
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}

		// 根据详细地址查询使用
		String address = filter.getTypedValue("address", String.class);
		filter.remove(address);
		if (StrUtil.hasText(address)) {
			address = SqlBuilder.likeStrVal(address);
			filter.put("address", address);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Shop> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateClosed(Integer id, boolean closed) {
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("closed", closed);
		String sqlId = this.getNamedSqlId("updateClosed");
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateDisabled(Integer id, boolean disabled) {
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("disabled", disabled);
		String sqlId = this.getNamedSqlId("updateDisabled");
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateAudit(Integer id, Integer auditorId, String auditorName, Date auditTime, Integer auditStatus) {
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("auditorId", auditorId);
		params.put("auditorName", auditorName);
		params.put("auditTime", auditTime);
		params.put("auditStatus", auditStatus);
		String sqlId = this.getNamedSqlId("updateAudit");
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateShop(Shop shop) {
		String sqlId = this.getNamedSqlId("updateShop");
		//
		return this.getSqlSession().update(sqlId, shop);
	}

	@Override
	public Shop selectByMerchantIdAndName(Integer merchantId, String name) {
		String sqlId = this.getNamedSqlId("selectByMerchantIdAndName");
		Map<String, Object> params = this.newParamMap();
		params.put("merchantId", merchantId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int updateAsIndexed(Integer id, Date newIndexTime) {
		String sqlId = this.getNamedSqlId("updateAsIndexed");

		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("indexTime", newIndexTime);

		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public PaginatedList<Shop> selectByLatestChanges(PaginatedFilter pager) {
		String sqlId = this.getNamedSqlId("selectByLatestChanges");
		// 分页参数
		PageBounds pageBounds = toPageBounds(pager.getPagination(), pager.getSortItems());
		PageList<Shop> PageList = (PageList) getSqlSession().selectList(sqlId, pager.getFilterItems(), pageBounds);

		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Integer> selectProductIdsByShopIdAndLackFlag(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectProductIdsByShopIdAndLackFlag");
		return this.getSqlSession().selectList(sqlId, shopId);
	}

	@Override
	public int updateChangeTime(Integer id) {
		String sqlId = this.getNamedSqlId("updateChangeTime");

		return this.getSqlSession().update(sqlId, id);

	}
}