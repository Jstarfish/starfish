package priv.starfish.mall.dao.shop.impl;

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
import priv.starfish.mall.dao.shop.ShopGradeDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.ShopGrade;

@Component("shopGradeDao")
public class ShopGradeDaoImpl extends BaseDaoImpl<ShopGrade, Integer> implements ShopGradeDao {
	@Override
	public ShopGrade selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopGrade selectByGradeAndLevel(Integer grade, Integer level) {
		String sqlId = this.getNamedSqlId("selectByGradeAndLevel");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("grade", grade);
		params.put("level", level);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopGrade shopGrade) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopGrade);
	}

	@Override
	public int update(ShopGrade shopGrade) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopGrade);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<ShopGrade> selectShopGrades(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectShopGrades");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<ShopGrade> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<ShopGrade> selectShopGradesByLowerPointAndUpperPoint(Integer lowerPoint,Integer upperPoint) {
		String sqlId = this.getNamedSqlId("selectShopGradesByLowerPointAndUpperPoint");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("lowerPoint", lowerPoint);
		params.put("upperPoint", upperPoint);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<ShopGrade> selectShopGradesByPoint(Integer point) {
		String sqlId = this.getNamedSqlId("selectShopGradesByPoint");
		return this.getSqlSession().selectList(sqlId, point);
	}

	@Override
	public List<ShopGrade> selectShopGrades() {
		String sqlId = this.getNamedSqlId("selectShopGrades");
		return this.getSqlSession().selectList(sqlId);
	}
}