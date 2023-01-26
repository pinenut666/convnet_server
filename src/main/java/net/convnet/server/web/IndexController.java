package net.convnet.server.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import net.convnet.server.common.result.CommonResult;
import net.convnet.server.email.EmailSender;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class IndexController {

   @Autowired
   private UserManager userManager;
   @Autowired
   private EmailSender emailSender;
   //验证码功能
   @GetMapping("captcha.jpg")
   public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
      response.setHeader("Cache-Control", "no-store, no-cache");
      response.setContentType("image/jpeg");
      // HUTOOL生成验证码
      LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
      request.getSession().setAttribute("KAPTCHA_SESSION_KEY", lineCaptcha.getCode());
      ServletOutputStream out = response.getOutputStream();
      lineCaptcha.write(out);
      out.close();
   }
   //用户登录
   @PostMapping("login")
   public CommonResult<Object> userlogin(@RequestBody LoginBean loginBean, HttpServletRequest request) {
      String username = loginBean.getName();
      String password = loginBean.getPassword();
      String captcha = loginBean.getCaptcha();
      Object kaptcha = request.getSession().getAttribute("KAPTCHA_SESSION_KEY");
      if(kaptcha == null){
         return CommonResult.error("验证码已失效");
      }
      if(!captcha.equals(kaptcha)){
         return CommonResult.error("验证码不正确");
      }
      User user = this.userManager.validateUser(username, password);
      //TODO:更合理的管理员
      if (user == null) {
         return CommonResult.error("用户名或密码错误,或者不是管理员");
      }
      StpUtil.login(user.getId());
      return CommonResult.success("登录成功");
   }
   //用户注册
   public CommonResult<Object> userRegist(RegisterBean registerBean){
      User user = this.userManager.getUserByName(registerBean.getName());
      if (user != null) {
         return CommonResult.error("用户名已存在，建议找回密码。");
      }
      return null;
   }



}