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
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.FaqGroupDao;
import priv.starfish.mall.comn.entity.FaqGroup;


@Component("faqGroupDao")
public class FaqGroupDaoImpl extends BaseDaoImpl<FaqGroup, Integer> implements FaqGroupDao {
	@Override
	public FaqGroup selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public int insert(FaqGroup faqGroup) {
		String sqlId = this.getNamedSqlId("insert");
		if (faqGroup.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(FaqGroup.class, "catId", faqGroup.getCatId()) + 1;
			faqGroup.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, faqGroup);
	}
	
	@Override
	public int update(FaqGroup faqGroup) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, faqGroup);
	}
	
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<FaqGroup> selectByFilter(
			PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<FaqGroup> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public List<FaqGroup> selectByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectByCatId");
		return this.getSqlSession().selectList(sqlId,catId);
	}
	
	@Override
	public List<Integer> selectIds(Integer catId) {
		String sqlId = this.getNamedSqlId("selectIds");
		return this.getSqlSession().selectList(sqlId,catId);
	}
	
	@Override
	public int deleteByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("deleteByCatId");
		if(catId!=null&&catId>0){
			return this.getSqlSession().delete(sqlId, catId);
		}
		return 0;
	}
}