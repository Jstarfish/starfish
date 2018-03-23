package priv.starfish.mall.dao.notify.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.notify.SmsVerfCodeDao;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;

@Component("smsVerfCodeDao")
public class SmsVerfCodeDaoImpl extends BaseDaoImpl<SmsVerfCode, Integer> implements SmsVerfCodeDao {
	@Override
	public SmsVerfCode selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SmsVerfCode selectByPhoneNoAndVfCodeAndInvalid(String phoneNo, String vfCode, Boolean invalid) {
		String sqlId = this.getNamedSqlId("selectByPhoneNoAndVfCodeAndInvalid");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("phoneNo", phoneNo);
		params.put("vfCode", vfCode);
		params.put("invalid", invalid);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SmsVerfCode smsVerfCode) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, smsVerfCode);
	}

	@Override
	public int update(SmsVerfCode smsVerfCode) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, smsVerfCode);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int updateInvalidByPhoneNoAndUsage(String phoneNo, SmsUsage usage) {
		String sqlId = this.getNamedSqlId("updateInvalidByPhoneNoAndUsage");
		//
		MapContext params = new MapContext();
		params.put("phoneNo", phoneNo);
		params.put("usage", usage);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public SmsVerfCode selectByPhoneNoAndVfCodeAndUsage(String phoneNo, String vfCode, SmsUsage usage) {
		String sqlId = this.getNamedSqlId("selectByPhoneNoAndVfCodeAndUsage");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("phoneNo", phoneNo);
		params.put("vfCode", vfCode);
		params.put("usage", usage);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<SmsVerfCode> selectByReqIpAndSendTime(String reqIp, String sendTime, String limitUsage) {
		String sqlId = this.getNamedSqlId("selectByReqIpAndSendTime");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("reqIp", reqIp);
		sendTime = SqlBuilder.likeStrVal(sendTime);
		params.put("sendTime", sendTime);
		limitUsage = SqlBuilder.likeStrVal(limitUsage);
		params.put("usage", limitUsage);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}
