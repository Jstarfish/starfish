package priv.starfish.mall.dao.market.impl;

import java.util.List;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.market.PrmtTagGoodsDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.PrmtTagGoods;

@Component("prmtTagGoodsDao")
public class PrmtTagGoodsDaoImpl extends BaseDaoImpl<PrmtTagGoods, Integer> implements PrmtTagGoodsDao {
	@Override
	public PrmtTagGoods selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public PrmtTagGoods selectByTagIdAndProductId(Integer tagId, Long productId) {
		String sqlId = this.getNamedSqlId("selectByTagIdAndProductId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tagId", tagId);
		params.put("productId", productId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(PrmtTagGoods prmtTagGoods) {
		//
		int maxSeqNo = this.getEntityMaxSeqNo(PrmtTagGoods.class, "tagId", prmtTagGoods.getTagId());
		prmtTagGoods.setSeqNo(maxSeqNo + 1);
		//
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, prmtTagGoods);
	}

	@Override
	public int update(PrmtTagGoods prmtTagGoods) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, prmtTagGoods);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByTagIdAndProductId(Integer tagId, Long productId) {
		String sqlId = this.getNamedSqlId("deleteByTagIdAndProductId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tagId", tagId);
		params.put("productId", productId);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PrmtTagGoods> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String title = filter.getTypedValue("title", String.class);
		if(StrUtil.hasText(title)){
			title = SqlBuilder.likeStrVal(title);
			filter.put("title", title);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PrmtTagGoods> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<PrmtTagGoods> selectByTagId(Integer tagId) {
		String sqlId = this.getNamedSqlId("selectByTagId");
		//
		return this.getSqlSession().selectList(sqlId, tagId);
	}

	@Override
	public List<PrmtTagGoods> selectByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectByProductId");
		//
		return this.getSqlSession().selectList(sqlId, productId);
	}
}
