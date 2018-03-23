package priv.starfish.mall.dao.shop.impl;

import java.util.HashMap;
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
import priv.starfish.mall.dao.shop.DistShopSvcDao;
import priv.starfish.mall.shop.entity.DistShopSvc;

@Component("distShopSvcDao")
public class DistShopSvcDaoImpl extends BaseDaoImpl<DistShopSvc, Long> implements DistShopSvcDao {

	@Override
	public int insert(DistShopSvc distShopSvc) {
		String sqlId = this.getNamedSqlId("insert");
		if (distShopSvc.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(DistShopSvc.class) + 1;
			distShopSvc.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, distShopSvc);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int update(DistShopSvc distShopSvc) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, distShopSvc);
	}

	@Override
	public PaginatedList<DistShopSvc> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String auditorName = filter.getTypedValue("auditorName", String.class);
		filter.remove(auditorName);
		if (StrUtil.hasText(auditorName)) {
			auditorName = SqlBuilder.likeStrVal(auditorName);
			filter.put("auditorName", auditorName);
		}
		String auditDesc = filter.getTypedValue("auditDesc", String.class);
		filter.remove(auditDesc);
		if (StrUtil.hasText(auditDesc)) {
			auditDesc = SqlBuilder.likeStrVal(auditDesc);
			filter.put("auditDesc", auditDesc);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<DistShopSvc> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public DistShopSvc selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public DistShopSvc selectByDistShopIdAndSvcId(Integer distShopId, Integer svcId) {
		String sqlId = this.getNamedSqlId("selectByDistShopIdAndSvcId");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("distShopId", distShopId);
		map.put("svcId", svcId);
		return this.getSqlSession().selectOne(sqlId, map);
	}

	@Override
	public List<DistShopSvc> selectByFilter(MapContext request) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, request);
	}

	@Override
	public int deleteByDistShopId(Integer distShopId) {
		String sqlId = this.getNamedSqlId("deleteByDistShopId");
		//
		return this.getSqlSession().delete(sqlId, distShopId);
	}

	@Override
	public int deleteBySvcId(Integer svcId) {
		String sqlId = this.getNamedSqlId("deleteBySvcId");
		//
		return this.getSqlSession().delete(sqlId, svcId);
	}

}