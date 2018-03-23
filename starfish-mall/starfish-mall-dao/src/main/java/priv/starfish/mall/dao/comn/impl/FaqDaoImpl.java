package priv.starfish.mall.dao.comn.impl;


import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.FaqDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.Faq;


@Component("faqDao")
public class FaqDaoImpl extends BaseDaoImpl<Faq, Integer> implements FaqDao {
	@Override
	public Faq selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public int insert(Faq faq) {
		String sqlId = this.getNamedSqlId("insert");
		if (faq.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(Faq.class, "groupId", faq.getGroupId()) + 1;
			faq.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, faq);
	}
	
	@Override
	public int update(Faq faq) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, faq);
	}
	
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int deleteByGroupId(Integer groupId) {
		String sqlId = this.getNamedSqlId("deleteByGroupId");
		return this.getSqlSession().delete(sqlId, groupId);
	}
	
	@Override
	public int deleteByGroupIds(List<Integer> groupIds) {
		String sqlId = this.getNamedSqlId("deleteByGroupIds");
		if(!TypeUtil.isNullOrEmpty(groupIds)){
			return this.getSqlSession().delete(sqlId, groupIds);
		}
		return 0;
	}
	@Override
	public PaginatedList<Faq> selectByFilter(
			PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String keywords = filter.getTypedValue("keywords", String.class);
		filter.remove(keywords);
		if (StrUtil.hasText(keywords)) {
			keywords = SqlBuilder.likeStrVal(keywords);
			filter.put("keywords", keywords);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Faq> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public List<Faq> selectByGroupId(Integer groupId) {
		String sqlId = this.getNamedSqlId("selectByGroupId");
		return this.getSqlSession().selectList(sqlId,groupId);
	}
	
}
