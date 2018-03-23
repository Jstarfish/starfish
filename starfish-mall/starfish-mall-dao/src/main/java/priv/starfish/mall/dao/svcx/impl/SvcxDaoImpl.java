package priv.starfish.mall.dao.svcx.impl;

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
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.svcx.entity.Svcx;

@Component("svcxDao")
public class SvcxDaoImpl extends BaseDaoImpl<Svcx, Integer> implements SvcxDao {

	@Override
	public Svcx selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(Svcx carSvc) {
		String sqlId = this.getNamedSqlId("insert");
		if (carSvc.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(Svcx.class) + 1;
			carSvc.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, carSvc);
	}

	@Override
	public int update(Svcx carSvc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, carSvc);
	}

	@Override
	public int updateForDelete(Integer id) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int updateForDeleteByGroupId(Integer groupId) {
		String sqlId = this.getNamedSqlId("updateForDeleteByGroupId");
		//
		return this.getSqlSession().delete(sqlId, groupId);
	}

	@Override
	public int updateByGroupIdAndState(Boolean disabled, Integer groupId) {
		String sqlId = this.getNamedSqlId("updateByGroupIdAndState");
		Map<String, Object> params = this.newParamMap();
		params.put("groupId", groupId);
		params.put("disabled", disabled);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<Svcx> selectByGroupIdAndState(Integer groupId, Boolean disabled) {
		String sqlId = this.getNamedSqlId("selectByGroupIdAndState");
		Map<String, Object> params = this.newParamMap();
		params.put("groupId", groupId);
		params.put("disabled", disabled);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Svcx> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}
		String grounpName = filter.getTypedValue("grounpName", String.class);
		filter.remove(grounpName);
		if (StrUtil.hasText(grounpName)) {
			grounpName = SqlBuilder.likeStrVal(grounpName.toString());
			filter.put("grounpName", grounpName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Svcx> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Svcx> select() {
		String sqlId = this.getNamedSqlId("select");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public int deleteById(Integer entityId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Svcx> selectByShopId(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectByShopId");
		return this.getSqlSession().selectList(sqlId, shopId);
	}

	@Override
	public Svcx selectByKindIdAndName(Integer kindId, String name) {
		String sqlId = this.getNamedSqlId("selectByKindIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("kindId", kindId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

}
