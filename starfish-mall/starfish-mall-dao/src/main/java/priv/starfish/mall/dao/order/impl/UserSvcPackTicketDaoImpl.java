package priv.starfish.mall.dao.order.impl;

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
import priv.starfish.mall.dao.order.UserSvcPackTicketDao;
import priv.starfish.mall.order.entity.UserSvcPackTicket;

@Component("userSvcPackTicketDao")
public class UserSvcPackTicketDaoImpl extends BaseDaoImpl<UserSvcPackTicket, Integer> implements UserSvcPackTicketDao {
	@Override
	public UserSvcPackTicket selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public UserSvcPackTicket selectByUserIdAndOrderIdAndOrderSvcId(Integer userId, Long orderId, Long orderSvcId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndOrderIdAndOrderSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("orderId", orderId);
		params.put("orderSvcId", orderSvcId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}
	
	@Override
	public int insert(UserSvcPackTicket userSvcPackTicket) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userSvcPackTicket);
	}

	@Override
	public int update(UserSvcPackTicket userSvcPackTicket) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userSvcPackTicket);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int deleteByUserIdAndOrderNo(Integer userId, String orderNo) {
		String sqlId = this.getNamedSqlId("deleteByUserIdAndOrderNo");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("orderNo", orderNo);
		return this.getSqlSession().delete(sqlId, map);
	}
	
	@Override
	public int selectUsedSvcPackTicketCount(Integer userId, String orderNo) {
		String sqlId = this.getNamedSqlId("selectUsedSvcPackTicketCount");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("orderNo", orderNo);
		return this.getSqlSession().selectOne(sqlId,map);
	}
	
	

	@Override
	public int selectUnUsedSvcPackTicketCount(Integer userId) {
		String sqlId = this.getNamedSqlId("selectUnUsedSvcPackTicketCount");
		return this.getSqlSession().selectOne(sqlId,userId);
	}

	@Override
	public PaginatedList<UserSvcPackTicket> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 模糊匹配字段处理
		MapContext filter = paginatedFilter.getFilterItems();
		String svcName = filter.getTypedValue("svcName", String.class);
		filter.remove(svcName);
		if(StrUtil.hasText(svcName)){
			svcName = SqlBuilder.likeStrVal(svcName.toString());
			filter.put("svcName", svcName);
		}
		String userName = filter.getTypedValue("userName", String.class);
		filter.remove(userName);
		if(StrUtil.hasText(userName)){
			userName = SqlBuilder.likeStrVal(userName.toString());
			filter.put("userName", userName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<UserSvcPackTicket> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public UserSvcPackTicket selectOneByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectOneByFilter");
		//
		return this.getSqlSession().selectOne(sqlId, filter);
	}
	
	
}