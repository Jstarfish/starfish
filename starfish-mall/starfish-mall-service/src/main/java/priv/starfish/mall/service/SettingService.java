package priv.starfish.mall.service;

import java.io.InputStream;
import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.TreeNode;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.BizParam;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.SysParam;
import priv.starfish.mall.logistic.dto.LogisApiDto;
import priv.starfish.mall.logistic.entity.DeliveryWay;
import priv.starfish.mall.logistic.entity.LogisApi;
import priv.starfish.mall.logistic.entity.LogisApiParam;
import priv.starfish.mall.logistic.entity.LogisCom;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.dto.SmsApiDto;
import priv.starfish.mall.notify.entity.MailServer;
import priv.starfish.mall.notify.entity.MailTemplate;
import priv.starfish.mall.notify.entity.MailVerfCode;
import priv.starfish.mall.notify.entity.SmsApi;
import priv.starfish.mall.notify.entity.SmsApiParam;
import priv.starfish.mall.notify.entity.SmsTemplate;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.notify.entity.TplModel;
import priv.starfish.mall.pay.entity.PayWay;

public interface SettingService extends BaseService {

	// ----------------------------------地区------------------------------------
	/**
	 * 根据地区id查询地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月25日 下午6:47:35
	 * 
	 * @param id
	 *            地区id
	 * @return 返回地区信息
	 */
	Region getRegionById(Integer id);

	/**
	 * 根据地区code查询地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月25日 下午6:47:35
	 * 
	 * @param code
	 *            地区code
	 * @return 返回地区信息
	 */
	Region getRegionByCode(String code);

	/**
	 * 根据地区父id和名称获取地区信息
	 * 
	 * @author koqiui
	 * @date 2016年1月4日 下午6:57:15
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	Region getRegionByParentIdAndName(Integer parentId, String name);

	/**
	 * 根据所属百度城市代码和名称获取对应的县级地区
	 * 
	 * @author koqiui
	 * @date 2016年1月4日 下午8:20:04
	 * 
	 * @param bdCityCode
	 *            所属百度城市代码
	 * @param countyName
	 *            县名
	 * @return
	 */
	Region getCountyByBdCityCodeAndName(Integer bdCityCode, String countyName);
	
	/**
	 * 返回地区的部分信息
	 * 
	 * @author koqiui
	 * @date 2015年11月3日 上午11:57:59
	 * 
	 * @param id
	 * @return
	 */
	RegionParts getRegionPartsById(Integer id);

	/**
	 * 根据地区父id和级别查询下一级地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 下午7:04:16
	 * 
	 * @param level
	 *            地区父id
	 * @param parentId
	 *            地区级别
	 * @return 返回下一级地区
	 */
	List<Region> getRegionsByParentId(Integer parentId);

	/**
	 * 获取地区树结构
	 * 
	 * @author 王少辉
	 * @date 2015年5月23日 下午5:38:15
	 * @param region
	 *            地区
	 * @return 返回地区树结构
	 */
	List<TreeNode> getRegionsTree(Region region);

	/**
	 * 新增地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月25日 下午5:44:05
	 * 
	 * @param region
	 *            地区
	 * @return 返回新增结果
	 */
	boolean saveRegion(Region region);

	/**
	 * 更新地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月25日 下午5:44:05
	 * 
	 * @param region
	 *            地区
	 * @return 返回更新结果
	 */
	boolean updateRegion(Region region);

	/**
	 * 删除地区
	 * 
	 * @author 王少辉
	 * @date 2015年5月25日 下午5:44:52
	 * 
	 * @param id
	 *            地区id
	 * @return 返回删除结果
	 */
	boolean deleteRegion(Integer id);

	/**
	 * 批量删除地区
	 * 
	 * @author 毛智东
	 * @date 2015年5月25日 下午9:08:22
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteRegionsByIds(List<Integer> ids);

	/**
	 * 从xls文件中导入地区信息
	 * 
	 * @author koqiui
	 * @date 2015年7月24日 下午8:37:37
	 * 
	 * @param xlsInputStream
	 * @throws Exception
	 */
	void importRegionsFromXls(InputStream xlsInputStream) throws Exception;

	// -------------------------------系统业务参数-----------------------------------
	/**
	 * 根据code获取系统参数
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午4:12:29
	 * 
	 * @param code
	 * @return 返回系统参数
	 */
	SysParam getSysParamByCode(String code);

	/**
	 * 获取所有系统参数
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午4:14:57
	 * 
	 * @return 返回系统参数列表
	 */
	List<SysParam> getAllSysParams();

	/**
	 * 添加系统参数
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午3:10:33
	 * 
	 * @param sysParam
	 * @return 返回更新结果
	 */
	boolean saveSysParam(SysParam sysParam);

	/**
	 * 批量添加系统参数
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午3:15:33
	 * 
	 * @param sysParamList
	 * @return 返回更新结果
	 */
	boolean saveSysParams(List<SysParam> sysParamList);

	/**
	 * 更新系统参数
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午4:15:33
	 * 
	 * @param sysParam
	 * @return 返回更新结果
	 */
	boolean updateSysParam(SysParam sysParam);

	/**
	 * 批量更新系统参数
	 * 
	 * @author 王少辉
	 * @date 2015年7月29日 下午4:15:33
	 * 
	 * @param sysParamList
	 * @return 返回更新结果
	 */
	boolean updateSysParams(List<SysParam> sysParamList);

	// -------------------------------商城业务参数-----------------------------------
	/**
	 * 根据主键获取业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:12:43
	 * 
	 * @param code
	 *            主键
	 * @return 返回商城参数
	 */
	BizParam getBizParamByCode(String code);

	/**
	 * 根据名称获取业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:12:48
	 * 
	 * @param name
	 *            业务参数名称
	 * @return 返回的业务参数
	 */
	BizParam getBizParamByName(String name);

	/**
	 * 获取参数列表
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:12:56
	 * 
	 * @return 返回参数列表
	 */
	List<BizParam> getBizParams();

	/**
	 * 新增一条业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:13:00
	 * 
	 * @param bizParam
	 *            要新增的业务参数
	 */
	boolean saveBizParam(BizParam bizParam);

	/**
	 * 更新业务参数列表
	 * 
	 * @author koqiui
	 * @date 2015年10月13日 下午5:57:33
	 * 
	 * @param bizParamList
	 * @return
	 */
	boolean saveBizParams(List<BizParam> bizParamList);

	/**
	 * 更新一条业务参数
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:13:04
	 * 
	 * @param bizParam
	 *            要更新的业务参数
	 */
	boolean updateBizParam(BizParam bizParam);

	/**
	 * 更新业务参数列表
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:13:18
	 * 
	 * @param bizParamList
	 *            要更新的业务参数列表
	 */
	boolean updateBizParams(List<BizParam> bizParamList);

	// -------------------------------邮箱服务器---------------------------------------------

	/**
	 * 分页查询邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月18日 下午8:32:25
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<MailServer> getMailServers(PaginatedFilter paginatedFilter);

	/**
	 * 根据id删除邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 上午10:10:40
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteMailServerById(Integer id);

	/**
	 * 检查给定名称的邮箱服务器是否存在
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 下午6:14:12
	 * 
	 * @param name
	 * @return
	 */
	boolean existMailServerByName(String name);

	/**
	 * 添加邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月19日 下午6:14:18
	 * 
	 * @param mailServer
	 * @return
	 */
	boolean saveMailServer(MailServer mailServer);

	/**
	 * 把所有的邮箱服务器设为禁用
	 * 
	 * @author 毛智东
	 * @date 2015年5月20日 下午4:04:32
	 * 
	 * @return
	 */
	void updateMailServersAllAsUnabled();

	/**
	 * 根据id查找邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月20日 下午6:00:20
	 * 
	 * @param id
	 * @return
	 */
	MailServer getMailServerById(Integer id);

	/**
	 * 修改邮箱服务器
	 * 
	 * @author 毛智东
	 * @date 2015年5月21日 上午9:47:32
	 * 
	 * @param mailServer
	 * @return
	 */
	boolean updateMailServer(MailServer mailServer);

	// -------------------------------物流设置-----------------------------------

	/**
	 * 分页获取物流公司信息
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午4:35:08
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<LogisticCom>
	 */
	PaginatedList<LogisCom> getLogisComsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 保存物流公司
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:12:06
	 * 
	 * @param logisCom
	 * @return boolean
	 */
	boolean saveLogisCom(LogisCom logisCom);

	/**
	 * 删除物流公司
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 上午10:57:38
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteLogisComById(Integer id);

	/**
	 * 更新物流公司
	 * 
	 * @author guoyn
	 * @date 2015年8月6日 下午6:06:23
	 * 
	 * @param logisCom
	 * @return boolean
	 */
	boolean upadteLogisCom(LogisCom logisCom);

	/**
	 * 分页获取物流接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午10:58:41
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<LogisticApi>
	 */
	PaginatedList<LogisApi> getLogisApisByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 保存LogisApi信息
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 上午11:51:52
	 * 
	 * @param logisticApiDto
	 * @return boolean
	 */
	boolean saveLogisApi(LogisApiDto logisticApiDto);

	/**
	 * 根据id删除物流查询api
	 * 
	 * @author guoyn
	 * @date 2015年7月31日 上午9:50:41
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteLogisApiById(Integer id);

	/**
	 * 更新物流查询服务商
	 * 
	 * @author guoyn
	 * @date 2015年8月4日 下午3:55:28
	 * 
	 * @param logisticApiDto
	 * @return boolean
	 */
	boolean upadteLogisApi(LogisApiDto logisticApiDto);

	/**
	 * 根据id获取物流接口
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午2:44:29
	 * 
	 * @param logisApiId
	 * @return LogisApi
	 */
	LogisApi getLogisApiById(Integer logisApiId);

	/**
	 * 根据ApiId获取LogisApiParam集合
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午3:14:22
	 * 
	 * @param logisApiId
	 * @return List<LogisApiParam>
	 */
	List<LogisApiParam> getLogisApiParamsByApiId(Integer logisApiId);

	/**
	 * 得到所有的物流公司
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午5:46:12
	 * 
	 * @return
	 */
	List<LogisCom> getLogisComListAll();

	/**
	 * 分组查询物流公司及其配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年6月6日 下午3:53:29
	 * 
	 * @param lcDisabled
	 * @param dwDisabled
	 * @return
	 */
	List<LogisCom> getLogisComsByGroup(Boolean lcDisabled, Boolean dwDisabled);

	// -------------------------------------模板设置--------------------------------------------

	/**
	 * 分页查询邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:08:42
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<MailTemplate> getMailTemplates(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:08:58
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SmsTemplate> getSmsTemplates(PaginatedFilter paginatedFilter);

	/**
	 * 添加邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:11:36
	 * 
	 * @param mailTemplate
	 * @return
	 */
	boolean saveMailTemplate(MailTemplate mailTemplate);

	/**
	 * 修改邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:11:51
	 * 
	 * @param mailTemplate
	 * @return
	 */
	boolean updateMailTemplate(MailTemplate mailTemplate);

	/**
	 * 根据id删除邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:12:33
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteMailTemplateById(Integer id);

	/**
	 * 添加短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:12:37
	 * 
	 * @param smsTemplate
	 * @return
	 */
	boolean saveSmsTemplate(SmsTemplate smsTemplate);

	/**
	 * 修改短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:13:34
	 * 
	 * @param smsTemplate
	 * @return
	 */
	boolean updateSmsTemplate(SmsTemplate smsTemplate);

	/**
	 * 根据id删除短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 上午11:13:43
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteSmsTemplateById(Integer id);

	/**
	 * 根据模板名字查找邮件模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午3:09:09
	 * 
	 * @param name
	 * @return
	 */
	MailTemplate getMailTemplateByName(String name);

	/**
	 * 根据模板名字查找短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年5月22日 下午3:09:12
	 * 
	 * @param name
	 * @return
	 */
	SmsTemplate getSmsTemplateByName(String name);

	/**
	 * 根据code查找邮箱模板
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 下午1:56:45
	 * 
	 * @param code
	 * @return
	 */
	MailTemplate getMailTemplateByCode(String code);

	/**
	 * 根据code查找短信模板
	 * 
	 * @author 毛智东
	 * @date 2015年6月16日 下午1:56:54
	 * 
	 * @param code
	 * @return
	 */
	SmsTemplate getSmsTemplateByCode(String code);

	/**
	 * 获取所有的模板模型
	 * 
	 * @author 毛智东
	 * @param code
	 * @date 2015年6月18日 下午3:48:19
	 * 
	 * @return
	 */
	List<TplModel> getTplModelsByCode(String code);

	/**
	 * 根据code获得模板模型
	 * 
	 * @author 毛智东
	 * @date 2015年6月18日 下午5:49:48
	 * 
	 * @param code
	 * @return
	 */
	TplModel getTplModelByCode(String code);

	// -------------------------------------短信设置--------------------------------------------

	/**
	 * 保存短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月22日 下午5:45:28
	 * 
	 * @param smsApiDto
	 * @return boolean
	 */
	boolean saveSmsApi(SmsApiDto smsApiDto);

	/**
	 * 分页获取短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月23日 上午10:54:59
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<SmsApi>
	 */
	PaginatedList<SmsApi> getSmsApisByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据id获取短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月23日 上午11:34:49
	 * 
	 * @param id
	 * @return SmsApi
	 */
	SmsApi getSmsApiById(Integer id);

	/**
	 * 根据短信接口id获取短信接口参数列表
	 * 
	 * @author guoyn
	 * @date 2015年5月23日 上午11:43:54
	 * 
	 * @param id
	 * @return List<SmsApi>
	 */
	List<SmsApiParam> getSmsApiParamsByApiId(Integer apiId);

	/**
	 * 删除短信接口服务商
	 * 
	 * @author guoyn
	 * @date 2015年5月27日 下午3:32:34
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteSmsApiById(Integer id);

	/**
	 * 修改短信接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月27日 下午4:08:14
	 * 
	 * @param smsApiDto
	 * @return boolean
	 */
	boolean updateSmsApi(SmsApiDto smsApiDto);

	/**
	 * 获取所有短信接口服务商
	 * 
	 * @author guoyn
	 * @date 2015年5月27日 下午7:35:59
	 * 
	 * @return List<SmsApi>
	 */
	List<SmsApi> getSmsApis();

	// -------------------------------------配送方式设置--------------------------------------------

	/**
	 * 添加配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:18:57
	 * 
	 * @param deliveryWay
	 * @return
	 */
	boolean saveDeliveryWay(DeliveryWay deliveryWay);

	/**
	 * 删除配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:19:01
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteDeliveryWay(Integer id);

	/**
	 * 修改配送方式
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:19:04
	 * 
	 * @param deliveryWay
	 * @return
	 */
	boolean updateDeliveryWay(DeliveryWay deliveryWay);

	/**
	 * 分页查询配送方式列表
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 下午4:19:08
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<DeliveryWay> getDeliveryWays(PaginatedFilter paginatedFilter);

	/**
	 * 根据名称和公司id判断对象是否存在
	 * 
	 * @author 毛智东
	 * @date 2015年6月12日 下午6:01:43
	 * 
	 * @param name
	 * @param comId
	 * @return
	 */
	DeliveryWay getDeliveryWayByName(String name, Integer comId);

	// -------------------------------------------------短信验证码--------------------------------------------------------------

	/**
	 * 保存短信验证码
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 上午11:36:27
	 * 
	 * @param smsVerfCode
	 * @return
	 */
	boolean saveSmsVerfCode(SmsVerfCode smsVerfCode);
	
	boolean updateSmsVerfCode(SmsVerfCode smsVerfCode);

	/**
	 * 验证短信验证码是否正确
	 * 
	 * @author 毛智东
	 * @date 2015年7月3日 下午4:52:51
	 * 
	 * @param smsVerfCode
	 * @return
	 */
	boolean validSmsVerfCode(SmsVerfCode smsVerfCode);

	/**
	 * 获取已发送短信验证码
	 * 
	 * @author 郝江奎
	 * @date 2015年11月14日 下午4:00:51
	 * 
	 * @param smsVerfCode
	 * @return
	 */
	List<SmsVerfCode> getSmsVerfCodes(String reqIp, String sendTime, SmsUsage[] limitUsages);

	// -------------------------------------------------邮箱验证码---------------------------------------------------------------

	/**
	 * 保存邮箱验证码
	 * 
	 * @author 毛智东
	 * @date 2015年7月8日 下午7:43:20
	 * 
	 * @param mailVerfCode
	 * @return
	 */
	boolean saveMailVerfCode(MailVerfCode mailVerfCode);

	/**
	 * 验证邮箱验证码是否正确
	 * 
	 * @author 毛智东
	 * @date 2015年7月8日 下午7:43:23
	 * 
	 * @param mailVerfCode
	 * @return
	 */
	boolean validMailVerfCode(MailVerfCode mailVerfCode);

	// -------------------------------销售楼层-----------------------------------

	/**
	 * 分页查询销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月14日 下午6:28:17
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesFloor> getSalesFloors(PaginatedFilter paginatedFilter);

	/**
	 * 新建楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月15日 下午12:01:18
	 * 
	 * @param salesFloor
	 * @return
	 */
	boolean saveSalesFloor(SalesFloor salesFloor);

	/**
	 * @author 廖晓远
	 * @date 2015年7月18日 下午5:26:58
	 * 
	 * @param salesFloor
	 * @return
	 */
	boolean updateSalesFloor(SalesFloor salesFloor);

	/**
	 * 根据no删除销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:30:56
	 * 
	 * @param no
	 * @return
	 */
	boolean deleteSalesFloorByNo(Integer no);

	/**
	 * 根据nos批量删除销售楼层
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:31:28
	 * 
	 * @param nos
	 * @return
	 */
	boolean deleteSalesFloorByNos(List<Integer> nos);

	// -----begin-------WJJ-------------------支付设置-----------------------------------

	/**
	 * @Title:getPayWayByFilter
	 * @Description:分页查询支付接口信息
	 * @Params:@param paginatedFilter
	 * @Params:@return
	 * @Return:PaginatedList<PayWay>
	 * @author:WJJ
	 * @Date:2015年9月6日下午3:11:33
	 */
	PaginatedList<PayWay> getPayWayByFilter(PaginatedFilter paginatedFilter);

	boolean savePayWay(PayWay payWay);

	boolean deletePayWayById(Integer id);

	/**
	 * @Title:updatePayWay
	 * @Description:更新支付方式（参数值）
	 * @Params:@param payWay
	 * @Params:@return
	 * @Return:boolean
	 * @author:WJJ
	 * @Date:2015年9月7日下午7:11:48
	 */
	boolean updatePayWay(PayWay payWay);
	// -----end-------WJJ-------------------支付设置-----------------------------------

	/**
	 * 查询处于启用状态的支付方式
	 * 
	 * @author "WJJ"
	 * @date 2015年11月20日 下午5:25:51
	 * 
	 * @return
	 */
	List<PayWay> getUsablePayWay();

}
