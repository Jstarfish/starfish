package priv.starfish.mall.dao.interact.impl;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.interact.GoodsInquiryDao;
import priv.starfish.mall.interact.entity.GoodsInquiry;

@Component("goodsInquiryDao")
public class GoodsInquiryDaoImpl extends BaseDaoImpl<GoodsInquiry, Long> implements GoodsInquiryDao {
	@Override
	public GoodsInquiry selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(GoodsInquiry goodsInquiry) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsInquiry);
	}

	@Override
	public int update(GoodsInquiry goodsInquiry) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsInquiry);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<GoodsInquiry> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<GoodsInquiry> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}