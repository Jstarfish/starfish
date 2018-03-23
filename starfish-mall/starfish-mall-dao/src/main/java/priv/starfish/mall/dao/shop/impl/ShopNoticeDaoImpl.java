package priv.starfish.mall.dao.shop.impl;

import java.util.List;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.shop.ShopNoticeDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.ShopNotice;

@Component("shopNoticeDao")
public class ShopNoticeDaoImpl extends BaseDaoImpl<ShopNotice, Integer> implements ShopNoticeDao {
	@Override
	public ShopNotice selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ShopNotice shopNotice) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopNotice);
	}

	@Override
	public int update(ShopNotice shopNotice) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopNotice);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<ShopNotice> selectList(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectList");
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<ShopNotice> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		if (ids != null && ids.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByIds");
			return this.getSqlSession().delete(sqlId, ids);
		} else {
			return 0;
		}
	}
}