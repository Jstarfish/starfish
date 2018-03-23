package priv.starfish.mall.dao.notify;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;

@IBatisSqlTarget
public interface SmsVerfCodeDao extends BaseDao<SmsVerfCode, Integer> {
	SmsVerfCode selectById(Integer id);

	SmsVerfCode selectByPhoneNoAndVfCodeAndInvalid(String phoneNo, String vfCode, Boolean invalid);

	int insert(SmsVerfCode smsVerfCode);

	int update(SmsVerfCode smsVerfCode);

	int deleteById(Integer id);

	/**
	 * 手机号和用途的记录全设为无效的
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 下午4:13:37
	 * 
	 * @param phoneNo
	 * @param useage
	 * @return
	 */
	int updateInvalidByPhoneNoAndUsage(String phoneNo, SmsUsage usage);

	/**
	 * 根据手机号、验证码、用途查找有效的记录
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 下午4:37:32
	 * 
	 * @param phoneNo
	 * @param vfCode
	 * @param usage
	 * @return
	 */
	SmsVerfCode selectByPhoneNoAndVfCodeAndUsage(String phoneNo, String vfCode, SmsUsage usage);

	List<SmsVerfCode> selectByReqIpAndSendTime(String reqIp, String sendTime, String limitUsage);
}