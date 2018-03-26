package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.mall.comn.entity.BankProvinceCode;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.UserAccountService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.settle.entity.SettleWay;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 用户资金账户
 * 
 * @author 郝江奎
 * @date 2015年11月10日 下午4:06:33
 *
 */
@Controller
@RequestMapping("/userAccount")
public class UserAccountController extends BaseController {

	@Resource
	private UserService userService;

	@Resource
	private UserAccountService userAccountService;

	/**
	 * 
	 * 获取用户资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015年11月2日 下午8:35:07
	 * 
	 * @param request
	 * @return JqGridPage<UserAccount>
	 */
	@Remark("获取用户资金账户")
	@RequestMapping(value = "/list/by/bankCode", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<UserAccount>> getUserAccountListDisByBankCode(HttpServletRequest request, @RequestParam("userId") int userId) {
		Result<List<UserAccount>> result = Result.newOne();
		//
		List<UserAccount> UserAccountList = userAccountService.getUserAccountsDisByBankCode(userId);
		result.data = UserAccountList;
		return result;
	}

	/**
	 * 增加用户资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015年9月24日 下午4:11:09
	 * 
	 * @return
	 */
	@Remark("增加用户资金账户")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserAccount> addUserAccount(HttpServletRequest request, @RequestBody UserAccount userAccount) {
		Result<UserAccount> result = Result.newOne();
		Integer userId = userAccount.getUserId();
		if (userId == null) {
			UserContext userContext = getUserContext(request);
			userId = userContext.getUserId();
			userAccount.setUserId(userId);
		}

		userAccount.setVerified(false);
		userAccount.setDisabled(false);
		userAccount.setTs(new Date());
		//
		boolean ok = userAccountService.createUserAccount(userAccount);
		if (ok) {
			result.data = userAccount;
			result.message = "添加成功";
		} else {
			result.type = Type.warn;
			result.message = "添加失败";
		}
		return result;
	}

	/**
	 * 
	 * 获取用户资金账户
	 * 
	 * @author guoyn
	 * @date 2015年10月9日 下午6:35:07
	 * 
	 * @param request
	 * @return JqGridPage<UserAccount>
	 */
	@Remark("获取用户资金账户")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<UserAccount> getUserAccountList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<UserAccount> paginatedList = userAccountService.getUserAccountsByFilter(paginatedFilter);
		//
		JqGridPage<UserAccount> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 删除用户资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015年9月24日 下午6:11:09
	 * 
	 * @return
	 */
	@Remark("删除用户资金账户")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserAccount> delUserAccount(HttpServletRequest request, @RequestParam("id") Integer id) {
		Result<UserAccount> result = Result.newOne();
		//
		boolean ok = userAccountService.deleteUserAccountById(id);
		if (ok) {
			result.message = "删除成功";
		} else {
			result.type = Type.warn;
			result.message = "删除失败";
		}
		return result;
	}

	/**
	 * 修改用户资金账户
	 * 
	 * @author 郝江奎
	 * @date 2015年9月24日 下午7:11:09
	 * 
	 * @return
	 */
	@Remark("修改用户资金账户")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<UserAccount> updateUserAccount(HttpServletRequest request, @RequestBody UserAccount userAccount) {
		Result<UserAccount> result = Result.newOne();
		// 获取页面缓存用户信息
		// UserContext userContext = getUserContext(request);
		// int userId = userContext.getUserId();
		//
		boolean ok = userAccountService.updateUserAccount(userAccount);
		if (ok) {
			result.message = "修改成功";
		} else {
			result.type = Type.warn;
			result.message = "修改失败";
		}
		return result;
	}

	/**
	 * 
	 * 获取商城的结算通道
	 * 
	 * @author 郝江奎
	 * @date 2016年1月4日 上午10:35:07
	 * 
	 * @param request
	 * @return JqGridPage<settleWay>
	 */
	@Remark("获取商城的结算通道")
	@RequestMapping(value = "/settleWay/get/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SettleWay>> getUserWay(HttpServletRequest request, @RequestParam("userId") int userId) {
		Result<List<SettleWay>> result = Result.newOne();
		//
		List<SettleWay> settleWayList = userAccountService.getUserWay(userId);
		result.data = settleWayList;
		return result;
	}

	/**
	 * 获取银行省份code列表
	 * 
	 * @author 郝江奎
	 * @date 2016年1月22日上午10:06:06
	 * 
	 * @return
	 */
	@Remark("获取银行省份code列表")
	@RequestMapping(value = "/bankProvinceCode/selectList/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SelectList> getBankProvinceCodeList() {
		Result<SelectList> result = Result.newOne();
		SelectList selectList = SelectList.newOne();
		List<BankProvinceCode> list = userAccountService.getBankProvinceCodes();
		selectList.setUnSelectedItem("", "- 请选择 -");
		if (list.size() > 0) {
			for (BankProvinceCode bankProvinceCode : list) {
				selectList.addItem(bankProvinceCode.getCode(), bankProvinceCode.getProvince());
			}
		}
		selectList.setDefaultValue("");
		result.data = selectList;
		return result;
	}
}
