package priv.starfish.mall.dao.merchant.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.merchant.MerchantPicDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.merchant.entity.MerchantPic;

@Component("merchantPicDao")
public class MerchantPicDaoImpl extends BaseDaoImpl<MerchantPic, Long> implements MerchantPicDao {
	@Override
	public MerchantPic selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(MerchantPic merchantPic) {
		String sqlId = this.getNamedSqlId("insert");
		//
		int maxSeqNo = getEntityMaxSeqNo(MerchantPic.class);
		merchantPic.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, merchantPic);
	}

	@Override
	public int update(MerchantPic merchantPic) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, merchantPic);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<MerchantPic> selectByMerchantId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByMerchantId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}
}