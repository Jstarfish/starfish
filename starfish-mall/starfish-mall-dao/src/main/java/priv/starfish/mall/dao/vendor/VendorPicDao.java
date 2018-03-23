package priv.starfish.mall.dao.vendor;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.vendor.entity.VendorPic;

@IBatisSqlTarget
public interface VendorPicDao extends BaseDao<VendorPic, Long> {
	VendorPic selectById(Long id);

	VendorPic selectByCode(String code);

	int insert(VendorPic vendorPicture);

	int update(VendorPic vendorPicture);

	int deleteById(Long id);

	/**
	 * 查找某供应商的照片集合
	 * 
	 * @author "WJJ"
	 * @date 2015年10月16日 上午11:19:06
	 * 
	 * @param userId
	 * @return List<VendorPicture>
	 */
	List<VendorPic> selectByVendorId(Integer userId);
}