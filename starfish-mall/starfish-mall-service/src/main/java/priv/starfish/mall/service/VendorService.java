package priv.starfish.mall.service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.vendor.entity.Vendor;

public interface VendorService extends BaseService {

	// ------------------------供应商------------------------------
	/**
	 * 判断是否已经存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:05:42
	 * 
	 * @param name
	 * @return
	 */
	Boolean existVendorByName(String name);

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
	PaginatedList<Vendor> getVendorsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 添加供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午8:00:31
	 * 
	 * @param vendor
	 * @return
	 */
	boolean saveVendor(Vendor vendor);

	/**
	 * 修改供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午8:02:11
	 * 
	 * @param vendor
	 * @return
	 */
	boolean updateVendor(Vendor vendor);

	/**
	 * 删除供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午8:03:12
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteVendorById(Integer id);

	/**
	 * 根据供应商ID获取供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午8:04:09
	 * 
	 * @param id
	 * @return
	 */
	Vendor getVendorById(Integer id);

	/**
	 * 根据供应商phoneNo获取供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 下午5:09:42
	 * 
	 * @param phoneNo
	 * @return
	 */
	Vendor getVendorByPhone(String phoneNo);

	// ------------------------供应商照片------------------------------

}
