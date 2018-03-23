package priv.starfish.mall.dao.comn.impl;


import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.BizLicensePicDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.BizLicensePic;


@Component("bizLicensePicDao")
public class BizLicensePicDaoImpl extends BaseDaoImpl<BizLicensePic, Integer> implements BizLicensePicDao {
	
	@Override
	public BizLicensePic selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public int insert(BizLicensePic bizLicensePic) {
		String sqlId = this.getNamedSqlId("insert");
		//
		int maxSeqNo = getEntityMaxSeqNo(BizLicensePic.class);
		bizLicensePic.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, bizLicensePic);
	}
	
	@Override
	public int update(BizLicensePic bizLicensePic) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, bizLicensePic);
	}
	
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public BizLicensePic selectByLicenseId(Integer licenseId) {
		String sqlId = this.getNamedSqlId("selectByLicenseId");
		//
		return this.getSqlSession().selectOne(sqlId, licenseId);
	}
}