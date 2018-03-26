package priv.starfish.mall.manager.controller;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;



@Controller
@RequestMapping("login")
public class LoginController {
    Logger logger = Logger.getLogger(LoginController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request,
                          @RequestParam("userName") String userName,
                          @RequestParam("password") String password){

        //password = encrypt(userName,password);
        Subject currentUser = SecurityUtils.getSubject();
        //TODO: currentUser.isAuthenticated() 被改写问题
        //if (!currentUser.isAuthenticated()){
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        token.setRememberMe(true);
        try{

            System.out.println("1. " + token.hashCode());
            //可以login的话，说明认证通过
            currentUser.login(token);
            return "redirect:/index";
        }catch (UnknownAccountException uae) {
            logger.error("-----用户不存在-----"+uae.getMessage());
        } catch (IncorrectCredentialsException ice) {
            logger.error("-----密码不匹配-----"+ice.getMessage());
        }catch (AuthenticationException e){
            System.out.println("登录失败："+e.getMessage());
            logger.error(e.getMessage());
            e.printStackTrace();
            //token.clear();
        }
        return "common/404";

        // }
        // return "redirect:/index.do";
    }

    private static String encrypt(String username,String password) {
        String hashAlgorithmName = "MD5";
        Object salt = ByteSource.Util.bytes(username);
        int hashIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);
        System.out.println(result);
        return result.toString();
    }

    public static void main(String[] args) {
        encrypt("13100000000","222222");
    }
}
