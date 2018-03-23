package priv.starfish.mall.dao.merchant.impl;

import java.util.Date;
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
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.merchant.entity.Merchant;

@Component("merchantDao")
public class MerchantDaoImpl extends BaseDaoImpl<Merchant, Integer> implements MerchantDao {
	@Override
	public Merchant selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(Merchant merchant) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, merchant);
	}

	@Override
	public int update(Merchant merchant) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, merchant);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Merchant> selectMerchants(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectMerchants");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		filter.remove("name");
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Merchant> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Merchant> selectAllMerchants(MapContext param) {
		Object realName = param.get("realName");
		if (realName != null) {
			realName = SqlBuilder.likeStrVal(realName.toString());
			param.put("realName", realName);
		}

		String sqlId = this.getNamedSqlId("selectAllMerchants");
		return this.getSqlSession().selectList(sqlId, param);
	}

	@Override
	public Merchant selectByPhoneNo(String phoneNo) {
		String sqlId = this.getNamedSqlId("selectByPhoneNo");
		return this.getSqlSession().selectOne(sqlId, phoneNo);
	}

	@Override
	public List<Merchant> selectBySettleDay(Date settleDay) {
		String sqlId = this.getNamedSqlId("selectBySettleDay");
		//
		return this.getSqlSession().selectList(sqlId, settleDay);
	}

	@Override
	public List<Merchant> selectMerchantsAsEnabled(Boolean disabled) {
		String sqlId = this.getNamedSqlId("selectMerchantsAsEnabled");
		//
		return this.getSqlSession().selectList(sqlId, disabled);
	}
}
