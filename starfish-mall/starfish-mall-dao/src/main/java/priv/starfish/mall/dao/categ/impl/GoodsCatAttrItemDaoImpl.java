package priv.starfish.mall.dao.categ.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.categ.GoodsCatAttrItemDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.GoodsCatAttrItem;

import java.util.List;
import java.util.Map;

@Component("goodsCatAttrItemDao")
public class GoodsCatAttrItemDaoImpl extends BaseDaoImpl<GoodsCatAttrItem, Integer> implements GoodsCatAttrItemDao {
	@Override
	public GoodsCatAttrItem selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatAttrItem selectByAttrIdAndValue(Integer attrId, String value) {
		String sqlId = this.getNamedSqlId("selectByAttrIdAndValue");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("attrId", attrId);
		params.put("value", value);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatAttrItem goodsCatAttrItem) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsCatAttrItem);
	}

	@Override
	public int update(GoodsCatAttrItem goodsCatAttrItem) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsCatAttrItem);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByAttrId(Integer attrId) {
		String sqlId = this.getNamedSqlId("deleteByAttrId");
		return this.getSqlSession().delete(sqlId, attrId);
	}

	@Override
	public int deleteByAttrIds(List<Integer> attrIds) {
		if (attrIds != null && attrIds.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByAttrIds");
			return this.getSqlSession().delete(sqlId, attrIds);
		} else {
			return 0;
		}
	}

	@Override
	public List<GoodsCatAttrItem> selectByAttrId(Integer attrId) {
		String sqlId = this.getNamedSqlId("selectByAttrId");
		return this.getSqlSession().selectList(sqlId, attrId);
	}

	@Override
	public List<GoodsCatAttrItem> selectByAttrId(Integer attrId, List<Integer> uncontainIds) {
		String sqlId = this.getNamedSqlId("selectByAttrIdAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("attrId", attrId);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int deleteByAttrIdAndUncontainIds(Integer attrId, List<Integer> uncontainIds) {
		String sqlId = this.getNamedSqlId("deleteByAttrIdAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("attrId", attrId);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public List<String> selectCodesByAttrId(Integer attrId) {
		String sqlId = this.getNamedSqlId("selectCodesByAttrId");
		return this.getSqlSession().selectList(sqlId, attrId);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginatedList<String> selectValue2ByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectValue2ByFilter");
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<String> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}