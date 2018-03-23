package priv.starfish.mall.dao.logistic.impl;

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
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.logistic.LogisComDao;
import priv.starfish.mall.logistic.entity.LogisCom;

@Component("LogisComDao")
public class LogisComDaoImpl extends BaseDaoImpl<LogisCom, Integer> implements LogisComDao {

	@Override
	public LogisCom selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	@Override
	public LogisCom selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}
	@Override
	public int insert(LogisCom logisCom) {
		int maxSeqNo = getEntityMaxSeqNo(LogisCom.class);
		logisCom.setSeqNo(maxSeqNo + 1);
		//
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, logisCom);
	}
	@Override
	public int update(LogisCom logisCom) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, logisCom);
	}
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<LogisCom> selectLogisComsByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectLogisComsByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<LogisCom> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<LogisCom> selectListAll() {
		String sqlId = this.getNamedSqlId("selectListAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public List<LogisCom> selectListByGroup(Boolean lcDisabled, Boolean dwDisabled) {
		String sqlId = this.getNamedSqlId("selectListByGroup");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("lcDisabled", lcDisabled);
		params.put("dwDisabled", dwDisabled);
		return this.getSqlSession().selectList(sqlId, params);
	}

}
