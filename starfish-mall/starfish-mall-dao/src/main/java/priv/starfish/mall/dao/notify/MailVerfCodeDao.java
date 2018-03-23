package priv.starfish.mall.dao.notify;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.notify.entity.MailVerfCode;

@IBatisSqlTarget
public interface MailVerfCodeDao extends BaseDao<MailVerfCode, Integer> {
	MailVerfCode selectById(Integer id);

	int insert(MailVerfCode mailVerfCode);

	int update(MailVerfCode mailVerfCode);

	int deleteById(Integer id);

	/**
	 * 根据邮箱和验证码查找记录
	 * 
	 * @author 毛智东
	 * @date 2015年7月8日 下午7:59:44
	 * 
	 * @param email
	 * @param vfCode
	 * @return
	 */
	MailVerfCode selectByEmailAndVfCode(String email, String vfCode);
}
