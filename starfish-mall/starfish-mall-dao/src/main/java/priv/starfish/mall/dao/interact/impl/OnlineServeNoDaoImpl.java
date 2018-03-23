package priv.starfish.mall.dao.interact.impl;

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
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.dao.interact.OnlineServeNoDao;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.OnlineServeNo;

@Component("onlineServeNoDao")
public class OnlineServeNoDaoImpl extends BaseDaoImpl<OnlineServeNo, Integer> implements OnlineServeNoDao {

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<InteractUserDto> selectByFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 过滤like参数
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("authScope", authScope+"");
		filterItems.put("entityId", entityId);
		String servantNo = filterItems.getTypedValue("servantNo", String.class);
		filterItems.remove("servantNo");
		if (StrUtil.hasText(servantNo)) {
			servantNo = SqlBuilder.likeStrVal(servantNo);
			filterItems.put("servantNo", servantNo);
		}
		String servantName = filterItems.getTypedValue("servantName", String.class);
		filterItems.remove("servantName");
		if (StrUtil.hasText(servantName)) {
			servantName = SqlBuilder.likeStrVal(servantName);
			filterItems.put("servantName", servantName);
		}
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			nickName = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<InteractUserDto> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public List<InteractUserDto> selectByFilter(AuthScope authScope, Integer entityId) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		Map<String, Object> params = this.newParamMap();
		params.put("scope", authScope);
		params.put("code", entityId);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public OnlineServeNo selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(OnlineServeNo onlineServeNo) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, onlineServeNo);
	}

	@Override
	public int update(OnlineServeNo onlineServeNo) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, onlineServeNo);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
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

	@Override
	public OnlineServeNo selectByServantNo(String servantNo) {
		String sqlId = this.getNamedSqlId("selectByServantNo");
		return this.getSqlSession().selectOne(sqlId, servantNo);
	}

	@Override
	public OnlineServeNo selectByServantName(String servantName) {
		String sqlId = this.getNamedSqlId("selectByServantName");
		return this.getSqlSession().selectOne(sqlId, servantName);
	}

}
