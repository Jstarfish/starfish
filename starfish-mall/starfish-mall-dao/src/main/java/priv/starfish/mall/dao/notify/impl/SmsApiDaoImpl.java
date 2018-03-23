package priv.starfish.mall.dao.notify.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.notify.SmsApiDao;
import priv.starfish.mall.notify.entity.SmsApi;

@Component("SmsApiDao")
public class SmsApiDaoImpl extends BaseDaoImpl<SmsApi, Integer> implements SmsApiDao {

	@Override
	public SmsApi selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	@Override
	public SmsApi selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}
	@Override
	public int insert(SmsApi smsApi) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, smsApi);
	}
	@Override
	public int update(SmsApi smsApi) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, smsApi);
	}
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<SmsApi> selectSmsApisByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSmsApisByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name.toString());
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SmsApi> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SmsApi> selectSmsApis() {
		String sqlId = this.getNamedSqlId("selectSmsApis");
		//
		return this.getSqlSession().selectList(sqlId);
	}

}
