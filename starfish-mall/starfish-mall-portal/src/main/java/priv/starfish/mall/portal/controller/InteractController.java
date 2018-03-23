package priv.starfish.mall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.GoodsInquiry;
import priv.starfish.mall.service.InteractService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@Remark("在线客服、在线咨询等")
@RequestMapping(value = "/interact")
public class InteractController extends BaseController {

	@Resource
	InteractService interactService;

	@Remark("在线客服列表页面")
	@RequestMapping(value = { "/olsList/jsp" }, method = RequestMethod.GET)
	public String toOlsListPage() {
		return "interact/olsList";
	}
	
	@Remark("在线客服聊天页面")
	@RequestMapping(value = { "/olsChat/jsp" }, method = RequestMethod.GET)
	public String toOlsChatPage() {
		return "interact/olsChat";
	}
	
	/**
	 * 分布查询在线客服列表
	 * 
	 * @author 郝江奎
	 * @date 2015年9月19日 上午11:45:40
	 * 
	 * @param request
	 *            请求参数
	 * @return 返回在线客服列表
	 */
	@Remark("查询在线客服列表")
	@RequestMapping(value = "/servant/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<InteractUserDto>> listOls(HttpServletRequest request) {
		Result<List<InteractUserDto>> result = Result.newOne();
		//获取当前的authScope和entityId
		AuthScope authScope = AuthScope.valueOf("mall");
		Integer entityId = 1;
		//根据条件查找在线客服
		List<InteractUserDto> InteractUserDtos = interactService.getOnlineServeNos(authScope, entityId);
		if (InteractUserDtos.size() > 0) {
			result.type = Type.info;
			result.data = InteractUserDtos;
			result.message = "获取客服成功";
		}else {
			result.type = Type.warn;
			result.message = "获取失败";
		}
		
		
		return result;
	}

	/**
	 * 发表咨询
	 * 
	 * @author 毛智东
	 * @date 2015年7月24日 下午4:47:41
	 * 
	 * @param goodsInquiry
	 * @return
	 */
	@Remark("发表咨询")
	@RequestMapping(value = "/inquiry/send/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> sendInquiry(HttpServletRequest request, @RequestBody GoodsInquiry goodsInquiry) {
		UserContext userContext = getUserContext(request);
		//
		Result<Boolean> result = Result.newOne();
		String successMsg = "提交成功";
		String failMsg = "提交失败";
		try {
			if (userContext.isSysUser()) {
				goodsInquiry.setUserId(userContext.getUserId());
				boolean flag = interactService.saveGoodsInquiry(goodsInquiry);
				result.message = flag ? successMsg : failMsg;
				result.data = flag;
			} else {
				result.type = Type.fatal;
				result.message = "登录后才可以咨询";
			}
		} catch (Exception e) {
			result.type = Type.error;
			result.message = "出错了，请重新提交";
			e.printStackTrace();
			return result;
		}
		return result;
	}
}
