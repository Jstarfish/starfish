package priv.starfish.mall.dao.order.impl;

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
import priv.starfish.mall.dao.order.SaleOrderInnerAmountDao;
import priv.starfish.mall.order.entity.SaleOrderInnerAmount;

@Component("saleOrderInnerAmountDao")
public class SaleOrderInnerAmountDaoImpl extends BaseDaoImpl<SaleOrderInnerAmount, Long> implements SaleOrderInnerAmountDao {

	@Override
	public int insert(SaleOrderInnerAmount saleOrderInnerAmount) {
		String sqlId = this.getNamedSqlId("insert");
		if (saleOrderInnerAmount.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SaleOrderInnerAmount.class) + 1;
			saleOrderInnerAmount.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, saleOrderInnerAmount);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int update(SaleOrderInnerAmount saleOrderInnerAmount) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, saleOrderInnerAmount);
	}

	@Override
	public PaginatedList<SaleOrderInnerAmount> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String extValStr = filter.getTypedValue("extValStr", String.class);
		filter.remove(extValStr);
		if (StrUtil.hasText(extValStr)) {
			extValStr = SqlBuilder.likeStrVal(extValStr);
			filter.put("extValStr", extValStr);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SaleOrderInnerAmount> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public SaleOrderInnerAmount selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<SaleOrderInnerAmount> selectByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectList(sqlId, orderId);
	}

}