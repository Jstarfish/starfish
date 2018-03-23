package priv.starfish.mall.dao.market.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.SalesRegionGoodsDao;
import priv.starfish.mall.market.entity.SalesRegionGoods;

@Component("salesRegionGoodsDao")
public class SalesRegionGoodsDaoImpl extends BaseDaoImpl<SalesRegionGoods, Integer> implements SalesRegionGoodsDao {
	@Override
	public SalesRegionGoods selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SalesRegionGoods selectByRegionIdAndProductId(Integer regionId, Long productId) {
		String sqlId = this.getNamedSqlId("selectByRegionIdAndProductId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("regionId", regionId);
		params.put("productId", productId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SalesRegionGoods salesRegionGoods) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (salesRegionGoods.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SalesRegionGoods.class);
			salesRegionGoods.setSeqNo(seqNo);
		}
		//
		return this.getSqlSession().insert(sqlId, salesRegionGoods);
	}

	@Override
	public int update(SalesRegionGoods salesRegionGoods) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, salesRegionGoods);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SalesRegionGoods> selectByRegionId(Integer regionId) {
		String sqlId = this.getNamedSqlId("selectByRegionId");
		return this.getSqlSession().selectList(sqlId, regionId);
	}

	@Override
	public int deleteByRegionId(Integer regionId) {
		String sqlId = this.getNamedSqlId("deleteByRegionId");
		return this.getSqlSession().delete(sqlId, regionId);
	}

	@Override
	public int deleteByRegionIdAndUncontainIds(Integer regionId, List<Integer> uncontainIds) {
		if (uncontainIds.size() == 0) {
			uncontainIds = null;
		}
		String sqlId = this.getNamedSqlId("deleteByRegionIdAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("regionId", regionId);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().delete(sqlId, params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<SalesRegionGoods> selectSalesRegionGoods(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSalesRegionGoods");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SalesRegionGoods> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}
