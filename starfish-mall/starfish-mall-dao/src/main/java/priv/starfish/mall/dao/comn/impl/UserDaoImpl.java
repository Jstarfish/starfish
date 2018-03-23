package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.PasswordUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.entity.User;

@Component("userDao")
public class UserDaoImpl extends BaseDaoImpl<User, Integer> implements UserDao {
	@Override
	public User selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public User selectByPhoneNo(String phoneNo) {
		String sqlId = this.getNamedSqlId("selectByPhoneNo");
		//
		return this.getSqlSession().selectOne(sqlId, phoneNo);
	}

	@Override
	public int insert(User user) {
		// 初始化默认值
		if (user.getLocked() == null) {
			user.setLocked(false);
		}
		if (user.getVerified() == null) {
			user.setVerified(false);
		}
		if (StrUtil.hasText(user.getPassword())) {
			String salt = PasswordUtil.generateSaltStr();
			user.setSalt(salt);
			String enPassword = PasswordUtil.encrypt(user.getPassword(), salt);
			user.setPassword(enPassword);
		}

		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, user);
	}

	@Override
	public int update(User user) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, user);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByPhoneNo(String phoneNo) {
		String sqlId = this.getNamedSqlId("deleteByPhoneNo");
		//
		return this.getSqlSession().delete(sqlId, phoneNo);
	}

	@Override
	public boolean existsByPhoneNo(String phoneNo) {
		String sqlId = this.getNamedSqlId("existsByPhoneNo");
		//
		return this.getSqlSession().selectOne(sqlId, phoneNo);
	}

	@Override
	public boolean existsOtherByPhoneNo(String phoneNo, Integer notId) {
		String sqlId = this.getNamedSqlId("existsOtherByPhoneNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("phoneNo", phoneNo);
		params.put("notId", notId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public User selectByEmail(String email) {
		String sqlId = this.getNamedSqlId("selectByEmail");
		//
		return this.getSqlSession().selectOne(sqlId, email);
	}

	@Override
	@SuppressWarnings({"unchecked","rawtypes"})
	public PaginatedList<User> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			nickName = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickName);
		}
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<User> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({"unchecked","rawtypes"})
	public PaginatedList<User> selectByFuzzyFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFuzzyFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			nickName = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickName);
		}
		String phoneNo = filterItems.getTypedValue("phoneNo", String.class);
		filterItems.remove("phoneNo");
		if (StrUtil.hasText(phoneNo)) {
			phoneNo = SqlBuilder.likeStrVal(phoneNo);
			filterItems.put("phoneNo", phoneNo);
		}
		//
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<User> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<User> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilterNormal");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

}