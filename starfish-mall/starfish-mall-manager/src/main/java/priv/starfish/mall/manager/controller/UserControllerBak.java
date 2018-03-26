/*
package priv.starfish.mall.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.web.CheckCodeHelper;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserRole;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.UserService;

import javax.servlet.http.HttpServletRequest;


*/
/**
 * Created by starfish on 2017/8/24.
 *//*

@Controller
@RequestMapping("/user")
public class UserControllerBak extends BaseController {

    //Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    */
/**
     * @description 跳转到用户列表页面
     * @date 2017/12/15 16:08
     *//*

    */
/*@RequestMapping("/listUsers")
    public String getUserList(){
        return "system/user/userList";
    }*//*


    */
/**
     * @description 获取用户数据
     * @date 2017/12/15 16:08
     *//*

    */
/*@ResponseBody
    @RequestMapping("/getUserList")
    public Object listUsers(User form) {
        List<User> user = userService.getAllUsers(form);
        return user;
    }*//*


    */
/**
     * @description 登录页面
     * @date 2017/12/15 16:05
     *//*

    @RequestMapping(value = "/login/jsp",method = RequestMethod.GET)
    public String toLoginJsp(){
        return "user/login";
    }

    */
/**
     * @description 执行登录
     * @date 2017/12/18 16:03
     *//*

    @RequestMapping(value = "/login/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> doLogin(HttpServletRequest request, @RequestBody MapContext requestData) {
        Result<String> result = Result.newOne();
        //
        String phoneNo = requestData.getTypedValue("phoneNo", String.class);
        String password = requestData.getTypedValue("password", String.class);
        String chkCode = requestData.getTypedValue("chkCode", String.class);
        //
        if (StrUtil.isNullOrBlank(phoneNo) || StrUtil.isNullOrBlank(password) || StrUtil.isNullOrBlank(chkCode)) {
            result.type = Type.warn;
            result.message = "请输入完整信息";
        } else {

            if (CheckCodeHelper.isValidCode(request.getSession(), chkCode)) {
                UserContext userContext = getUserContext(request);
                // 执行登录
               // password = RSACrypter.decryptStringFromJs(password);
                Result<?> loginResult = userService.doLogin(phoneNo, password);
                if (loginResult.type == Type.info) {
                    User user = userService.getUserByPhoneNo(phoneNo);
                    userContext.setUserId(user.getId());
                    userContext.setPhoneNo(user.getPhoneNo());
                    userContext.setUserName(user.getNickName());
                    Gender gender = user.getGender();
                    if (gender == null) {
                        gender = Gender.X;
                    }
                    // userContext.setGender(gender.name());
                    // Integer userId = user.getId();
                    if (user.isSysAdmin()) {
                        // 系统管理员 - 直接登录
                        ScopeEntity scopeEntity = ScopeEntity.newOne();
                        //
                        userContext.setScopeEntity(scopeEntity);
                        //
                        scopeEntity.setScope(AuthScope.sys.name());
                        scopeEntity.setId(UserRole.SysEntityId);
                        scopeEntity.setName("系统后台");
                        //
                        setUserContext(request, userContext);
                        // 检查页面重定向
                        String redirectUrl = (String) request.getSession().getAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
                        if (StrUtil.hasText(redirectUrl)) {
                            result.data = redirectUrl;
                            //
                            request.getSession().removeAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
                        } else {
                            result.data = "/navgt/sys/root/frame/jsp";
                        }
                    } else {
                        // 非系统管理员
                        setUserContext(request, userContext);

                        // 检查页面重定向
                        String redirectUrl = (String) request.getSession().getAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
                        if (StrUtil.hasText(redirectUrl)) {
                            result.data = redirectUrl;
                            //
                            request.getSession().removeAttribute(AppBase.SESSION_KEY_REDIRECT_URL);
                        } else {
                            // 多入口 - 让用户选择
                            result.data = "/navgt/entry/index/jsp";
                        }
                    }
                }
                result.type = loginResult.type;
                result.message = loginResult.message;
            } else {
                result.type = Type.warn;
                result.message = "验证码错误";
            }
        }
        return result;
       //return "redirect::http://localhost:8080/starfish-mall-manager-listener/navgt/entry/index/jsp";
    }
}
*/
