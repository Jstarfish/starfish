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
import priv.starfish.mall.dao.notify.SmsTemplateDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.SmsTemplate;

/**
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午6:55:38
 *
 */
@Component("smsTemplateDao")
public class SmsTemplateDaoImpl extends BaseDaoImpl<SmsTemplate, Integer> implements SmsTemplateDao {
	@Override
	public SmsTemplate selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SmsTemplate selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public SmsTemplate selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(SmsTemplate smsTemplate) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, smsTemplate);
	}

	@Override
	public int update(SmsTemplate smsTemplate) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, smsTemplate);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<SmsTemplate> selectList(PaginatedFilter paginatedFilter) {
		// 分页参数
		String sqlId = this.getNamedSqlId("selectSmsTemplates");
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SmsTemplate> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SmsTemplate> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}