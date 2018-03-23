package priv.starfish.mall.dao.merchant;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.merchant.entity.MerchantPic;

@IBatisSqlTarget
public interface MerchantPicDao extends BaseDao<MerchantPic, Long> {
	MerchantPic selectById(Long id);

	int insert(MerchantPic merchantPic);

	int update(MerchantPic merchantPic);

	int deleteById(Long id);

	/**
	 * 查找某商户的照片集合
	 * 
	 * @author guoyn
	 * @date 2015年10月9日 下午4:37:14
	 * 
	 * @param userId
	 * @return List<MerchantPic>
	 */
	List<MerchantPic> selectByMerchantId(Integer userId);
}