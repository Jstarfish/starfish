package priv.starfish.mall.dao.agent.impl;

import java.util.List;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.agent.AgentDao;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;

@Component("agentDao")
public class AgentDaoImpl extends BaseDaoImpl<Agent, Integer> implements AgentDao {
	@Override
	public Agent selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(Agent agent) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, agent);
	}

	@Override
	public int update(Agent agent) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, agent);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	/**
	 * 分页查询代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月9日 下午7:28:37
	 * 
	 * @param paginatedFilter
	 *            : like realName, =disabled, =id
	 * @return PaginatedList<Agent>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<Agent> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		String realName = filterItems.getTypedValue("realName", String.class);
		filterItems.remove("realName");
		if (StrUtil.hasText(realName)) {
			realName = SqlBuilder.likeStrVal(realName);
			filterItems.put("realName", realName);
		}
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());

		PageList<Agent> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Agent> selectByFilter(MapContext filterItems) {
		String sqlId = this.getNamedSqlId("selectByFilter");
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
}