package priv.starfish.mall.dao.order.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.SaleOrderGoodsDao;
import priv.starfish.mall.order.entity.SaleOrderGoods;

@Component("saleOrderGoodsDao")
public class SaleOrderGoodsDaoImpl extends BaseDaoImpl<SaleOrderGoods, Long> implements SaleOrderGoodsDao {
	@Override
	public SaleOrderGoods selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleOrderGoods saleOrderGoods) {
		String sqlId = this.getNamedSqlId("insert");
		if (saleOrderGoods.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SaleOrderGoods.class, "orderId", saleOrderGoods.getOrderId()) + 1;
			saleOrderGoods.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, saleOrderGoods);
	}

	@Override
	public int update(SaleOrderGoods saleOrderGoods) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleOrderGoods);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int selectCountByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectCountByProductId");
		//
		return this.getSqlSession().selectOne(sqlId, productId);
	}

	@Override
	public List<SaleOrderGoods> selectByOrderSvcId(Long orderSvcId) {
		String sqlId = this.getNamedSqlId("selectByOrderSvcId");
		//
		return this.getSqlSession().selectList(sqlId, orderSvcId);
	}

	@Override
	public List<SaleOrderGoods> selectByOrderNoAndUserId(String orderNo, Integer userId) {
		String sqlId = this.getNamedSqlId("selectByOrderNoAndUserId");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("orderNo", orderNo);
		map.put("userId", userId);
		return this.getSqlSession().selectList(sqlId, map);
	}

	@Override
	public List<SaleOrderGoods> selectByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectList(sqlId, orderId);
	}
	
}