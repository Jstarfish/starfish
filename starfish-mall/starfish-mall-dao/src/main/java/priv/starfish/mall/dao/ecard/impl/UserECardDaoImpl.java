package priv.starfish.mall.dao.ecard.impl;

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
import priv.starfish.mall.dao.ecard.UserECardDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.ecard.entity.UserECard;

@Component("userECardDao")
public class UserECardDaoImpl extends BaseDaoImpl<UserECard, Integer> implements UserECardDao {
	@Override
	public UserECard selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(UserECard userECard) {
		String sqlId = this.getNamedSqlId("insert");
		//
		Integer seqNo = getEntityMaxSeqNo(UserECard.class, "userId", userECard.getUserId()) + 1;
		userECard.setSeqNo(seqNo);
		userECard.setInvalid(false);
		userECard.setDeleted(false);
		return this.getSqlSession().insert(sqlId, userECard);
	}

	@Override
	public int update(UserECard userECard) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userECard);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<UserECard> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		MapContext filter = paginatedFilter.getFilterItems();
		String cardNo = filter.getTypedValue("cardNo", String.class);
		filter.remove(cardNo);
		if (StrUtil.hasText(cardNo)) {
			cardNo = SqlBuilder.likeStrVal(cardNo);
			filter.put("cardNo", cardNo);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<UserECard> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PaginatedList<UserECard> selectByFilterBack(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterBack");

		/*MapContext filter = paginatedFilter.getFilterItems();
		
		Integer seqNo = filter.getTypedValue("seqNo", Integer.class);
		filter.remove(seqNo);
		if(null != seqNo){
			if(seqNo == 0){
				filter.put("compare", 0);
			}else{
				filter.put("compare", 1);
			}
		}*/
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<UserECard> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public List<UserECard> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilterNormal");
		// 加通配符
		String cardNo = filter.getTypedValue("cardNo", String.class);
		if (cardNo != null) {
			filter.remove(cardNo);
			if (StrUtil.hasText(cardNo)) {
				cardNo = SqlBuilder.likeStrVal(cardNo);
				filter.put("cardNo", cardNo);
			}
		}

		return this.getSqlSession().selectList(sqlId, filter);
	}
	
	@Override
	public Map<Integer, Integer> selectECardShopsByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectECardShopsByUserId");
		return this.getSqlSession().selectMap(sqlId, userId,"shopId");
	}

	@Override
	public Integer selectCountByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectCountByFilter");
		//
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public int updateForMerge(MapContext params) {
		String sqlId = this.getNamedSqlId("updateForMerge");
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateForPresent(MapContext params) {
		String sqlId = this.getNamedSqlId("updateForPresent");
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateForDelete(MapContext params) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateUnbind(UserECard userECard) {
		String sqlId = this.getNamedSqlId("updateUnbind");
		//
		return this.getSqlSession().update(sqlId, userECard);
	}

	@Override
	public int updateForChangeRemainVal(UserECard userECard) {
		String sqlId = this.getNamedSqlId("updateForChangeRemainVal");
		//
		return this.getSqlSession().update(sqlId, userECard);
	}

	@Override
	public UserECard selectByCardNo(String cardNo) {
		String sqlId = this.getNamedSqlId("selectByCardNo");
		//
		return this.getSqlSession().selectOne(sqlId, cardNo);
	}
}