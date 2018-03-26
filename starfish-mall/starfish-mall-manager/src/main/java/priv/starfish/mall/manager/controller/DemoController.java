package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.mall.comn.dto.UserDto;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;

@Controller
@Remark("各种演示")
@RequestMapping(value = "/demo")
public class DemoController extends BaseController {

	@Resource
	FileRepository fileRepository;

	@Remark("文件(上传)演示页面")
	@RequestMapping(value = { "/file/jsp" }, method = RequestMethod.GET)
	public String toFilePage() {
		return "demo/file";
	}

	@Remark("图片对话框 + CKEditor 示例页面")
	@RequestMapping(value = { "/repoImage/jsp" }, method = RequestMethod.GET)
	public String toRepoImagePage() {
		return "demo/repoImage";
	}
	
	@RequestMapping(value = { "/shop/jsp" }, method = RequestMethod.GET)
	public String  toDemoJsp(){
		return "demo/shop";
	}
	
	@RequestMapping(value = { "/xxx/data" }, method = RequestMethod.POST)
	@ResponseBody
	public Result<UserDto>  getDemoData( @RequestBody UserDto userDto ){
		System.out.println(userDto);

		userDto.setPassword("aaaaaaaaaaaaa");
		
		Result<UserDto> result = Result.newOne();
		result.data = userDto;
		//
		return result;
	}
}
