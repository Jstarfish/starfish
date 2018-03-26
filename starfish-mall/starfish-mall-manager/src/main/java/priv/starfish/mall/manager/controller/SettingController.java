package priv.starfish.mall.manager.controller;

import freemarker.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.Converter;
import priv.starfish.common.cms.FreeMarkerService;
import priv.starfish.common.cms.StringFreeMarkerServiceImpl;
import priv.starfish.common.mail.JavaMailServiceImpl;
import priv.starfish.common.map.HttpIntResultMappingExecutor;
import priv.starfish.common.map.HttpMapResultMappingExecutor;
import priv.starfish.common.map.ParamMapping;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.*;
import priv.starfish.common.sms.SmsErrorCode;
import priv.starfish.common.util.*;
import priv.starfish.mall.comn.entity.BizParam;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.SysParam;
import priv.starfish.mall.logistic.dict.LogisErrorCode;
import priv.starfish.mall.logistic.dto.LogisApiDto;
import priv.starfish.mall.logistic.entity.DeliveryWay;
import priv.starfish.mall.logistic.entity.LogisApi;
import priv.starfish.mall.logistic.entity.LogisCom;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.notify.dto.SmsApiDto;
import priv.starfish.mall.notify.dto.TemplatePreview;
import priv.starfish.mall.notify.entity.*;
import priv.starfish.mall.pay.entity.PayWay;
import priv.starfish.mall.service.SettingService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.Map.Entry;

@Remark("全局基础设置")
@Controller
@RequestMapping(value = "/setting")
public class SettingController extends BaseController {

	@Resource
	private SettingService settingService;

	// -------------------------------系统业务参数-----------------------------------
	/**
	 * 系统参数页面
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午5:51:45
	 * 
	 * @return 返回系统参数页面
	 */
	@Remark("系统参数页面")
	@RequestMapping(value = "/sysParam/jsp", method = RequestMethod.GET)
	public String toSysParamPage() {
		return "setting/sysParam";
	}

	/**
	 * 获取所有系统参数页面
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午5:54:28
	 * 
	 * @return 返回查询结果
	 */
	@Remark("获取所有系统参数")
	@RequestMapping(value = "/sysParams/all/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SysParam>> getAllSysParams() {
		Result<List<SysParam>> result = Result.newOne();

		try {
			result.data = settingService.getAllSysParams();
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化系统参数数据出错！";
		}

		return result;
	}

	/**
	 * 批量更新系统参数
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午5:55:27
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("批量更新系统参数")
	@RequestMapping(value = "/sysParams/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateSysParamList(@RequestBody MapContext requestData) {
		Result<Object> result = Result.newOne();

		try {
			SysParam bean;
			List<SysParam> sysParamList = new ArrayList<SysParam>(0);
			for (Entry<String, Object> entity : requestData.entrySet()) {
				bean = settingService.getSysParamByCode(entity.getKey());
				bean.setValue(entity.getValue().toString());
				sysParamList.add(bean);
			}
			boolean ok = settingService.updateSysParams(sysParamList);
			if (ok) {
				result.message = "保存成功！";
			} else {
				result.type = Type.error;
				result.message = "保存失败！";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "保存失败！";
			e.printStackTrace();
		}

		return result;
	}

	// -------------------------------商城业务参数-----------------------------------
	/**
	 * 商城业务参数查看页面
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:08:04
	 * 
	 * @return 返回页面路径
	 */
	@Remark("商城业务参数查看页面")
	@RequestMapping(value = "/bizParam/jsp", method = RequestMethod.GET)
	public String toBizParamIndexPage() {
		return "setting/bizParam";
	}

	/**
	 * 获取所有商城业务参数信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:09:49
	 * 
	 * @return 返回参数列表
	 */
	@Remark("获取所有商城业务参数信息")
	@RequestMapping(value = "/bizParams/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<BizParam>> getAllBizParams() {
		Result<List<BizParam>> result = Result.newOne();

		try {
			List<BizParam> bizParamList = settingService.getBizParams();
			if (CollectionUtils.isEmpty(bizParamList)) {
				result.type = Type.error;
				result.message = "系统没有业务参数数据，请先在业务参数表里添加配置项！";
			} else {
				result.data = bizParamList;
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化数据出错！";
		}

		return result;
	}

	/**
	 * 批量更新商城业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:11:14
	 * 
	 * @param requestData
	 *            要更新的数据
	 * @return 返回更新结果
	 */
	@Remark("批量更新商城业务参数")
	@RequestMapping(value = "/bizParams/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateBizParamList(@RequestBody MapContext requestData) {
		Result<Object> result = Result.newOne();
		String msg = "";

		try {
			BizParam bean;
			List<BizParam> bizParamList = new ArrayList<BizParam>(0);
			for (Entry<String, Object> entity : requestData.entrySet()) {
				bean = settingService.getBizParamByCode(entity.getKey());
				bean.setValue(entity.getValue().toString());
				bizParamList.add(bean);
			}

			boolean ok = settingService.updateBizParams(bizParamList);
			if (ok) {
				msg = "更新成功！";
			} else {
				result.type = Type.error;
				msg = "更新失败！";
			}
		} catch (Exception e) {
			result.type = Type.error;
			msg = "更新失败！";
		}

		result.message = msg;

		return result;
	}

	// ----------------------------------地区------------------------------------

	long REGION_EXPIRES_MINUTES = 3600 * 24 * 30;

	//
	@Remark("获取地区列表信息")
	@RequestMapping(value = "/region/selectList/get", method = RequestMethod.GET)
	@ResponseBody
	public Result<SelectList> getRegionSelectList(@RequestParam(required = false) Integer parentId) {
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
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "获取地区失败！";
		}
		return result;
	}

	/**
	 * 根据地区父id获取下一级地区（供地区设置调用）
	 * 
	 * @author 王少辉
	 * @date 2015年8月14日 上午10:02:42
	 * 
	 * @param requestData
	 * @return
	 */
	@Remark("获取地区列表信息")
	@RequestMapping(value = "/children/regions/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<TreeNode>> getRegionByParentId(@RequestBody MapContext requestData) {
		Result<List<TreeNode>> result = Result.newOne();
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>(0);

		try {
			// 根据地区父id和级别查询下一级地区
			Integer parentId = requestData.getTypedValue("parentId", Integer.class);
			List<Region> regionList = CacheHelper.getRegionsByParentId(parentId);
			if (CollectionUtils.isNotEmpty(regionList)) {
				TreeNode treeNode = null;
				for (Region region : regionList) {
					treeNode = new TreeNode();
					TypeUtil.copyProperties(region, treeNode);
					if (region.getLevel() < Region.MAX_LEVEL) {
						treeNode.setLeaf(true);
						treeNode.setIsParent(true);
					}
					treeNodeList.add(treeNode);
				}
			}

			result.data = treeNodeList;
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化地区错误！";
		}

		return result;
	}

	/**
	 * 地区设置页面
	 * 
	 * @author 王少辉
	 * @date 2015年5月23日 下午3:49:58
	 * 
	 * @return 跳转地区设置页面
	 */
	@Remark("地区设置页面")
	@RequestMapping(value = "/region/tree/jsp", method = RequestMethod.GET)
	public String toRegionTreeJsp() {
		return "setting/region";
	}

	/**
	 * 获取地区树
	 * 
	 * @author 王少辉
	 * @date 2015年5月23日 下午7:16:38
	 * 
	 * @return 返回获取地区树
	 */
	@Remark("获取地区树")
	@RequestMapping(value = "/region/tree/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<TreeNode>> getRegionTree(@RequestBody Region region) {
		Result<List<TreeNode>> result = Result.newOne();

		try {
			result.data = settingService.getRegionsTree(region);
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化地区树信息错误";
		}

		return result;
	}

	/**
	 * 更新地区树名称
	 * 
	 * @author 王少辉
	 * @date 2015年5月23日 下午7:16:38
	 * 
	 * @return 返回更新结果
	 */
	@Remark("更新地区树名称")
	@RequestMapping(value = "/region/tree/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<TreeNode>> updateRegionTree(@RequestBody Region region) {
		Result<List<TreeNode>> result = Result.newOne();
		List<TreeNode> resultList = new ArrayList<TreeNode>(0);

		try {
			if (region == null || region.getId() == null) {
				result.message = "请求错误";
				return result;
			}

			Region bean = settingService.getRegionById(region.getId());
			bean.setName(region.getName());
			String code = StrUtil.chsToPinyin(region.getName());
			bean.setCode(code);
			boolean ok = settingService.updateRegion(bean);
			if (ok) {
				resultList = settingService.getRegionsTree(null);
				result.data = resultList;
				result.message = "更新成功";
			} else {
				result.message = "更新失败";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "初始化地区树信息错误";
		}

		return result;
	}

	/**
	 * 添加地区
	 * 
	 * @author 毛智东
	 * @date 2015年5月25日 下午7:35:10
	 * 
	 * @param region
	 * @return
	 */
	@Remark("添加地区")
	@RequestMapping(value = "/region/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Region> addRegion(@RequestBody Region region) {
		Result<Region> result = Result.newOne();
		String name = region.getName();
		region.setPy(StrUtil.chsToPy(name));
		result.message = settingService.saveRegion(region) ? "保存成功！" : "保存失败！";
		result.data = region;
		return result;
	}

	/**
	 * 删除地区
	 * 
	 * @author 毛智东
	 * @date 2015年5月25日 下午9:13:50
	 * 
	 * @param ids
	 * @return
	 */
	@Remark("删除地区")
	@RequestMapping(value = "/regions/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteRegions(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		//
		boolean success = settingService.deleteRegionsByIds(ids);
		result.type = success ? Type.info : Type.warn;
		result.message = success ? "操作成功！" : "操作失败！";
		//
		return result;
	}

	/**
	 * 添加地区
	 * 
	 * @author 毛智东
	 * @date 2015年5月25日 下午7:35:10
	 * 
	 * @return
	 */
	@Remark("获取地区")
	@RequestMapping(value = "/region/get/by/id", method = RequestMethod.GET)
	@ResponseBody
	public Result<Region> getRegionById(@RequestParam(required = false) Integer id) {
		Result<Region> result = Result.newOne();
		result.data = settingService.getRegionById(id);
		return result;
	}

	// ----------------------------------邮箱服务器------------------------------------

	/**
	 * 邮箱服务器配置页面
	 * 
	 * @author 毛智东
	 * @date 2015年5月18日 下午5:43:40
	 * 
	 * @return
	 */
	@Remark("邮箱服务器配置")
	@RequestMapping(value = "/mail/servers/jsp", method = RequestMethod.GET)
	public String getMailServers() {
		return "setting/mailServers";
	}

	@Remark("分页查询模块")
	@RequestMapping(value = "/mail/servers/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<MailServer> getMailServers(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<MailServer> paginatedList = settingService.getMailServers(paginatedFilter);
		JqGridPage<MailServer> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 邮箱服务器删除
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 下午4:17:37
	 * 
	 * @return
	 */
	@Remark("邮箱服务器删除")
	@RequestMapping(value = "/mail/server/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteMailServer(@RequestParam("id") Integer id) {
		Result<Object> result = Result.newOne();
		result.message = settingService.deleteMailServerById(id) ? "删除成功" : "删除失败";
		return result;
	}

	/**
	 * 邮箱服务器名称是否占用
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 下午6:32:26
	 * 
	 * @param name
	 * @return
	 */
	@Remark("邮箱服务器名称是否占用")
	@RequestMapping(value = "/mail/server/name/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getMailServerByName(@RequestBody String name) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.existMailServerByName(name);
		return result;
	}

	/**
	 * 添加邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 下午8:38:25
	 * 
	 * @return
	 */
	@Remark("添加邮箱服务器")
	@RequestMapping(value = "/mail/server/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MailServer> addMailServer(@RequestBody MailServer mailServer) {
		Result<MailServer> result = Result.newOne();
		boolean ok = settingService.saveMailServer(mailServer);
		result.type = ok ? Result.Type.info : Result.Type.warn;
		result.message = ok ? "保存成功！" : "保存失败！";
		result.data = mailServer;
		return result;
	}

	/**
	 * 根据id查看邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月20日 下午6:05:16
	 * 
	 * @param id
	 * @return
	 */
	@Remark("根据id查看邮箱服务器")
	@RequestMapping(value = "/mail/server/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<MailServer> getMailServer(@RequestParam("id") Integer id) {
		Result<MailServer> result = Result.newOne();
		MailServer mailServer = settingService.getMailServerById(id);
		result.data = mailServer;
		return result;
	}

	/**
	 * 修改邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月21日 上午9:49:16
	 * 
	 * @param mailServer
	 * @return
	 */
	@Remark("修改邮箱服务器")
	@RequestMapping(value = "/mail/server/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MailServer> updateMailServer(@RequestBody MailServer mailServer) {
		Result<MailServer> result = Result.newOne();
		if (mailServer.getEnabled()) {
			settingService.updateMailServersAllAsUnabled();
		}
		boolean ok = settingService.updateMailServer(mailServer);
		result.type = ok ? Result.Type.info : Result.Type.warn;
		result.message = ok ? "保存成功！" : "保存失败！";
		result.data = mailServer;
		return result;
	}

	/**
	 * 检验邮箱服务器
	 * 
	 * @author 廖晓远
	 * @date 2015年6月16日 下午4:59:12
	 * 
	 * @param mailServer
	 * @return
	 */
	@Remark("检验邮箱服务器")
	@RequestMapping(value = "/mail/server/test/send", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> testSendMail(@RequestBody MailServer mailServer) {
		Result<Object> result = Result.newOne();
		try {
			JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
			javaMailSender.setProtocol(mailServer.getType().toString());
			javaMailSender.setHost(mailServer.getSendServer());
			if (mailServer.getSendPort() != null) {
				javaMailSender.setPort(mailServer.getSendPort());
			} else {
				javaMailSender.setPort(-1);
			}
			javaMailSender.setUsername(mailServer.getAccount());
			javaMailSender.setPassword(mailServer.getPassword());
			javaMailSender.setDefaultEncoding("UTF-8");

			Properties javaMailProps = new Properties();
			javaMailProps.put("mail.smtp.auth", "true");
			javaMailProps.put("mail.debug", "true");

			javaMailSender.setJavaMailProperties(javaMailProps);
			//
			JavaMailServiceImpl javaMailService = new JavaMailServiceImpl();
			javaMailService.setJavaMailSender(javaMailSender);

			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setFrom(StrUtil.toNamedMailAddress(mailServer.getAccount(), mailServer.getDispName()));
			mailMessage.setTo(StrUtil.toNamedMailAddress(mailServer.getMailAddres(), "测试"));
			mailMessage.setSubject("测试邮件");
			// 获取freeMarker测试邮件
			Template template = freeMarkerService.getTemplate("mail", "mail.test");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("sendTime", DateUtil.toStdDateTimeStr(new Date()));
			String mailText = freeMarkerService.renderContent(template, model);
			mailMessage.setText(mailText);
			javaMailService.sendMail(mailMessage, true, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.type = Type.error;
			return result;
		}
	}

	/**
	 * 启用/禁用 邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年8月7日 上午11:07:09
	 * 
	 * @param mailServer
	 * @return
	 */
	@Remark("启用/禁用 邮箱服务器")
	@RequestMapping(value = { "/mail/server/open/do", "/mail/server/close/do" }, method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> openMailServer(@RequestBody MailServer mailServer) {
		Result<Boolean> result = Result.newOne();
		MailServer mail = settingService.getMailServerById(mailServer.getId());
		mail.setEnabled(mailServer.getEnabled());
		if (mailServer.getEnabled()) {
			// 禁用其他的邮箱服务器
			settingService.updateMailServersAllAsUnabled();
		}
		Boolean flag = settingService.updateMailServer(mail);
		if (mailServer.getEnabled()) {
			result.message = flag ? "启用成功！" : "启用失败！";
		} else {
			result.message = flag ? "禁用成功！" : "禁用失败！";
		}
		return result;
	}

	// -------------------------------物流服务商设置-----------------------------------
	/**
	 * 跳转物流配置页面
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午3:06:27
	 * 
	 * @return String
	 */
	@Remark("物流查询接口配置页面")
	@RequestMapping(value = "/logis/api/jsp", method = RequestMethod.GET)
	public String toLogisticConfig() {
		return "setting/logisticApiConfig";
	}

	/**
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午10:40:46
	 * 
	 * @param request
	 * @return JqGridPage<LogisticApi>
	 */
	@Remark("分页查询物流接口信息")
	@RequestMapping(value = "/logis/api/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<LogisApi> listLogisApi(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<LogisApi> paginatedList = settingService.getLogisApisByFilter(paginatedFilter);
		//
		JqGridPage<LogisApi> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存物流接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 上午10:19:15
	 * 
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("保存物流接口信息")
	@RequestMapping(value = "/logis/api/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> saveLogisApi(@RequestBody LogisApiDto logisticApiDto, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		boolean ok = settingService.saveLogisApi(logisticApiDto);
		result.data = logisticApiDto.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 删除物流接口
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:55:12
	 * 
	 * @param requestData
	 * @return Result<Object>
	 */
	@Remark("删除物流接口信息")
	@RequestMapping(value = "/logis/api/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteLogisApi(@RequestBody MapContext requestData) {
		//
		Result<Object> result = Result.newOne();
		Integer id = requestData.getTypedValue("id", Integer.class);
		//
		boolean ok = settingService.deleteLogisApiById(id);
		result.message = ok ? "删除成功!" : "删除失败!";
		return result;
	}

	/**
	 * 更新物流接口
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:55:53
	 * 
	 * @param logisticApiDto
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("更新物流接口信息")
	@RequestMapping(value = "/logis/api/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateLogisApi(@RequestBody LogisApiDto logisticApiDto, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		boolean ok = settingService.upadteLogisApi(logisticApiDto);
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 验证物流接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午4:12:55
	 * 
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("验证物流接口信息")
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/logis/api/validate/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> validateLogis(@RequestBody MapContext requestData, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		HttpMapResultMappingExecutor mappingExecutor = HttpMapResultMappingExecutor.getInstance();

		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("url", requestData.getTypedValue("url", String.class));
		extra.put("method", requestData.getTypedValue("method", String.class));

		List<Map<String, Object>> ioParams = requestData.getTypedValue("params", TypeUtil.Types.StringObjectMapList.getClass());
		List<ParamMapping> ioParamMappings = CollectionUtil.convertToList(ioParams, apiParamConverter);
		// 请求参数-变量设置
		Map<String, Object> inConext = new HashMap<String, Object>();
		inConext.put("exComCode", requestData.getTypedValue("exComCode", String.class));
		inConext.put("exNo", requestData.getTypedValue("exNo", String.class));
		Map<String, Object> resultMap = null;
		Integer errCode = 0;
		String msg = "查询失败";
		try {
			resultMap = mappingExecutor.execute(ioParamMappings, inConext, extra);
			Object resultCode = resultMap.get("errCode");
			Object message = resultMap.get("message");
			if (resultCode != null) {
				errCode = Integer.valueOf(resultCode.toString());
				if (errCode < 0)
					result.type = Type.warn;
				msg = LogisErrorCode.fromValue(errCode).getText();
			}
			if (message != null) {
				String _message = message.toString();
				if (StrUtil.hasText(_message)) {
					msg = _message;
				}
			}

		} catch (Exception e) {
			result.type = Type.warn;
			e.printStackTrace();
		}
		//
		result.message = msg;
		return result;
	}

	// -------------------------------物流公司设置-----------------------------------

	/**
	 * 跳转至物流公司列表页面
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:07:13
	 * 
	 * @return String
	 */
	@Remark("物流公司列表页面")
	@RequestMapping(value = "/logis/company/jsp", method = RequestMethod.GET)
	public String toLogisticComConfig() {
		return "setting/logisticComList";
	}

	/**
	 * 分业获取物流公司列表
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午6:08:36
	 * 
	 * @param request
	 * @return JqGridPage<LogisticCom>
	 */
	@Remark("分页查询物流公司")
	@RequestMapping(value = "/logis/coms/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<LogisCom> listLogisCom(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<LogisCom> paginatedList = settingService.getLogisComsByFilter(paginatedFilter);
		//
		JqGridPage<LogisCom> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 保存物流公司
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:08:39
	 * 
	 * @param logisCom
	 * @param response
	 * @return Result
	 */
	@Remark("保存物流公司 信息")
	@RequestMapping(value = "/logis/company/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> saveLogisCom(@RequestBody LogisCom logisCom, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		boolean ok = settingService.saveLogisCom(logisCom);
		result.data = logisCom.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 删除物流接口信息
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 下午6:04:03
	 * 
	 * @param requestData
	 * @return Result<Object>
	 */
	@Remark("删除物流接口信息")
	@RequestMapping(value = "/logis/company/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteLogisCom(@RequestBody MapContext requestData) {
		//
		Result<Object> result = Result.newOne();
		Integer id = requestData.getTypedValue("id", Integer.class);
		//
		boolean ok = settingService.deleteLogisComById(id);
		result.message = ok ? "删除成功!" : "删除失败!";
		return result;
	}

	@Remark("更新物流公司")
	@RequestMapping(value = "/logis/company/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updateLogisCom(@RequestBody LogisCom logisCom, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		boolean ok = settingService.upadteLogisCom(logisCom);
		result.data = logisCom.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	// -------------------------------短信设置-----------------------------------
	/**
	 * 跳转短信配置页面
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午2:39:49
	 * 
	 * @return String
	 */
	@Remark("短息接口及参数配置页面")
	@RequestMapping(value = "/sms/jsp", method = RequestMethod.GET)
	public String toSmsConfig() {
		return "setting/smsConfig";
	}

	/**
	 * 保存短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午5:40:38
	 * 
	 * @param response
	 * @return Result<Integer>
	 */
	@Remark("保存短信接口信息")
	@RequestMapping(value = "/sms/api/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> saveSmsApi(@RequestBody SmsApiDto smsApiDto, HttpServletResponse response) {
		//
		Result<Integer> result = Result.newOne();
		//
		boolean ok = settingService.saveSmsApi(smsApiDto);
		result.data = smsApiDto.getId();
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	/**
	 * 分页查询短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午5:35:45
	 * 
	 * @param request
	 * @return JqGridPage<SmsApi>
	 */
	@Remark("分页查询短信接口信息")
	@RequestMapping(value = "/sms/api/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SmsApi> listSmsApi(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SmsApi> paginatedList = settingService.getSmsApisByFilter(paginatedFilter);
		//
		JqGridPage<SmsApi> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// 把DB数据模型转化为通用模型
	Converter<Map<String, Object>, ParamMapping> apiParamConverter = new Converter<Map<String, Object>, ParamMapping>() {

		@Override
		public ParamMapping convert(Map<String, Object> src, int index) {
			ParamMapping paramMapping = ParamMapping.newOne();
			MapContext mapContext = MapContext.from(src);
			// {name=sds, varFlag=0, value=csdc, ioFlag=1, desc=}
			paramMapping.setName(mapContext.getTypedValue("name", String.class));
			paramMapping.setVarFlag(mapContext.getTypedValue("varFlag", Boolean.class));
			paramMapping.setValue(mapContext.getTypedValue("value", String.class));
			paramMapping.setIoFlag(mapContext.getTypedValue("ioFlag", Integer.class));
			paramMapping.setDesc(mapContext.getTypedValue("desc", String.class));
			return paramMapping;
		}
	};

	/**
	 * 验证短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午4:12:55
	 * 
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("验证短信接口信息")
	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/sms/api/validate/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> validateSms(@RequestBody MapContext requestData, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		HttpIntResultMappingExecutor mappingExecutor = HttpIntResultMappingExecutor.getInstance();

		Map<String, Object> extra = new HashMap<String, Object>();
		extra.put("url", requestData.getTypedValue("url", String.class));
		extra.put("method", requestData.getTypedValue("method", String.class));

		List<Map<String, Object>> ioParams = requestData.getTypedValue("params", TypeUtil.Types.StringObjectMapList.getClass());
		List<ParamMapping> ioParamMappings = CollectionUtil.convertToList(ioParams, apiParamConverter);

		Map<String, Object> inConext = new HashMap<String, Object>();
		inConext.put("phoneNo", requestData.getTypedValue("phoneNo", String.class));
		inConext.put("content", requestData.getTypedValue("content", String.class));
		Integer resultCode = -1;
		try {
			resultCode = mappingExecutor.execute(ioParamMappings, inConext, extra);
			if (resultCode != 100) {
				result.type = Type.warn;
			}
		} catch (Exception e) {
			resultCode = -1;
			result.type = Type.warn;
			e.printStackTrace();
		}
		result.message = SmsErrorCode.fromValue(resultCode).getMessage();
		return result;

	}

	/**
	 * 删除短信接口服务商
	 * 
	 * @author guoyn
	 * @date 2015年5月27日 下午3:31:46
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<Object>
	 */
	@Remark("删除短信接口服务商")
	@RequestMapping(value = "/sms/api/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteSmsApi(@RequestBody MapContext requestData, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		Integer id = requestData.getTypedValue("id", Integer.class);
		boolean ok = settingService.deleteSmsApiById(id);
		result.message = ok ? "删除成功!" : "删除失败!";
		return result;
	}

	@Remark("保存短信接口信息")
	@RequestMapping(value = "/sms/api/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> editSmsApi(@RequestBody SmsApiDto smsApiDto, HttpServletResponse response) {
		//
		Result<Integer> result = Result.newOne();
		//
		boolean ok = settingService.updateSmsApi(smsApiDto);
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	// -------------------------------------模板设置----------------------------------------------

	/**
	 * 模板设置页面
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午9:34:07
	 * 
	 * @return
	 */
	@Remark("模板设置")
	@RequestMapping(value = "/template/list/jsp", method = RequestMethod.GET)
	public String templateSetting() {
		return "setting/templateList";
	}

	/**
	 * 分页查询邮箱模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月23日 上午11:23:56
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询邮箱模板")
	@RequestMapping(value = "/mail/templates/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<MailTemplate> getMailTemplates(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<MailTemplate> paginatedList = settingService.getMailTemplates(paginatedFilter);
		JqGridPage<MailTemplate> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 分页查询短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月23日 上午11:25:43
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询短信模板")
	@RequestMapping(value = "/sms/templates/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SmsTemplate> getSmsTemplates(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SmsTemplate> paginatedList = settingService.getSmsTemplates(paginatedFilter);
		JqGridPage<SmsTemplate> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 添加邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:37:31
	 * 
	 * @param mailTemplate
	 * @return
	 */
	@Remark("添加邮件模板")
	@RequestMapping(value = "/mail/template/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MailTemplate> addMailTemplate(@RequestBody MailTemplate mailTemplate) {
		Result<MailTemplate> result = Result.newOne();
		result.message = settingService.saveMailTemplate(mailTemplate) ? "保存成功！" : "保存失败！";
		result.data = mailTemplate;
		return result;
	}

	/**
	 * 修改邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月23日 上午11:21:44
	 * 
	 * @param mailTemplate
	 * @return
	 */
	@Remark("修改邮件模板")
	@RequestMapping(value = "/mail/template/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<MailTemplate> updateMailTemplate(@RequestBody MailTemplate mailTemplate) {
		Result<MailTemplate> result = Result.newOne();
		result.message = settingService.updateMailTemplate(mailTemplate) ? "保存成功！" : "保存失败！";
		result.data = mailTemplate;
		return result;
	}

	/**
	 * 删除邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:37:41
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除邮件模板")
	@RequestMapping(value = "/mail/template/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteMailTemplate(@RequestParam("id") Integer id) {
		Result<Object> result = Result.newOne();
		result.message = settingService.deleteMailTemplateById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 添加短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午2:46:24
	 * 
	 * @param smsTemplate
	 * @return
	 */
	@Remark("添加短信模板")
	@RequestMapping(value = "/sms/template/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SmsTemplate> addSmsTemplate(@RequestBody SmsTemplate smsTemplate) {
		Result<SmsTemplate> result = Result.newOne();
		result.message = settingService.saveSmsTemplate(smsTemplate) ? "保存成功！" : "保存失败！";
		result.data = smsTemplate;
		return result;
	}

	/**
	 * 修改短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月23日 上午11:17:37
	 * 
	 * @param smsTemplate
	 * @return
	 */
	@Remark("修改短信模板")
	@RequestMapping(value = "/sms/template/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SmsTemplate> updateSmsTemplate(@RequestBody SmsTemplate smsTemplate) {
		Result<SmsTemplate> result = Result.newOne();
		result.message = settingService.updateSmsTemplate(smsTemplate) ? "保存成功！" : "保存失败！";
		result.data = smsTemplate;
		return result;
	}

	/**
	 * 删除短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午2:46:32
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除短信模板")
	@RequestMapping(value = "/sms/template/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteSmsTemplate(@RequestParam("id") Integer id) {
		Result<Object> result = Result.newOne();
		result.message = settingService.deleteSmsTemplateById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 邮件模板名称是否可用
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午3:29:58
	 * 
	 * @param name
	 * @return
	 */
	@Remark("邮件模板名称是否占用")
	@RequestMapping(value = "/mail/template/name/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getMailTempByName(@RequestBody String name) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.getMailTemplateByName(name) == null;
		return result;
	}

	/**
	 * 短信模板名称是否可用
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午3:30:01
	 * 
	 * @param name
	 * @return
	 */
	@Remark("短信模板名称是否占用")
	@RequestMapping(value = "/sms/template/name/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getSmsTempByName(@RequestBody String name) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.getSmsTemplateByName(name) == null;
		return result;
	}

	/**
	 * 邮件模板code是否占用
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 下午2:14:21
	 * 
	 * @param code
	 * @return
	 */
	@Remark("邮件模板code是否占用")
	@RequestMapping(value = "/mail/template/code/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getMailTempByCode(@RequestBody String code) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.getMailTemplateByCode(code) == null;
		return result;
	}

	/**
	 * 短信模板code是否占用
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 下午2:15:33
	 * 
	 * @param code
	 * @return
	 */
	@Remark("短信模板code是否占用")
	@RequestMapping(value = "/sms/template/code/isabel/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getSmsTempByCode(@RequestBody String code) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.getSmsTemplateByCode(code) == null;
		return result;
	}

	/**
	 * 获取所有的模板模型
	 * 
	 * @author 毛智东
	 * @date 2015年6月18日 下午3:54:35
	 * 
	 * @return
	 */
	@Remark("获取所有的模板模型")
	@RequestMapping(value = "/tpl/models/get/by/code", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getTplModelsByCode(@RequestParam("code") String code) {
		Result<SelectList> result = Result.newOne();
		List<TplModel> tplModels = settingService.getTplModelsByCode(code);
		SelectList selectList = SelectList.newOne();
		if (tplModels.size() > 0) {
			selectList.setUnSelectedItem("", "-请选择-");
			for (TplModel tplModel : tplModels) {
				selectList.addItem(tplModel.getCode(), tplModel.getName());
			}
		}
		result.data = selectList;
		return result;
	}

	/**
	 * 根据code获取模板模型
	 * 
	 * @author 毛智东
	 * @date 2015年6月18日 下午5:55:20
	 * 
	 * @param code
	 * @return
	 */
	@Remark("根据code获取模板模型")
	@RequestMapping(value = "/tpl/model/get/by/code", method = RequestMethod.POST)
	@ResponseBody
	public Result<TplModel> getTplModelByCode(@RequestParam("code") String code) {
		Result<TplModel> result = Result.newOne();
		TplModel tplModel = settingService.getTplModelByCode(code);
		result.data = tplModel;
		return result;
	}

	/**
	 * 模板预览
	 * 
	 * @author 毛智东
	 * @date 2015年6月23日 下午6:08:42
	 * 
	 * @param preview
	 * @return
	 */
	@Remark("模板预览")
	@RequestMapping(value = "/template/preview/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> previewTemplate(@RequestBody TemplatePreview preview) {
		Result<String> result = Result.newOne();
		StringFreeMarkerServiceImpl stringImpl = new StringFreeMarkerServiceImpl();
		FreeMarkerService freeMarkerService = stringImpl;
		// 这里使用了默认的
		stringImpl.setTemplateContent("preview", preview.getContent());
		Map<String, Object> sampleData = JsonUtil.fromJson(preview.getSample(), TypeUtil.TypeRefs.StringObjectMapType);
		// 生成结果
		String resultContent = freeMarkerService.renderContent("preview", sampleData);
		result.data = resultContent;
		return result;
	}

	// ------------------------------------------配送方式设置---------------------------------------------------

	/**
	 * 配送方式设置页面
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:52:50
	 * 
	 * @return
	 */
	@Remark("配送方式设置页面")
	@RequestMapping(value = "/deliveryWay/jsp", method = RequestMethod.GET)
	public String goDeliveryWaySetting() {
		return "setting/deliveryWay";
	}

	/**
	 * 添加配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:55:50
	 * 
	 * @param deliveryWay
	 * @return
	 */
	@Remark("添加配送方式")
	@RequestMapping(value = "/deliveryWay/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<DeliveryWay> addDeliveryWay(@RequestBody DeliveryWay deliveryWay) {
		Result<DeliveryWay> result = Result.newOne();
		result.message = settingService.saveDeliveryWay(deliveryWay) ? "保存成功！" : "保存失败！";
		result.data = deliveryWay;
		return result;
	}

	/**
	 * 删除配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:01:30
	 * 
	 * @param id
	 * @return
	 */
	@Remark("删除配送方式")
	@RequestMapping(value = "/deliveryWay/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteDeliveryWay(@RequestParam("id") Integer id) {
		Result<Object> result = Result.newOne();
		result.message = settingService.deleteDeliveryWay(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	/**
	 * 修改配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:04:43
	 * 
	 * @param deliveryWay
	 * @return
	 */
	@Remark("修改配送方式")
	@RequestMapping(value = "/deliveryWay/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<DeliveryWay> updateDeliveryWay(@RequestBody DeliveryWay deliveryWay) {
		Result<DeliveryWay> result = Result.newOne();
		result.message = settingService.updateDeliveryWay(deliveryWay) ? "保存成功！" : "保存失败！";
		result.data = deliveryWay;
		return result;
	}

	/**
	 * 分页查询配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:06:53
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询配送方式")
	@RequestMapping(value = "/deliveryWay/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DeliveryWay> getDeliveryWays(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<DeliveryWay> paginatedList = settingService.getDeliveryWays(paginatedFilter);
		JqGridPage<DeliveryWay> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 封装物流公司select组件
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:56:40
	 * 
	 * @return
	 */
	@Remark("封装物流公司select组件")
	@RequestMapping(value = "/logis/company/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getLogisComs() {
		Result<SelectList> result = Result.newOne();
		// 装载地区数据
		SelectList selectList = SelectList.newOne();
		List<LogisCom> list = settingService.getLogisComListAll();
		if (list.size() > 0) {
			selectList.setUnSelectedItem("", "- 请选择 -");
		}
		for (LogisCom logisCom : list) {
			selectList.addItem(logisCom.getId().toString(), logisCom.getName());
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}

	/**
	 * 根据名称和公司id查找配送方式是否存在
	 * 
	 * @author 毛智东
	 * @date 2015年5月30日 上午11:20:38
	 * 
	 * @return
	 */
	@Remark("根据名称和公司id查找配送方式是否存在")
	@RequestMapping(value = "/deliveryWay/by/name/and/comId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getDeliveryWayByNameAndComId(@RequestBody DeliveryWay deliveryWay) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.getDeliveryWayByName(deliveryWay.getName(), deliveryWay.getComId()) == null;
		return result;
	}

	/**
	 * 分组查询物流公司及其分配方式
	 * 
	 * @author 毛智东
	 * @date 2015年6月6日 下午3:57:47
	 * 
	 * @return
	 */
	@Remark("分组查询物流公司及其分配方式")
	@RequestMapping(value = "/logis/company/group/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<LogisCom>> getLogisComsByGroup() {
		Result<List<LogisCom>> result = Result.newOne();
		List<LogisCom> logisComs = settingService.getLogisComsByGroup(false, false);
		result.data = logisComs;
		return result;
	}

	// -------------------------------------------店铺运费设置--------------------------------------------------------

	@Remark("店铺运费设置")
	@RequestMapping(value = "/freight/set/jsp", method = RequestMethod.GET)
	public String toFreightSetJsp() {
		return "setting/freightSet";
	}

	// ------------------------------------------短信验证码-----------------------------------------------------

	/**
	 * 短信验证码验证
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 下午7:33:05
	 * 
	 * @param smsVerfCode
	 *            页面参数：ajax.data({phoneNo : "13311111111", vfCode : "123456", usage : "regist"});
	 * @return
	 */
	@Remark("短信验证码验证")
	@RequestMapping(value = "/sms/verf/code/valid", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> validSmsVerfCode(@RequestBody SmsVerfCode smsVerfCode) {
		Result<Boolean> result = Result.newOne();
		result.data = settingService.validSmsVerfCode(smsVerfCode);
		return result;
	}

	// -------------------------------销售楼层-----------------------------------

	/**
	 * 分页查询销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月14日 下午6:27:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询销售楼层")
	@RequestMapping(value = "/salesFloor/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SalesFloor> getSalesFloors(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SalesFloor> paginatedList = settingService.getSalesFloors(paginatedFilter);
		JqGridPage<SalesFloor> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 新增销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 下午12:03:44
	 * 
	 * @param salesFloor
	 * @return
	 */
	@Remark("新增销售楼层")
	@RequestMapping(value = "/salesFloor/create/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SalesFloor> createSalesFloor(@RequestBody SalesFloor salesFloor) {
		Result<SalesFloor> result = Result.newOne();
		boolean flag = true;
		try {
			flag = settingService.saveSalesFloor(salesFloor);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.data = salesFloor;
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 修改销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 下午5:26:24
	 * 
	 * @param salesFloor
	 * @return
	 */
	@Remark("修改销售楼层")
	@RequestMapping(value = "/salesFloor/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SalesFloor> updateSalesFloor(@RequestBody SalesFloor salesFloor) {
		Result<SalesFloor> result = Result.newOne();
		boolean flag = true;
		try {
			flag = settingService.updateSalesFloor(salesFloor);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.data = salesFloor;
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 根据Id删除销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:27:06
	 * 
	 * @param salesFloor
	 * @return
	 */
	@Remark("根据Id删除销售楼层")
	@RequestMapping(value = "/salesFloor/delete/by/no", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> delSalesFloorById(@RequestBody SalesFloor salesFloor) {
		Result<Object> result = Result.newOne();
		boolean flag = true;
		try {
			flag = settingService.deleteSalesFloorByNo(salesFloor.getNo());
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			result.type = Result.Type.error;
		}
		result.message = flag ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 根据Ids删除销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:26:57
	 * 
	 * @param nos
	 * @return
	 */
	@Remark("根据Ids删除销售楼层")
	@RequestMapping(value = "/salesFloor/delete/by/nos/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> deleteSalesFloorByIds(@RequestBody List<Integer> nos) {
		Result<Object> result = Result.newOne();
		boolean ok = true;
		try {
			ok = settingService.deleteSalesFloorByNos(nos);

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;

	}

	// TODO-------------------begin-----------------------支付方式设置-------------------WJJ--------------------------------

	/**
	 * @Title:goPayWaySetting
	 * @Description:支付方式设置页面
	 * @Params:@return
	 * @Return:String
	 * @author:WJJ
	 * @Date:2015年9月6日下午2:52:37
	 */
	@Remark("支付方式设置页面")
	@RequestMapping(value = "/payWay/jsp", method = RequestMethod.GET)
	public String goPayWaySetting() {
		return "setting/payWay";
	}

	/**
	 * @Title:listPayWay
	 * @Description:分页查询支付接口信息
	 * @Params:@param request
	 * @Params:@return
	 * @Return:JqGridPage<PayWay>
	 * @author:WJJ
	 * @Date:2015年9月7日下午7:10:21
	 */
	@Remark("分页查询支付接口信息")
	@RequestMapping(value = "/pay/api/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayWay> listPayWay(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayWay> paginatedList = settingService.getPayWayByFilter(paginatedFilter);
		//
		JqGridPage<PayWay> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/*
	 * @Remark("保存支付接口信息")
	 * 
	 * @RequestMapping(value = "/pay/api/add/do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public Result<Object> savePayWay(@RequestBody PayWayDto payWayDto, HttpServletResponse response) { // Result<Object> result = Result.newOne(); // boolean ok =
	 * settingService.savePayWay(payWayDto); result.data = payWayDto.getId(); result.message = ok ? "保存成功!" : "保存失败!"; return result; }
	 */

	/*
	 * @Remark("删除支付接口信息")
	 * 
	 * @RequestMapping(value = "/pay/api/delete/do", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public Result<Object> deletePayWay(@RequestBody MapContext requestData) { // Result<Object> result = Result.newOne(); Integer id = requestData.getTypedValue("id", Integer.class);
	 * // boolean ok = settingService.deletePayWayById(id); result.message = ok ? "删除成功!" : "删除失败!"; return result; }
	 */

	/**
	 * @Title:updatePayWay
	 * @Description:更新支付接口信息
	 * @Params:@param payWay
	 * @Params:@param response
	 * @Params:@return
	 * @Return:Result<Object>
	 * @author:WJJ
	 * @Date:2015年9月7日下午7:11:01
	 */
	@Remark("更新支付接口信息")
	@RequestMapping(value = "/pay/api/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> updatePayWay(@RequestBody PayWay payWay, HttpServletResponse response) {
		//
		Result<Object> result = Result.newOne();
		//
		boolean ok = settingService.updatePayWay(payWay);
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	// -------------------end-----------------------支付方式设置-------------------WJJ--------------------------------
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

	/**
	 * 获取处于启用状态的支付方式-下拉框
	 * 
	 * @author 邓华锋
	 * @date 2016年2月29日 下午2:47:57
	 * 
	 * @return
	 */
	@Remark("获取处于启用状态的支付方式-下拉框")
	@RequestMapping(value = "/payWay/usbale/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getFaqCatList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<PayWay> list = settingService.getUsablePayWay();
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (PayWay payWay : list) {
				selectList.addItem(payWay.getCode(), payWay.getName());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}
}
