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
import priv.starfish.mall.dao.svcx.SvcGroupDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.svcx.entity.SvcGroup;

@Component("svcGroupDao")
public class SvcGroupDaoImpl extends BaseDaoImpl<SvcGroup, Integer> implements SvcGroupDao {

	@Override
	public SvcGroup selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SvcGroup carSvcGroup) {
		String sqlId = this.getNamedSqlId("insert");
		if (carSvcGroup.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SvcGroup.class) + 1;
			carSvcGroup.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, carSvcGroup);
	}

	@Override
	public int update(SvcGroup carSvcGroup) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, carSvcGroup);
	}

	@Override
	public int updateForDelete(Integer id) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().update(sqlId, id);
	}

	@Override
	public List<SvcGroup> select() {
		String sqlId = this.getNamedSqlId("select");
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SvcGroup> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SvcGroup> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int selectCountByNameAndKindId(String name,Integer kindId) {
		String sqlId = this.getNamedSqlId("selectCountByNameAndKindId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("kindId", kindId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<SvcGroup> selectFront() {
		String sqlId = this.getNamedSqlId("selectFront");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public int deleteById(Integer entityId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
