package priv.starfish.mall.dao.settle.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.settle.SettleRecDao;
import priv.starfish.mall.settle.entity.SettleRec;

@Component("settleRecDao")
public class SettleRecDaoImpl extends BaseDaoImpl<SettleRec, Integer> implements SettleRecDao {
	@Override
	public SettleRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SettleRec settleRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, settleRec);
	}

	@Override
	public int update(SettleRec settleRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, settleRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SettleRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		//TODO  设置查询参数  研究需要哪几个  、加注释、xml里判断不为空
		MapContext filter = paginatedFilter.getFilterItems();
		
		String peerName = filter.getTypedValue("peerName", String.class);
		filter.remove(peerName);
		if (StrUtil.hasText(peerName)) {
			peerName = SqlBuilder.likeStrVal(peerName.toString());
			filter.put("peerName", peerName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SettleRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}