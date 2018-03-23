package priv.starfish.mall.dao.agency.impl;

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
import priv.starfish.mall.dao.agency.AgencyDao;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;

@Component("agencyDao")
public class AgencyDaoImpl extends BaseDaoImpl<Agency, Integer> implements AgencyDao {
	@Override
	public Agency selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Agency selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public int insert(Agency agency) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, agency);
	}

	@Override
	public int update(Agency agency) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, agency);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	/**
	 * 分页查询代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:59:46
	 * 
	 * @param paginatedFilter
	 *            like name(代理处名称),like realName(所属代理商), =auditStatus =disabled, =code
	 * @return PaginatedList<Agency>
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Agency> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		//
		String realName = filterItems.getTypedValue("realName", String.class);
		filterItems.remove("realName");
		if (StrUtil.hasText(realName)) {
			realName = SqlBuilder.likeStrVal(realName);
			filterItems.put("realName", realName);
		}
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());

		PageList<Agency> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Agency> selectByFilter(MapContext filterItems) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		//
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			nickName = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickName);
		}
		//
		return this.getSqlSession().selectList(sqlId, filterItems);
	}

	@Override
	public int updateDisabled(Integer id, boolean disabled) {
		String sqlId = this.getNamedSqlId("updateDisabled");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("disabled", disabled);
		params.put("id", id);
		return this.sqlSession.update(sqlId, params);
	}

	@Override
	public int updateAuditStatusById(Integer id, Integer auditStatus) {
		String sqlId = this.getNamedSqlId("updateAuditStatusById");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("auditStatus", auditStatus);
		params.put("id", id);
		return this.sqlSession.update(sqlId, params);
	}

	@Override
	public int deleteByAgentId(Integer agentId) {
		String sqlId = this.getNamedSqlId("deleteByAgentId");
		//
		return this.sqlSession.delete(sqlId, agentId);
	}

	@Override
	public List<Agency> selectByAgentId(Integer agentId) {
		String sqlId = this.getNamedSqlId("selectByAgentId");
		//
		return this.getSqlSession().selectList(sqlId, agentId);
	}
}