package priv.starfish.mall.dao.comn.impl;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.BizLicense;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.BizLicenseDao;

import java.util.List;
import java.util.Map;


@Component("bizLicenseDao")
public class BizLicenseDaoImpl extends BaseDaoImpl<BizLicense, Integer> implements BizLicenseDao {
	
	@Override
	public BizLicense selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public BizLicense selectByUserIdAndRegNo(Integer userId, String regNo) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndRegNo");
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("regNo", regNo);
		return this.getSqlSession().selectOne(sqlId, params);
	}
	
	@Override
	public int insert(BizLicense bizLicense) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, bizLicense);
	}
	
	@Override
	public int update(BizLicense bizLicense) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, bizLicense);
	}
	
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<BizLicense> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<BizLicense> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<BizLicense> selectByUserId(Integer merchId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		return this.getSqlSession().selectList(sqlId, merchId);
	}

}