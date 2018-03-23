package priv.starfish.mall.dao.shop.impl;

import java.util.HashMap;
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
import priv.starfish.mall.dao.shop.DistShopMemoDao;
import priv.starfish.mall.shop.entity.DistShopMemo;

@Component("distShopMemoDao")
public class DistShopMemoDaoImpl extends BaseDaoImpl<DistShopMemo, Integer> implements
        DistShopMemoDao {
	
	@Override
	public int insert(DistShopMemo distShopMemo) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, distShopMemo);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int update(DistShopMemo distShopMemo) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, distShopMemo);
	}
	
	@Override
	public PaginatedList<DistShopMemo> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String keywords = filter.getTypedValue("keywords", String.class);
		filter.remove(keywords);
		if (StrUtil.hasText(keywords)) {
			keywords = SqlBuilder.likeStrVal(keywords);
			filter.put("keywords", keywords);
		}
		String  distShopName = filter.getTypedValue("distShopName", String.class);
		filter.remove( distShopName);
		if (StrUtil.hasText( distShopName)) {
			distShopName = SqlBuilder.likeStrVal(distShopName);
			filter.put("distShopName",  distShopName);
		}
		
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination(),paginatedFilter.getSortItems());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<DistShopMemo> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public DistShopMemo selectById(Integer id){
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public DistShopMemo selectByIdAndDistShopId(Integer id, Integer distShopId) {
		String sqlId = this.getNamedSqlId("selectByIdAndDistShopId");
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("id", id);
		map.put("distShopId", distShopId);
		return this.getSqlSession().selectOne(sqlId, map);
	}
	
}