package priv.starfish.mall.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.mall.dao.comn.User3rdDao;
import priv.starfish.mall.comn.entity.User3rd;
import priv.starfish.mall.service.User3rdService;

@Service("user3rdService")
public class User3rdServiceImpl extends BaseServiceImpl implements User3rdService {

	@Resource
	User3rdDao user3rdDao;

	@Override
	public User3rd getById(Integer id) {
		return user3rdDao.selectById(id);
	}

	@Override
	public User3rd getByAppIdAndOpenId(Integer appId, String openId) {
		return user3rdDao.selectByAppIdAndOpenId(appId, openId);
	}

	@Override
	public Integer getSysUserIdById(Integer id) {
		return user3rdDao.selectSysUserIdById(id);
	}

	@Override
	public Integer getSysUserIdByAppIdAndOpenId(Integer appId, String openId) {
		return user3rdDao.selectSysUserIdByAppIdAndOpenId(appId, openId);
	}

}
