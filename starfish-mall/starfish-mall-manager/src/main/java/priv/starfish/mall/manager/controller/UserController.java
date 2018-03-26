package priv.starfish.mall.manager.controller;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.exception.UnAuthenticatedException;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.common.web.CheckCodeHelper;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.car.entity.UserCar;
import priv.starfish.mall.car.entity.UserCarSvcRec;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.dto.UserDto;
import priv.starfish.mall.comn.entity.*;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.mall.entity.Mall;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.notify.dict.SmsUsage;
import priv.starfish.mall.notify.entity.SmsVerfCode;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.UserSvcPackTicket;
import priv.starfish.mall.order.po.SaleOrderPo;
import priv.starfish.mall.service.*;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.web.base.CacheHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


/**
 * Created by starfish on 2017/8/24.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    Logger log = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private MemberService memberService;

    @Resource
    private MallService mallService;

    @Resource
    private ShopService shopService;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private AgencyService agencyService;

    @Resource
    private DistShopService wxShopService;

    @Resource
    private SettingService settingService;

    @Resource
    private SaleOrderService saleOrderService;

    @Resource
    FileRepository fileRepository;

    @Resource
    CarService carService;

    /**
     * @description 跳转到用户列表页面
     * @date 2017/12/15 16:08
     */
    /*@RequestMapping("/listUsers")
    public String getUserList(){
        return "system/user/userList";
    }*/

    /**
     * @description 获取用户数据
     * @date 2017/12/15 16:08
     */
    /*@ResponseBody
    @RequestMapping("/getUserList")
    public Object listUsers(User form) {
        List<User> user = userService.getAllUsers(form);
        return user;
    }*/

    /**
     * @description 登录页面
     * @date 2017/12/15 16:05
     */
    @RequestMapping(value = "/login/jsp",method = RequestMethod.GET)
    public String toLoginJsp(){
        return "user/login";
    }

    /**
     * 商城套餐服务票管理页面
     *
     * @author wangdi
     * @date 2016年1月27日 下午6:05:55
     *
     * @return
     */
    @Remark("商城套餐服务票管理页面")
    @RequestMapping(value = "/svc/pack/ticket/list/jsp/-mall", method = RequestMethod.GET)
    public String toSvcPackTicketListJsp() {
        return "user/svcPackTicketList";
    }

    /**
     * 门店套餐服务票管理页面
     *
     * @author wangdi
     * @date 2016年1月28日 下午4:05:21
     *
     * @return
     */
    @Remark("门店套餐服务票管理页面")
    @RequestMapping(value = "/svc/pack/ticket/list/jsp/-shop", method = RequestMethod.GET)
    public String toShopSvcPackTicketListJsp() {
        return "user/shopSvcPackTicketList";
    }




    /**
     * @description 执行登录
     * @date 2017/12/18 16:03
     */
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
            result.type = Result.Type.warn;
            result.message = "请输入完整信息";
        } else {
            if (CheckCodeHelper.isValidCode(request.getSession(), chkCode)) {
                UserContext userContext = getUserContext(request);
                Subject currentUser = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(phoneNo,password);
                token.setRememberMe(true);

                try{
                    System.out.println("1. " + token.hashCode());
                    //可以login的话，说明认证通过
                    currentUser.login(token);

                    User user = userService.getUserByPhoneNo(phoneNo);
                    userContext.setUserId(user.getId());
                    userContext.setPhoneNo(user.getPhoneNo());
                    userContext.setUserName(user.getNickName());
                    Gender gender = user.getGender();
                    if (gender == null) {
                        gender = Gender.X;
                    }
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
                    }else{
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
                }catch (UnknownAccountException uae) {
                    log.error("-----用户不存在-----"+uae.getMessage());
                } catch (IncorrectCredentialsException ice) {
                    log.error("-----密码不匹配-----"+ice.getMessage());
                }catch (AuthenticationException e){
                    System.out.println("登录失败："+e.getMessage());
                    log.error(e.getMessage());
                    e.printStackTrace();
                    //token.clear();
                }
            } else {
                result.type = Type.warn;
                result.message = "验证码错误";
            }
        }
        return result;
    }


    @Remark("返回当前用户信息")
    @RequestMapping(value = "/info/context/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<UserContext> getContextUserInfo(HttpServletRequest request) {
        UserContext userContext = getUserContext(request);
        // 获取商城的logo
        MallDto mallDto = mallService.getMallInfo();
        if (userContext.getScopeEntity() == null) {
            ScopeEntity scopeEntity = ScopeEntity.newOne();
            //TODO  待解决：mallDto查询为null
            scopeEntity.setLogoUrl(mallDto.getFileBrowseUrl());
            scopeEntity.setName(mallDto.getName());
            userContext.setScopeEntity(scopeEntity);
        }
        //
        UserContext newUserContext = UserContext.newOne();
        //
        Result<UserContext> result = Result.newOne();
        if (userContext.isSysUser()) {
            newUserContext.setUserId(userContext.getUserId());
            newUserContext.setUserName(userContext.getUserName());
            newUserContext.setPhoneNo(userContext.getPhoneNo());
            newUserContext.setScopeEntity(userContext.getScopeEntity());
            result.data = newUserContext;
        } else {
            result.type = Result.Type.error;
            result.message = "登录超时";
        }
        return result;
    }



    /**
     * 注销操作
     *
     * @author 廖晓远
     * @date 2015-5-13 下午6:06:03
     * @param request
     * @return String 登录页面
     */
    @Remark("注销操作")
    @RequestMapping(value = "/logout/do", method = RequestMethod.GET)
    public String toLoginOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.getSession(false).invalidate();
        }
        return toLoginJsp();
    }

    /**
     * 返回用户所有实体入口
     *
     * @author guoyn
     * @date 2015年9月9日 下午4:53:41
     *
     * @param request
     * @return Result<Map<String, Object>>
     */
    @Remark("返回当前用户所有入口选择列表")
    @RequestMapping(value = "/entry/list/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<Map<String, Object>> getEntryList(HttpServletRequest request) {
        UserContext userContext = getUserContext(request);
        Result<Map<String, Object>> result = Result.newOne();
        //
        if (userContext.isSysUser()) {
            int userId = userContext.getUserId();
            List<ScopeEntity> scopeEntities = authxService.getScopeEntitiesByUserId(userId);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            // 商城
            Map<String, Object> mallMap = null;
            // 店铺
            List<Map<String, Object>> shops = new ArrayList<Map<String, Object>>(1);
            // 代理处
            List<Map<String, Object>> agencies = new ArrayList<Map<String, Object>>(1);
            // 卫星店
            Map<String, Object> wxshopMap = null;
            //
            for (ScopeEntity scopeEntity : scopeEntities) {
                Integer entityId = scopeEntity.getId();
                AuthScope scope = EnumUtil.valueOf(AuthScope.class, scopeEntity.getScope());
                if (scope == null) {
                    continue;
                }
                //
                if (scope.equals(AuthScope.mall)) {
                    if (mallMap == null) {
                        MallDto mallInfo = CacheHelper.getMallInfo();
                        if (mallInfo != null) {
                            mallMap = new HashMap<String, Object>();
                            mallMap.put("id", entityId);
                            mallMap.put("name", mallInfo.getName());
                            mallMap.put("logoPath", mallInfo.getFileBrowseUrl());
                            mallMap.put("bizScope", mallInfo.getBizScope());
                            mallMap.put("desc", mallInfo.getDesc());
                        }
                    }
                } else if (scope.equals(AuthScope.shop)) {
                    Shop shop = shopService.getShopById(entityId);
                    if (shop != null) {
                        Map<String, Object> shopMap = new HashMap<String, Object>();
                        shopMap.put("id", entityId);
                        shopMap.put("name", shop.getName());
                        shopMap.put("logoPath", shop.getFileBrowseUrl());
                        shopMap.put("bizScope", shop.getBizScope());
                        shopMap.put("applyTime", shop.getApplyTime());
                        shopMap.put("auditStatus", shop.getAuditStatus());
                        shopMap.put("closed", shop.getClosed());
                        shopMap.put("disabled", shop.getDisabled());
                        // 每个店铺放入店铺List集合中
                        shops.add(shopMap);
                    }
                } else if (scope.equals(AuthScope.agency)) {
                    Agency agency = agencyService.getAgencyById(entityId);
                    //
                    if (agency != null) {
                        Map<String, Object> agencyMap = new HashMap<String, Object>();
                        agencyMap.put("id", entityId);
                        agencyMap.put("name", agency.getName());
                        agencyMap.put("logoPath", agency.getFileBrowseUrl());
                        agencyMap.put("bizScope", agency.getBizScope());
                        agencyMap.put("applyTime", agency.getApplyTime());
                        agencyMap.put("auditStatus", agency.getAuditStatus());
                        agencyMap.put("disabled", agency.getDisabled());
                        // 每个店铺放入店铺List集合中
                        agencies.add(agencyMap);
                    }
                } else if (scope.equals(AuthScope.wxshop)) {
                    if (wxshopMap == null) {
                        // TODO
                    }

                }
            }
            // 商城唯一
            resultMap.put("mall", mallMap);
            //
            resultMap.put("shops", shops);
            //
            resultMap.put("agencies", agencies);
            //
            resultMap.put("wxshop", wxshopMap);

            result.data = resultMap;
        } else {
            result.type = Result.Type.error;
            result.message = "登录超时";
        }
        return result;
    }

    /**
     * 返回所选择入口界面
     *
     * @author 廖晓远
     * @date 2015-5-13 下午6:09:34
     * @param request
     * @param requestData
     * @return Result<String> 多入口所选择的页面
     */
    @Remark("返回当前用户选择的入口url")
    @RequestMapping(value = "/entry/target/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<String> getEntryTarget(HttpServletRequest request, @RequestBody MapContext requestData) {
        UserContext userContext = getUserContext(request);
        //
        Result<String> result = Result.newOne();
        //
        if (userContext.isSysUser()) {
            Integer entityId = requestData.getTypedValue("entityId", Integer.class);
            String scope = requestData.getTypedValue("scope", String.class);
            if (entityId != null && entityId > UserRole.SysEntityId && scope != null) {
                if (AuthScope.mall.name().equals(scope)) {
                    Mall mall = mallService.getMallById(entityId);
                    if (mall != null) {
                        MallDto mallInfo = CacheHelper.getMallInfo();
                        ScopeEntity scopeEntity = ScopeEntity.newOne();
                        userContext.setScopeEntity(scopeEntity);
                        //
                        scopeEntity.setId(mallInfo.getId());
                        scopeEntity.setScope(scope);
                        scopeEntity.setName(mallInfo.getName());
                        scopeEntity.setLogoUrl(mallInfo.getFileBrowseUrl());
                        //
                        setUserContext(request, userContext);
                        //
                        List<SiteModule> siteResTree = this.fetchUserSiteModules(userContext);
                        //将菜单加到session
                        setUserScopeEntitySiteResTree(request, siteResTree);
                        //
                        result.data = "/navgt/mall/root/frame/jsp";
                    } else {
                        result.type = Result.Type.error;
                        result.message = "商城不存在";
                        result.data = "/navgt/entry/index/jsp";
                    }
                } else if (AuthScope.shop.name().equals(scope)) {
                    Shop shop = shopService.getShopById(entityId);
                    if (shop != null) {
                        ScopeEntity scopeEntity = ScopeEntity.newOne();
                        userContext.setScopeEntity(scopeEntity);
                        //
                        scopeEntity.setId(shop.getId());
                        scopeEntity.setScope(scope);
                        scopeEntity.setName(shop.getName());
                        scopeEntity.setLogoUrl(shop.getFileBrowseUrl());
                        //
                        setUserContext(request, userContext);
                        //
                        List<SiteModule> siteResTree = this.fetchUserSiteModules(userContext);
                        setUserScopeEntitySiteResTree(request, siteResTree);
                        //
                        result.data = "/navgt/shop/root/frame/jsp";
                    } else {
                        result.type = Result.Type.error;
                        result.message = "店铺不存在";
                        result.data = "/navgt/entry/index/jsp";
                    }
                }
            } else {
                result.type = Result.Type.error;
                result.message = "提供的信息不充分";
                result.data = "/navgt/entry/index/jsp";
            }
        } else {
            throw new UnAuthenticatedException("未登录或会话已超时");
        }
        return result;
    }


    @Remark("重置密码页面")
    @RequestMapping(value = "/logPass/retrv/jsp", method = RequestMethod.GET)
    public String toRetrieveLogPassJsp() {
        return "user/logPass-retrv";
    }

    @Remark("重置密码")
    @RequestMapping(value = "/logPass/reset/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result<Boolean> resetLogPswd(HttpSession session, @RequestBody MapContext requestData) {
        Result<Boolean> result = Result.newOne();
        String phoneNo = requestData.getTypedValue("phoneNo", String.class);
        String password = requestData.getTypedValue("password", String.class);
        String chkCode = requestData.getTypedValue("chkCode", String.class);
        String smsCode = requestData.getTypedValue("smsCode", String.class);
        if (CheckCodeHelper.isValidCode(session, chkCode)) {
            User user = userService.getUserByPhoneNo(phoneNo);
            if (user != null) {
                SmsVerfCode smsVerfCode = new SmsVerfCode();
                smsVerfCode.setPhoneNo(phoneNo);
                smsVerfCode.setVfCode(smsCode);
                smsVerfCode.setUsage(SmsUsage.logPass);
                if (settingService.validSmsVerfCode(smsVerfCode)) {
                    String salt = PasswordUtil.generateSaltStr();
                    password = RSACrypter.decryptStringFromJs(password);
                    password = PasswordUtil.encrypt(password, salt);
                    user.setSalt(salt);
                    user.setPassword(password);
                    boolean ok = userService.updateUser(user);
                    if (ok) {
                        // 修改验证码记录（已使用）
                        settingService.updateSmsVerfCode(smsVerfCode);
                    }
                    result.data = ok;
                } else {
                    result.type = Type.error;
                    result.message = "短信验证码错误";
                }
            } else {
                result.type = Type.error;
                result.message = "手机号码错误";
            }
        } else {
            result.type = Type.error;
            result.message = "验证码错误";
        }
        return result;
    }

    // --------------------------------个人信息------------------------------------
    /**
     * 用户个人信息页面
     *
     * @author 王少辉
     * @date 2015年5月29日 上午11:00:43
     *
     * @return 返回用户个人信息页面
     */
    @Remark("用户个人信息页面")
    @RequestMapping(value = "/personal/info/jsp", method = RequestMethod.GET)
    public String toUserSettingPage() {
        return "user/userInfo";
    }

    /**
     * 查询用户信息
     *
     * @author 王少辉
     * @date 2015年5月29日 下午6:02:12
     *
     * @param userDto
     * @param request
     * @return
     */
    @Remark("查询用户信息")
    @RequestMapping(value = "/info/get", method = RequestMethod.POST)
    @ResponseBody
    public Result<UserDto> getUserInfo(@RequestBody UserDto userDto, HttpServletRequest request) {
        Result<UserDto> result = Result.newOne();

        UserContext userContext = getUserContext(request);
        User user = userService.getUserById(userContext.getUserId());
        if (user != null) {
            UserDto dto = new UserDto();
            TypeUtil.copyProperties(user, dto);
            List<Role> roles = authxService.getRolesByUserId(user.getId());
            dto.setRoles(roles);
            result.data = dto;
        }

        return result;
    }

    // /**
    // * 检验用户密码
    // *
    // * @author 王少辉
    // * @date 2015年5月30日 下午5:00:12
    // *
    // * @param userDto
    // * @param request
    // * @return
    // */
    // @Description("检验用户密码")
    // @RequestMapping(value = "/password/check/do", method = RequestMethod.POST)
    // @ResponseBody
    // public Result<?> checkUserPswd(@RequestBody UserDto userDto, HttpServletRequest request) {
    // UserContext userContext = getUserContext(request);
    // //
    // Result<?> result = Result.newOne();
    //
    // if (!userContext.isSysUser()) {
    // result.type = Result.Type.error;
    // result.message = "登录超时";
    // } else {
    // boolean ok = userService.checkUserPassword(userDto);
    // if (!ok) {
    // result.type = Result.Type.error;
    // }
    // }
    //
    // return result;
    // }

    /**
     * 更新用户信息
     *
     * @author 王少辉
     * @date 2015年5月30日 下午6:00:18
     *
     * @param mapContext
     * @param request
     * @return
     */
    @Remark("更新用户信息")
    @RequestMapping(value = "/info/update/do", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> saveUserInfo(@RequestBody MapContext mapContext, HttpServletRequest request) {
        Result<?> result = Result.newOne();
        //
        UserContext userContext = getUserContext(request);

        User userInfo = userService.getUserById(userContext.getUserId());
        if (userInfo != null && userInfo.getPhoneNo().equals(userContext.getPhoneNo())) {
            userInfo.setNickName(mapContext.getTypedValue("nickName", String.class));
            String gender = mapContext.getTypedValue("gender", String.class);
            if (gender.equals(Gender.M.name())) {
                userInfo.setGender(Gender.M);
            } else if (gender.equals(Gender.F.name())) {
                userInfo.setGender(Gender.F);
            } else {
                userInfo.setGender(Gender.X);
            }
            userInfo.setEmail(mapContext.getTypedValue("email", String.class));
            userInfo.setRealName(mapContext.getTypedValue("realName", String.class));

            boolean ok = userService.updateUser(userInfo);
            if (ok) {
                userContext.setUserName(userInfo.getNickName());
                userContext.setPhoneNo(userInfo.getPhoneNo());
            }
            result.message = ok ? "更新成功" : "更新失败";

        } else {
            result.type = Result.Type.error;
            result.message = "会话超时，请重新登录";
        }

        return result;
    }

    /**
     * 更新用户登录密码
     *
     * @author 王少辉
     * @date 2015年8月27日 下午8:47:31
     *
     * @param mapContext
     * @param request
     * @return
     */
    @Remark("更新用户登录密码")
    @RequestMapping(value = "/logPass/update/do", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> updateLogPswd(@RequestBody MapContext mapContext, HttpServletRequest request) {
        Result<?> result = Result.newOne();
        //
        UserContext userContext = getUserContext(request);

        User userInfo = userService.getUserById(userContext.getUserId());
        // 检验密码
        String oldPassword = mapContext.getTypedValue("oldPassword", String.class);
        if (StrUtil.hasText(oldPassword)) {
            oldPassword = RSACrypter.decryptStringFromJs(oldPassword);
            boolean ok = userService.verifyUserPassword(userContext.getUserId(), oldPassword);
            if (ok) {
                String password = mapContext.getTypedValue("password", String.class);
                if (StrUtil.hasText(password)) {
                    String salt = userInfo.getSalt();
                    userInfo.setSalt(salt);
                    password = RSACrypter.decryptStringFromJs(password);
                    String enNewPassword = PasswordUtil.encrypt(password, salt);
                    userInfo.setPassword(enNewPassword);

                    result.message = userService.updateUser(userInfo) ? "修改成功" : "修改失败";
                }
            } else {
                result.type = Result.Type.error;
                result.message = "旧密码有误，请重新输入";
            }
        }

        return result;
    }

    // --------------------------------在线客服------------------------------------
    /**
     * 跳转到在线客服列表页面
     *
     * @author 王少辉
     * @date 2015年7月8日 下午8:21:24
     *
     * @return 返回在线客服列表页面
     */
    @Remark("在线客服列表页面")
    @RequestMapping(value = "/online/servant/list/jsp/-mall", method = RequestMethod.GET)
    public String toMallOlsListPage() {
        return "interact/olsList";
    }

    @Remark("在线客服列表页面")
    @RequestMapping(value = "/online/servant/list/jsp/-shop", method = RequestMethod.GET)
    public String toShopOlsListPage() {
        return "interact/olsList";
    }

    /**
     * 邮箱是否存在
     *
     * @author 毛智东
     * @date 2015年7月6日 下午1:17:54
     *
     * @param email
     * @return
     */
    @Remark("邮箱是否存在")
    @RequestMapping(value = "/exist/by/email", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> getUserExistByEmail(@RequestBody String email) {
        Result<Boolean> result = Result.newOne();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            result.data = true;
        }
        return result;
    }

    // --------------------------------申请商户/供应商入驻------------------------------------

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
    @RequestMapping(value = "/userAccount/list/get", method = RequestMethod.POST)
    @ResponseBody
    public JqGridPage<UserAccount> getUserAccountList(HttpServletRequest request) {
        // 封装前台参数为JqGridRequest格式
        JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
        // 封装为PaginatedFilter格式
        PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
        // 获取页面缓存用户信息
        UserContext userContext = getUserContext(request);
        Integer userId = userContext.getUserId();
        // Integer userId = 1;
        MapContext filters = paginatedFilter.getFilterItems();
        filters.put("userId", userId);
        //
        PaginatedList<UserAccount> paginatedList = userAccountService.getUserAccountsByFilter(paginatedFilter);
        //
        JqGridPage<UserAccount> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
        return jqGridPage;
    }

    @Remark("增加企业营业执照")
    @RequestMapping(value = "/bizLicense/add/do", method = RequestMethod.POST)
    @ResponseBody
    public Result<BizLicense> addBizLicense(HttpServletRequest request, @RequestBody BizLicense bizLicense) {
        Result<BizLicense> result = Result.newOne();
        // 获取页面缓存用户信息
        UserContext userContext = getUserContext(request);

        Integer userId = bizLicense.getUserId();
        if (userId == null) {
            userId = userContext.getUserId();
            bizLicense.setUserId(userId);
        }
        //
        boolean ok = shopService.createBizLicense(bizLicense);
        if (ok) {
            result.data = bizLicense;
            result.message = "添加成功";
        } else {
            result.type = Type.warn;
            result.message = "添加失败";
        }
        return result;
    }

    /**
     * 根据用户手机号获取用户信息
     *
     * @author wangdi
     * @date 2015年10月22日 上午11:34:40
     *
     * @return
     */
    @Remark("根据用户手机号获取用户信息")
    @RequestMapping(value = "/info/get/by/phoneNo/-shop", method = RequestMethod.POST)
    @ResponseBody
    public Result<SaleOrderPo> getUserInfoByPhoneNo(HttpServletRequest request) {
        Result<SaleOrderPo> result = Result.newOne();
        SaleOrderPo context = new SaleOrderPo();

        try {
            String mobileNo = request.getParameter("mobileNo");
            if (mobileNo != null) {
                User user = userService.getUserByPhoneNo(mobileNo);
                if (user != null) {
                    context.setUserId(user.getId());
                    context.setUserName(user.getNickName());
                    context.setUserPhone(user.getPhoneNo());
                    context.setLinkMan(user.getRealName());
                    context.setLinkNo(user.getPhoneNo());
                } else {
                    context.setUserId(-1);
                }
            } else {
                UserContext userContext = getUserContext(request);
                Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
                ShopDto shopDto = shopService.getShopInfoById(shopId);
                context.setShopName(shopDto.getShop().getName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.type = Result.Type.error;
            result.message = "获取用户信息失败！";
        }

        result.data = context;

        return result;
    }

    /**
     * 根据id解锁、锁定会员
     *
     * @author 郝江奎
     * @date 2015年12月17日 下午15:17:13
     *
     * @param response
     * @return Result<Object>
     */
    @Remark("根据id解锁、锁定会员")
    @RequestMapping(value = "/update/locked/by/id", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> updateUserLockedById(@RequestBody User user, HttpServletResponse response) {
        //
        Result<Object> result = Result.newOne();
        result.message = "修改成功!";
        //
        if (user.getLocked()) {
            user.setLockTime(new Date());

        } else {
            user.setLockTime(null);
            user.setFailTime(null);
            user.setFailCount(0);
        }
        boolean ok = userService.updateUser(user);
        if (!ok) {
            result.type = Type.warn;
            result.message = "修改失败!";
        }
        return result;
    }

    /**
     * 获取用户销售订单
     *
     * @author wangdi
     * @date 2015年12月21日 下午12:03:56
     *
     * @param request
     * @return
     */
    @Remark("获取用户销售订单")
    @RequestMapping(value = "/saleOrder/list/get/-shop", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<SaleOrder>> getUserSaleOrders(HttpServletRequest request) {
        Result<List<SaleOrder>> result = Result.newOne();
        try {
            String userId = request.getParameter("userId");
            if (userId == null) {
                result.type = Result.Type.warn;
                result.message = "未指定用户";
                return result;
            }
            // 添加过滤条件
            UserContext userContext = getUserContext(request);
            Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
            MapContext filter = MapContext.newOne();
            filter.put("shopId", shopId);
            filter.put("userId", Integer.parseInt(userId));
            filter.put("orderState", "finished");

            List<SaleOrder> userSaleOrders = saleOrderService.getSaleOrdersByFilterNormal(filter);
            result.data = userSaleOrders;
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户套餐服务票
     *
     * @author wangdi
     * @date 2016年1月26日 下午3:45:19
     *
     * @param request
     * @return
     */
    @Remark("获取用户套餐服务票")
    @RequestMapping(value = "/svc/pack/ticket/list/get/-mall", method = RequestMethod.POST)
    @ResponseBody
    public JqGridPage<UserSvcPackTicket> getUserSvcPackTickets(HttpServletRequest request) {
        JqGridPage<UserSvcPackTicket> jqGridPage = null;

        try {
            JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
            PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
            PaginatedList<UserSvcPackTicket> paginatedList = saleOrderService.getUserSvcPackTicketsByFilter(paginatedFilter);
            jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return jqGridPage;
    }

    /**
     * 获取门店用户套餐服务票
     *
     * @author wangdi
     * @date 2016年1月26日 下午3:45:19
     *
     * @param request
     * @return
     */
    @Remark("获取门店用户套餐服务票")
    @RequestMapping(value = "/svc/pack/ticket/list/get/-shop", method = RequestMethod.POST)
    @ResponseBody
    public JqGridPage<UserSvcPackTicket> getShopUserSvcPackTickets(HttpServletRequest request) {
        JqGridPage<UserSvcPackTicket> jqGridPage = null;

        try {
            JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
            PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
            // 只查询绑定当前门店的服务票
            UserContext userContext = getUserContext(request);
            Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
            MapContext filter = paginatedFilter.getFilterItems();
            filter.put("shopId", shopId);
            // 执行查询
            PaginatedList<UserSvcPackTicket> paginatedList = saleOrderService.getUserSvcPackTicketsByFilter(paginatedFilter);
            jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return jqGridPage;
    }

    /**
     * 完成用户套餐服务票
     *
     * @author wangdi
     * @date 2016年1月26日 下午4:00:47
     *
     * @param request
     * @return
     */
    @Remark("完成用户套餐服务票")
    @RequestMapping(value = "/svc/pack/ticket/finish/do/-shop", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> finishUserSvcPackTicket(HttpServletRequest request) {
        Result<Boolean> result = Result.newOne();

        try {
            String ticketId = request.getParameter("ticketId");
            String doneCode = request.getParameter("doneCode");

            if (ticketId == null) {
                result.data = false;
                result.type = Result.Type.warn;
                result.message = "未指定套餐服务票";
                return result;
            }

            if (doneCode == null) {
                result.data = false;
                result.type = Result.Type.warn;
                result.message = "请输入服务完成确认码";
                return result;
            }

            UserContext userContext = getUserContext(request);
            Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
            Integer actorId = userContext.getUserId();
            String actorName = userContext.getUserName();
            MapContext filter = MapContext.newOne();
            filter.put("ticketId", Integer.parseInt(ticketId));
            filter.put("shopId", shopId);
            UserSvcPackTicket ticket = saleOrderService.getUserSvcPackTicketByFilter(filter);
            if (ticket == null) {
                result.data = false;
                result.type = Result.Type.warn;
                result.message = "套餐服务票不存在或未绑定当前门店";
                return result;
            }

            if (!doneCode.equals(ticket.getDoneCode())) {
                result.data = false;
                result.type = Result.Type.warn;
                result.message = "服务完成确认码不正确";
                return result;
            }

            UserSvcPackTicket userSvcPackTicket = new UserSvcPackTicket();
            userSvcPackTicket.setId(ticket.getId());
            userSvcPackTicket.setActorId(actorId);
            userSvcPackTicket.setActorName(actorName);
            userSvcPackTicket.setActRole("商户");
            userSvcPackTicket.setFinished(true);
            userSvcPackTicket.setInvalid(true);
            userSvcPackTicket.setFinishTime(new Date());

            result.data = saleOrderService.updateUserSvcPackTicket(userSvcPackTicket);
            if (result.data) {
                SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(userSvcPackTicket.getOrderNo());
                // 插入车辆服务记录
                if (saleOrder.getCarId() != null) {
                    UserCar userCar = carService.getUserCarById(saleOrder.getCarId());
                    if (userCar != null && saleOrder.getSvcPackId() != null) {
                        UserCarSvcRec userCarSvcRec = carService.getUserCarSvcRecByUserIdAndCarId(saleOrder.getUserId(), userCar.getId());
                        if (userCarSvcRec != null) {
                            String svcIds = userCarSvcRec.getSvcIds();
                            svcIds += "," + userSvcPackTicket.getSvcId();
                            userCarSvcRec.setSvcIds(svcIds);
                            String svcNames = userCarSvcRec.getSvcNames();
                            svcNames += "," + userSvcPackTicket.getSvcName();
                            userCarSvcRec.setSvcNames(svcNames);
                            carService.updateUserCarSvcRec(userCarSvcRec);
                        } else {
                            Date now = new Date();
                            userCarSvcRec = new UserCarSvcRec();
                            userCarSvcRec.setUserId(saleOrder.getUserId());
                            userCarSvcRec.setCarId(userCar.getId());
                            userCarSvcRec.setCarName(userCar.getName());
                            userCarSvcRec.setBrandId(userCar.getBrandId());
                            userCarSvcRec.setSerialId(userCar.getSerialId());
                            userCarSvcRec.setModelId(userCar.getModelId());
                            userCarSvcRec.setDateVal(now);
                            userCarSvcRec.setDateStr(DateUtil.toDateDirStr(new Date()));
                            userCarSvcRec.setOrderId(saleOrder.getId());
                            userCarSvcRec.setOrderNo(saleOrder.getNo());
                            userCarSvcRec.setShopId(saleOrder.getShopId());
                            userCarSvcRec.setShopName(saleOrder.getShopName());
                            userCarSvcRec.setSvcIds(userSvcPackTicket.getSvcId().toString());
                            userCarSvcRec.setSvcNames(userSvcPackTicket.getSvcName());
                            userCarSvcRec.setTs(new Date());
                            if(saleOrder.getDistFlag()){
                                userCarSvcRec.setDistFlag(saleOrder.getDistFlag());
                                userCarSvcRec.setDistShopName(saleOrder.getDistShopName());
                            }else{
                                userCarSvcRec.setDistFlag(false);
                                userCarSvcRec.setDistShopName(null);
                            }
                            carService.saveUserCarSvcRec(userCarSvcRec);
                        }

                        Integer svcTimes=saleOrder.getSvcTimes();
                        //更新服务次数
                        saleOrder=new SaleOrder();
                        saleOrder.setId(saleOrder.getId());
                        svcTimes+=1;
                        saleOrder.setSvcTimes(svcTimes);
                        saleOrderService.updateSaleOrder(saleOrder);
                    }
                }
                result.message = "服务套餐票确认完成";
            } else {
                result.message = "服务套餐票确认失败";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.data = false;
            result.type = Result.Type.error;
            result.message = "完成用户套餐服务票失败";
        }

        return result;
    }

    /**
     * 根据手机号判断用户是否存在，
     * 如果存在，则直接返回此用户的昵称或真实姓名，
     * 否则不存在，则创建此手机号的用户，返回结果为null，代表是新用户。
     *
     * @author 邓华锋
     * @date 2016年2月24日 下午5:27:10
     *
     * @param requestData
     * @return
     */
    @Remark("手机号码是否存在，是否进行创建用户")
    @RequestMapping(value = "/ensure/exist/by/phoneNo/-shop", method = RequestMethod.POST)
    @ResponseBody
    public Result<SaleOrderPo> getUserCreateExistByPhoneNo(@RequestBody MapContext requestData) {
        Result<SaleOrderPo> result = Result.newOne();
        SaleOrderPo context = new SaleOrderPo();
        String phoneNo = requestData.getTypedValue("phoneNo", String.class);
        if (phoneNo != null) {
            User user=userService.getUserByPhoneNo(phoneNo);
            if(user!=null){
                context.setUserId(user.getId());
                context.setUserName(user.getNickName());
                context.setUserPhone(user.getPhoneNo());
                context.setLinkMan(user.getRealName());
                context.setLinkNo(user.getPhoneNo());
                result.data =context;
            }else{
                Member member = new Member();
                User newUser = new User();
                newUser.setPassword(null);
                newUser.setNickName(phoneNo);
                newUser.setPhoneNo(phoneNo);
                newUser.setSecureLevel(0);
                member.setUser(newUser);
                member.setDisabled(false);
                member.setMemo("您是注册会员");
                if(memberService.saveMember(member)){
                    logger.warn("代理下单注册会员成功");
                    context.setUserId(newUser.getId());
                    context.setUserName(newUser.getPhoneNo());
                    context.setUserPhone(newUser.getPhoneNo());
                    context.setLinkMan(newUser.getPhoneNo());
                    context.setLinkNo(newUser.getPhoneNo());
                    result.data =context;
                }else{
                    logger.warn("代理下单注册会员失败");
                    result.data =null;
                }
            }
        }
        return result;
    }





}
