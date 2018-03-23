package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.Agreement;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.mall.entity.Mall;
import priv.starfish.mall.mall.entity.MallNotice;
import priv.starfish.mall.mall.entity.Operator;

public interface MallService extends BaseService {

	/**
	 * 根据商城id商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:51:44
	 * 
	 * @param id
	 *            商城主键
	 * 
	 * @return 返回商城基本信息
	 */
	Mall getMallById(Integer id);

	/**
	 * 根据商城code获取商城基本信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:51:48
	 * 
	 * @param code
	 *            商城code
	 * @return 返回商城基本信息
	 */
	Mall getMallByCode(String code);

	/**
	 * 获取唯一的商城
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午5:55:58
	 * 
	 * @return
	 */
	Mall getMall();

	/**
	 * 获取唯一的运营商
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午7:09:24
	 * 
	 * @return
	 */
	Operator getOperator();

	/**
	 * 
	 * 获取唯一的商城信息
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午6:36:54
	 * 
	 * @return
	 */
	MallDto getMallInfo();

	/**
	 * 获取商户协议
	 * 
	 * @author 王少辉
	 * @date 2015年8月18日 下午1:33:46
	 * 
	 * @return 返回商城协议
	 */
	Agreement getMerchAgreement();

	/**
	 * 获取代理协议
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:03:46
	 * 
	 * @return 返回代理协议
	 */
	Agreement getAgentAgreement();

	/**
	 * 获取供应商协议
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午2:20:10
	 * 
	 * @return 返回供应商协议
	 */
	Agreement getVendorAgreement();

	/**
	 * 获取会员协议
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午11:33:46
	 * 
	 * @return 返回会员协议
	 */
	Agreement getMemberAgreement();

	/**
	 * 根据商城公告id获取商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午10:01:27
	 * 
	 * @param noticeId
	 *            商城公告主键
	 * @return 返回商城公告
	 */
	MallNotice getMallNoticeById(Integer noticeId);

	/**
	 * 获取商城公告列表
	 * 
	 * @author 王少辉
	 * @date 2015年8月21日 上午10:31:19
	 * 
	 * @param paginatedFilter
	 * @return 返回商城公告列表
	 */
	PaginatedList<MallNotice> getMallNoticesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 注册商城信息（包括用户、运营商、商城信息）
	 * 
	 * @author 王少辉
	 * @date 2015年7月21日 上午11:25:03
	 * 
	 * @param mall
	 *            要注册的商城信息
	 * @return
	 */
	boolean createOrUpdateMall(MallDto mallDto);

	/**
	 * 新增商城协议
	 * 
	 * @author 王少辉
	 * @date 2015年8月18日 下午1:46:29
	 * 
	 * @param agreement
	 *            商城协议
	 * @return 返回新增结果
	 */
	boolean saveMerchAgreement(Agreement mallAgreement);

	/**
	 * 新增会员协议
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午13:59:29
	 * 
	 * @param memberAgreement
	 *            商城协议
	 * @return 返回新增结果
	 */
	boolean saveMemberAgreement(Agreement memberAgreement);

	/**
	 * 新增代理商协议
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午13:46:29
	 * 
	 * @param agentAgreement
	 *            代理协议
	 * @return 返回新增结果
	 */
	boolean saveAgentAgreement(Agreement agentagreement);

	/**
	 * 新增供应商协议
	 * 
	 * @author 郝江奎
	 * @date 2015年10月12日 下午14:46:29
	 * 
	 * @param vendorAgreement
	 *            供应商协议
	 * @return 返回新增结果
	 */
	boolean saveVendorAgreement(Agreement agentagreement);

	/**
	 * 更新协议
	 * 
	 * @author 郝江奎
	 * @date 2015年9月6日 下午13:48:26
	 * 
	 * @param agreement
	 *            协议
	 * @return 返回更新结果
	 */
	boolean updateAgreement(Agreement agreement);

	/**
	 * 更新商城信息和用户信息
	 * 
	 * @author 王少辉
	 * @date 2015年7月30日 下午12:40:48
	 * 
	 * @param mallDto
	 * @return
	 */
	boolean updateMallAndUser(MallDto mallDto);

	/**
	 * 新增一条商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:51:51
	 * 
	 * @param mall
	 *            要新增的商城信息
	 */
	boolean saveMall(Mall mall);

	/**
	 * 更新一条商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:51:55
	 * 
	 * @param mall
	 *            要更新的商城信息
	 */
	boolean updateMall(Mall mall);

	/**
	 * 新增商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午10:02:26
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回新增结果
	 */
	boolean saveMallNotice(MallNotice mallNotice);

	/**
	 * 更新商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午10:03:24
	 * 
	 * @param mallNotice
	 *            商城公告
	 * @return 返回修改结果
	 */
	boolean updateMallNotice(MallNotice mallNotice);

	/**
	 * 根据商城主键删除商城信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月13日 上午9:51:59
	 * 
	 * @param id
	 *            商城主键
	 */
	boolean deleteMallById(Integer id);

	/**
	 * 根据商城主键删除商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年5月20日 下午10:04:21
	 * 
	 * @param noticeId
	 *            商城公告主键
	 * @return 返回删除结果
	 */
	boolean delMallNoticeById(Integer noticeId);

	/**
	 * 根据商城主键删除商城公告
	 * 
	 * @author 王少辉
	 * @date 2015年8月27日 下午7:44:12
	 * 
	 * @param noticeIds
	 *            商城公告主键
	 * @return 返回删除结果
	 */
	boolean delMallNoticeByIds(List<Integer> noticeIds);

	// ----------------------------------- 商城人员 ---------------------------------------
	/**
	 * 获取商城全部工作人员
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午1:18:32
	 * 
	 * @param mallId
	 * @param includeRoles
	 * @return
	 */
	List<User> getMallWorkersById(Integer mallId, boolean includeRoles);

}
