package priv.starfish.mall.service;

import priv.starfish.mall.comn.entity.User3rd;

public interface User3rdService extends BaseService {

	User3rd getById(Integer id);

	User3rd getByAppIdAndOpenId(Integer appId, String openId);

	Integer getSysUserIdById(Integer id);

	Integer getSysUserIdByAppIdAndOpenId(Integer appId, String openId);

}
