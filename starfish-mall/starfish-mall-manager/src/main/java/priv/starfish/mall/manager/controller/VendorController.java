package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.service.VendorService;
import priv.starfish.mall.vendor.entity.Vendor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理处 控制层
 * 
 * @author WJJ
 * @date 2015年9月10日 下午5:29:11
 *
 */
@Controller
@RequestMapping("/vendor")
public class VendorController extends BaseController {

	@Resource
	private VendorService vendorService;
	@Resource
	private UserService userService;

	// --------------------------------------- vendor ---list-------------------------------------------

	@Remark("供应商列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toVendorList() {
		return "vendor/vendorList";
	}

	/**
	 * 分页查询供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:38:55
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询供应商")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Vendor> getVendors(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Vendor> paginatedList = vendorService.getVendorsByFilter(paginatedFilter);
		//
		JqGridPage<Vendor> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:38:05
	 * 
	 * @param vendor
	 * @return
	 */
	@Remark("保存供应商")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveVendor(@RequestBody Vendor vendor) {
		Result<Integer> result = Result.newOne();
		boolean ok = vendorService.saveVendor(vendor);
		if (ok) {
			result.data = vendor.getId();
			result.message = "保存成功";
		} else {
			result.type = Type.warn;
			result.message = "保存失败";
		}
		return result;
	}

	/**
	 * 修改供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:38:19
	 * 
	 * @param vendor
	 * @return
	 */
	@Remark("修改供应商")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> updateVendor(@RequestBody Vendor vendor) {
		Result<Integer> result = Result.newOne();
		boolean ok = vendorService.updateVendor(vendor);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	/**
	 * 删除供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:38:36
	 * 
	 * @param vendor
	 * @return
	 */
	@Remark("删除供应商")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteVendor(@RequestBody Vendor vendor) {
		Result<?> result = Result.newOne();
		// 
		boolean ok = vendorService.deleteVendorById(vendor.getId());
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 根据供应商ID获取供应商
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:41:58
	 * 
	 * @param vendor
	 * @return
	 */
	@Remark("根据供应商ID获取供应商")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Vendor> getVendorById(@RequestBody Vendor vendor) {
		Result<Vendor> result = Result.newOne();
		result.data = vendorService.getVendorById(vendor.getId());
		return result;
	}

	/**
	 * 根据名称判断是否已存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月13日 下午7:19:39
	 * 
	 * @param vendor
	 * @return
	 */
	@Remark("根据名称判断是否已存在")
	@RequestMapping(value = "/exist/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> existVendorByName(@RequestBody Vendor vendor) {
		Result<Boolean> result = Result.newOne();
		result.data = vendorService.existVendorByName(vendor.getName());
		return result;
	}

	/**
	 * 添加供应商时 验证手机号 是否已经存在
	 * 
	 * @author "WJJ"
	 * @date 2015年10月20日 下午4:48:22
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("添加供应商时 验证手机号")
	@RequestMapping(value = "/exist/by/phoneNo", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getAgentExistByPhoneNo(@RequestBody MapContext requestData) {
		Result<Map<String, Object>> result = Result.newOne();
		String phoneNo = requestData.getTypedValue("phoneNo", String.class);
		// {"user":user}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// 验证手机号是否已被注册
		if (StrUtil.hasText(phoneNo)) {
			User user = userService.getUserByPhoneNo(phoneNo);
			if (user != null) {
				dataMap.put("userFlag", true);
				dataMap.put("user", user);
				// 验证系统用户是否为供应商
				Vendor vendor = vendorService.getVendorByPhone(user.getPhoneNo());
				if (vendor != null) {
					dataMap.put("vendorFlag", true);
				} else {
					// 是系统用户，非供应商
					dataMap.put("vendorFlag", false);
				}
			} else {
				// 系统用户不存在
				dataMap.put("userFlag", false);
			}
		}
		result.data = dataMap;
		return result;
	}
}
