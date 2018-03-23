package priv.starfish.mall.dao.vendor;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.vendor.entity.Vendor;

@IBatisSqlTarget
public interface VendorDao extends BaseDao<Vendor, Integer> {
	Vendor selectById(Integer id);

	Vendor selectByCode(String code);

	int insert(Vendor vendor);

	int update(Vendor vendor);

	int deleteById(Integer id);

	/**
	 * 根据name判断是否已经存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:05:42
	 * 
	 * @param name
	 * @return
	 */
	int selectCountByName(String name);

	/**
	 * 分页查询供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:47:36
	 * 
	 * @param paginatedFilter
	 *            like name
	 * @return
	 */
	PaginatedList<Vendor> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据phoneNo查询是否已是供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 下午5:11:35
	 * 
	 * @param phoneNo
	 * @return
	 */
	Vendor selectByPhoneNo(String phoneNo);
}