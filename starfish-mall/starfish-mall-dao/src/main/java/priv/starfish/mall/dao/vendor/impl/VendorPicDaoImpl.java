package priv.starfish.mall.dao.vendor.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.vendor.VendorPicDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.vendor.entity.VendorPic;

@Component("vendorPicDao")
public class VendorPicDaoImpl extends BaseDaoImpl<VendorPic, Long> implements VendorPicDao {
	@Override
	public VendorPic selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public VendorPic selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public int insert(VendorPic vendorPicture) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, vendorPicture);
	}

	@Override
	public int update(VendorPic vendorPicture) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, vendorPicture);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<VendorPic> selectByVendorId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByVendorId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}
}