package priv.starfish.mall.dao.mall.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.mall.MallNoticeDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.mall.entity.MallNotice;

@Component("mallNoticeDao")
public class MallNoticeDaoImpl extends BaseDaoImpl<MallNotice, Integer> implements MallNoticeDao {

	@Override
	public MallNotice selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<MallNotice> selectByFitler(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFitler");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String title = filterItems.getTypedValue("title", String.class);
		filterItems.remove("title");
		if (StrUtil.hasText(title)) {
			title = SqlBuilder.likeStrVal(title);
			filterItems.put("title", title);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<MallNotice> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int insert(MallNotice mallNotice) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, mallNotice);
	}

	@Override
	public int update(MallNotice mallNotice) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, mallNotice);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		String sqlId = this.getNamedSqlId("deleteByIds");
		return this.getSqlSession().delete(sqlId, ids);
	}

}
