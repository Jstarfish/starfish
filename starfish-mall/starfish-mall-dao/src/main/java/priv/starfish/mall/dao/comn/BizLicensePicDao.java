package priv.starfish.mall.dao.comn;


import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.BizLicensePic;


@IBatisSqlTarget
public interface BizLicensePicDao extends BaseDao<BizLicensePic, Integer> {
	
	BizLicensePic selectById(Integer id);
	
	int insert(BizLicensePic bizLicensePic);
	
	int update(BizLicensePic bizLicensePic);
	
	int deleteById(Integer id);

	/**
	 * 查询某营业执照图片
	 * 
	 * @author guoyn
	 * @date 2015年10月12日 下午2:46:01
	 * 
	 * @param licenseId
	 * @return BizLicensePic
	 */
	BizLicensePic selectByLicenseId(Integer licenseId);
	
}