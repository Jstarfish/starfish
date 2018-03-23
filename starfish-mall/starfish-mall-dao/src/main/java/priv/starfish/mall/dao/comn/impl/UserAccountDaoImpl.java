package priv.starfish.mall.dao.comn.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.UserAccountDao;

import java.util.List;
import java.util.Map;

@Component("userAccountDao")
public class UserAccountDaoImpl extends BaseDaoImpl<UserAccount, Integer> implements UserAccountDao {
	@Override
	public UserAccount selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(UserAccount userAccount) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userAccount);
	}

	@Override
	public int update(UserAccount userAccount) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userAccount);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<UserAccount> selectByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public int deleteByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("deleteByUserId");
		return this.getSqlSession().delete(sqlId, userId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<UserAccount> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserAccount> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<UserAccount> selectDisByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectDisByUserId");
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public UserAccount selectByUserIdAndBankCodeAndAcctNo(Integer userId, String bankCode, String acctNo) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndBankCodeAndAcctNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("bankCode", bankCode);
		params.put("acctNo", acctNo);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}
}