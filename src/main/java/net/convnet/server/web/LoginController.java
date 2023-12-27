package net.convnet.server.web;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.Validator;
import cn.hutool.extra.servlet.ServletUtil;
import net.convnet.server.common.result.CommonResult;
import net.convnet.server.email.Email;
import net.convnet.server.email.EmailSender;
import net.convnet.server.identity.ResetCodeManager;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.ResetCode;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.web.pojo.LoginBean;
import net.convnet.server.web.pojo.RegisterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {

   @Autowired
   private UserManager userManager;
   @Autowired
   private EmailSender emailSender;
   @Autowired
   private ResetCodeManager resetCodeManager;
   @Value("${props.reset.url}")
   private String resetUrl;
   @Value("${props.listen}")
   private String listen;
   @Value("${props.allowregist}")
   private boolean allowregist;
   @Value("${props.maxRigistCount}")
   private int maxRigistCount;
   @Value("${props.forceUseMailCheck}")
   private boolean forceUseMailCheck;
   @Value("${props.defaultPass}")
   private String defaultPass;
   @Value("${props.canCreateGroup}")
   private boolean canCreateGroup;
   @Value("${props.canJoinGroup}")
   private boolean canJoinGroup;

   //验证码功能
   @GetMapping("captcha.jpg")
   public void captcha(HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {
      response.setHeader("Cache-Control", "no-store, no-cache");
      response.setContentType("image/jpeg");
      // HUTOOL生成验证码
      LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 50);
      request.getSession().setAttribute("KAPTCHA_SESSION_KEY", lineCaptcha.getCode());
      ServletOutputStream out = response.getOutputStream();
      lineCaptcha.write(out);
      out.close();
   }
   //管理员登录
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
      StpUtil.login(user.getId());
      if (!StpUtil.hasRole("admin")) {
         StpUtil.logout();
         return CommonResult.error("用户名或密码错误,或者不是管理员");
      }
      return CommonResult.success("登录成功");
   }
   //用户注册
   @PostMapping("/regist")
   public CommonResult<Object> userRegist(RegisterBean registerBean, HttpServletRequest request){
      if(!allowregist)
      {
         return CommonResult.error("不允许注册");
      }
      User user = this.userManager.getUserByName(registerBean.getName());
      //判断是否拥有该用户
      if (user != null) {
         return CommonResult.error("用户名已存在，建议找回密码。");
      }
      //判断是否为邮箱注册
      if(!Validator.isEmail(registerBean.getEmail()))
      {
          CommonResult.error("邮箱不合法。请使用邮箱注册。");
      }
      String ip = ServletUtil.getClientIP(request);
      if (this.maxRigistCount > 0) {
         int count = this.userManager.getTodayRegistUserCountFromIp(ip);
         if (count >= this.maxRigistCount) {
            return CommonResult.error("超过IP的最大注册上限");
         }
      }
      //设置注册用户的相关信息
      user = new User();
      user.setName(registerBean.getName());
      user.setNickName(registerBean.getName());
      if (this.forceUseMailCheck) {
         user.setPassword("wait　ForMail　Confirm");
      } else {
         user.setPassword(registerBean.getPassword());
      }
      user.setDescription("备注");
      user.setRegisterIp(ip);
      user.setCanAddfriend(this.canJoinGroup);
      user.setCanCreateGroup(this.canCreateGroup);
      this.userManager.saveUser(user);
      //我猜测这里做成直接修改密码的原因是兼容客户端登录?
      if (this.forceUseMailCheck) {
         ResetCode resetCode = resetCodeManager.createResetCode(user);
         Email e = new Email();
         e.setTo(new String[]{registerBean.getName()});
         e.setSubject("ConVNet 注册确认设置");
         String url = this.resetUrl + resetCode.getId();
         e.setBody("用户: " + user.getName() + ",请点击 <a href=" + url + " target=\"_blank\">" + url + "</a> 修改密码。");
         this.emailSender.send(e);
         return CommonResult.success("请查收邮箱确认验证码");
      } else {
        return CommonResult.success("注册成功");
      }
   }

   //用户重置密码
   @GetMapping("/reset")
   public CommonResult<Object> reset(String code, String password) throws Exception {
      User user = this.resetCodeManager.getUserByResetCode(code);
      if (user != null) {
         //修改用户的密码
         user.setPassword(password);
         this.userManager.saveUser(user);
         return CommonResult.success("用户密码修改成功，现在可以登录了");
      } else {
         return CommonResult.error("验证码无效");
      }
   }

}