package priv.starfish.mall.portal.controller;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.MobileAppInfo;
import priv.starfish.common.helper.AppHelper;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.SelectList;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.WebUtil;
import priv.starfish.mall.comn.entity.Agreement;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.pay.entity.PayWay;
import priv.starfish.mall.service.MallService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Remark("全局基础设置")
@Controller
@RequestMapping(value = "/setting")
public class SettingController extends BaseController {

	// ----------------------------------地区------------------------------------
	@Resource
	private SettingService settingService;

	@Resource
	MallService mallService;

	long REGION_EXPIRES_MINUTES = 3600 * 24 * 30;

	/**
	 * 获取地区列表信息
	 * 
	 * @param parentId
	 * @return
	 */
	@Remark("获取地区列表信息")
	@RequestMapping(value = "/region/list/get", method = RequestMethod.GET)
	@ResponseBody
	public void getRegionList(@RequestParam(required = false) Integer parentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		long curTimeInMs = System.currentTimeMillis();
		if (headerLastModified >= curTimeInMs) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			Result<List<Region>> result = Result.newOne();
			try {
				result.data = CacheHelper.getRegionsByParentId(parentId);
				//
				response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(curTimeInMs + REGION_EXPIRES_MINUTES));
				//
			} catch (Exception e) {
				result.type = Type.error;
				result.message = "初始化地区错误！";
			}
			//
			PrintWriter writer = WebUtil.responseAsJson(response, result, true);
			writer.close();
		}
	}

	@Remark("获取地区列表信息")
	@RequestMapping(value = "/region/selectList/get", method = RequestMethod.GET)
	@ResponseBody
	public void getRegionSelectList(@RequestParam(required = false) Integer parentId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		long curTimeInMs = System.currentTimeMillis();
		if (headerLastModified >= curTimeInMs) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			Result<SelectList> result = Result.newOne();
			try {
				// 根据地区父id和级别查询下一级地区
				List<Region> regionList = CacheHelper.getRegionsByParentId(parentId);
				// 装载地区数据
				SelectList selectList = SelectList.newOne();

				if (CollectionUtils.isNotEmpty(regionList)) {
					selectList.setUnSelectedItem("", "- 请选择 -");
					for (Region region : regionList) {
						selectList.addItem(region.getId() + "", region.getName());
					}
					selectList.setDefaultValue("");
				}
				result.data = selectList;
				//
				response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(curTimeInMs + REGION_EXPIRES_MINUTES));
				//
			} catch (Exception e) {
				result.type = Type.error;
				result.message = "获取地区失败！";
			}
			PrintWriter writer = WebUtil.responseAsJson(response, result, true);
			writer.close();
		}
	}

	/**
	 * 添加地区
	 * 
	 * @author 郝江奎
	 * @date 2015年11月4日 下午4:35:10
	 * 
	 * @return
	 * @throws IOException
	 */
	@Remark("获取地区")
	@RequestMapping(value = "/region/get/by/id", method = RequestMethod.GET)
	@ResponseBody
	public void getRegionById(@RequestParam(value = "id") Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		long headerLastModified = request.getDateHeader("If-Modified-Since");
		long curTimeInMs = System.currentTimeMillis();
		if (headerLastModified >= curTimeInMs) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		} else {
			Result<Region> result = Result.newOne();
			try {
				result.data = CacheHelper.getRegionById(id);
				//
				response.setDateHeader("Last-Modified", DateUtil.ceilToSecond(curTimeInMs + REGION_EXPIRES_MINUTES));
				//
			} catch (Exception e) {
				result.type = Type.error;
				result.message = "初始化地区错误！";
			}
			//
			PrintWriter writer = WebUtil.responseAsJson(response, result, true);
			writer.close();
		}
	}

	/**
	 * 获取商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:45:13
	 * 
	 *            商城id
	 * @return 返回商城基本信息
	 */
	@Remark("获取商城基本信息")
	@RequestMapping(value = "/mall/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<MallDto> getMall(HttpServletRequest request) {
		Result<MallDto> result = Result.newOne();
		//
		MallDto mallDto = mallService.getMallInfo();
		result.data = mallDto;
		//
		return result;
	}

	/**
	 * 获取会员协议信息
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:30:15
	 * 
	 * @param request
	 * @return 返回会员协议信息
	 */
	@Remark("获取会员协议信息")
	@RequestMapping(value = "/memberAgreement/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agreement> getMemberAgreement(HttpServletRequest request) {
		Result<Agreement> result = Result.newOne();

		try {
			result.data = mallService.getMemberAgreement();
		} catch (Exception e) {
			result.type = Result.Type.error;
			result.message = "获取会员协议失败";
		}

		return result;
	}

	// TODO------------------begin----------------支付方式------------------------------------
	/**
	 * 获取处于启用状态的支付方式
	 * 
	 * @author "WJJ"
	 * @date 2015年11月20日 下午5:21:59
	 * 
	 * @return
	 */
	@Remark("获取处于启用状态的支付方式")
	@RequestMapping(value = "/payWay/usbale/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<PayWay>> getUsablePayWay() {
		Result<List<PayWay>> result = Result.newOne();
		result.data = settingService.getUsablePayWay();
		return result;
	}

	// ------------------end----------------支付方式------------------------------------

	@Remark("获取App配置信息")
	@RequestMapping(value = "/app/info/get", method = RequestMethod.GET)
	@ResponseBody
	public Result<MobileAppInfo> getAppData(@RequestParam("appName") String appName) {
		Result<MobileAppInfo> result = Result.newOne();

		MobileAppInfo appInfo = AppHelper.getLocalObject(AppBase.LocalCacheKeys.MOBILE_APP_INFO_PREFIX + appName);
		if (appInfo != null) {
			result.data = appInfo;
		}

		return result;
	}
}
