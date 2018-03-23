package priv.starfish.mall.dao.comn.impl;

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
import priv.starfish.mall.dao.comn.FaqCatDao;
import priv.starfish.mall.comn.entity.FaqCat;

@Component("faqCatDao")
public class FaqCatDaoImpl extends BaseDaoImpl<FaqCat, Integer> implements
        FaqCatDao {
	@Override
	public FaqCat selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int selectCountByName(String name) {
		String sqlId = this.getNamedSqlId("selectCountByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(FaqCat faqCat) {
		String sqlId = this.getNamedSqlId("insert");
		if (faqCat.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(FaqCat.class) + 1;
			faqCat.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, faqCat);
	}

	@Override
	public int update(FaqCat faqCat) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, faqCat);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<FaqCat> selectByFilter(
			PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<FaqCat> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<FaqCat> selectAll() {
		Map<String,String> map=new HashMap<String,String>();
		String sqlId = this.getNamedSqlId("selectAll");
		return this.getSqlSession().selectList(sqlId,map);
	}
	
}