package priv.starfish.mall.dao.comn;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.BizLicense;


@IBatisSqlTarget
public interface BizLicenseDao extends BaseDao<BizLicense, Integer> {
	
	BizLicense selectById(Integer id);
	
	BizLicense selectByUserIdAndRegNo(Integer userId, String regNo);
	
	int insert(BizLicense bizLicense);
	
	int update(BizLicense bizLicense);
	
	int deleteById(Integer id);

	/**
	 * 
	 * 获取指定条件下的营业执照列表
	 * @author guoyn
	 * @date 2015年10月10日 下午2:15:15
	 * 
	 * @param paginatedFilter =userId
	 * @return PaginatedList<BizLicense>
	 */
	PaginatedList<BizLicense> selectByFilter(PaginatedFilter paginatedFilter);

	List<BizLicense> selectByUserId(Integer merchId);

}