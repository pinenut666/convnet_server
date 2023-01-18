package net.convnet.server.web;

import net.convnet.server.email.Email;
import net.convnet.server.email.EmailSender;
import net.convnet.server.identity.ResetCodeManager;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.ResetCode;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Controller
public class IndexController {
   @Autowired
   private UserManager userManager;
   @Autowired
   private SessionManager sessionManager;
   @Autowired
   private ResetCodeManager resetCodeManager;
   @Autowired
   private EmailSender emailSender;
   @Value("#{props['reset.url']}")
   private String resetUrl;
   private Set<String> adminNames = new HashSet();
   @Value("#{props.listen}")
   private String listen;
   @Value("#{props.allowregist}")
   private boolean allowregist;
   @Value("#{props['maxRigistCount']}")
   private int maxRigistCount;
   @Value("#{props['forceUseMailCheck']}")
   private boolean forceUseMailCheck;
   @Value("#{props['defaultPass']}")
   private String defaultPass;
   @Value("#{props.canCreateGroup}")
   private boolean canCreateGroup;
   @Value("#{props.canJoinGroup}")
   private boolean canJoinGroup;

   @Value("#{props['admin.names']}")
   public void setAdminNames(String[] adminNames) {
      Collections.addAll(this.adminNames, adminNames);
   }

   @RequestMapping(
      value = {"/login"},
      method = {RequestMethod.GET}
   )
   public String login(Model model) throws Exception {
      model.addAttribute("listen", this.listen);
      model.addAttribute("sessionCount", this.sessionManager.getSessions().size());
      return "login";
   }

   @RequestMapping(
      value = {"/login"},
      method = {RequestMethod.POST}
   )
   public String doLogin(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam(value = "url",required = false) String url, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
      try {
         User user = this.userManager.validateUser(name, password);
         if (user == null || !this.adminNames.contains(user.getName())) {
            throw new RuntimeException("用户名或密码错误,或者不是管理员");
         }

         request.getSession().setAttribute("userId", user.getId());
         if (StringUtils.isEmpty(url)) {
            url = request.getContextPath();
         }

         response.sendRedirect(url);
      } catch (Exception var8) {
         model.addAttribute("msg", var8.getMessage());
      }

      model.addAttribute("listen", this.listen);
      model.addAttribute("sessionCount", this.sessionManager.getSessions().size());
      return "login";
   }

   @RequestMapping(
      value = {"/logout"},
      method = {RequestMethod.GET}
   )
   public String logout(HttpServletRequest request) throws Exception {
      request.getSession().invalidate();
      return "redirect:/login";
   }

   @RequestMapping(
      value = {"/forget"},
      method = {RequestMethod.GET}
   )
   public String forget() throws Exception {
      return "/forget";
   }

   @RequestMapping(
      value = {"/forget"},
      method = {RequestMethod.POST}
   )
   public String doForget(@RequestParam("name") String name, Model model) throws Exception {
      User user = this.userManager.getUserByName(name);
      if (user != null) {
         ResetCode resetCode = this.resetCodeManager.createResetCode(user);
         Email e = new Email();
         e.setTo(new String[]{name});
         e.setSubject("ConVNet 密码重置");
         String url = this.resetUrl + resetCode.getId();
         e.setBody("用户: " + user.getName() + ",请点击 <a href=" + url + " target=\"_blank\">" + url + "</a> 重置Convnet密码");
         this.emailSender.send(e);
         model.addAttribute("msg", "已发送密码重置链接至 " + name + " 请查收");
      } else {
         model.addAttribute("msg", "用户不存在或者邮箱错误");
      }

      return "forget";
   }

   @RequestMapping(
      value = {"/regist"},
      method = {RequestMethod.GET}
   )
   public String regist(Model model) throws Exception {
      if (!this.allowregist) {
         model.addAttribute("msg", "服务器禁止注册");
      }

      return "/regist";
   }

   public static String getIpAddr(HttpServletRequest request) {
      String ip = request.getHeader("x-forwarded-for");
      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getHeader("Proxy-Client-IP");
      }

      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getHeader("WL-Proxy-Client-IP");
      }

      if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getRemoteAddr();
         if (ip.equals("127.0.0.1")) {
            InetAddress inet = null;

            try {
               inet = InetAddress.getLocalHost();
            } catch (UnknownHostException var4) {
               var4.printStackTrace();
            }

            ip = inet.getHostAddress();
         }
      }

      if (ip != null && ip.length() > 15 && ip.indexOf(",") > 0) {
         ip = ip.substring(0, ip.indexOf(","));
      }

      return ip;
   }

   @RequestMapping(
      value = {"/regist"},
      method = {RequestMethod.POST}
   )
   public String doRegist(@RequestParam("name") String name, Model model, HttpServletRequest request) throws Exception {
      if (this.allowregist) {
         User user = this.userManager.getUserByName(name);
         if (user != null) {
            model.addAttribute("msg", "用户已经存在 您可以尝试<a href= 'forget'>取回密码</a>");
            return "/regist";
         }

         String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
         if (!name.matches(EMAIL_REGEX)) {
            model.addAttribute("msg", "必须使用邮箱进行注册");
            return "/regist";
         }

         String ip = getIpAddr(request);
         if (this.maxRigistCount > 0) {
            int count = this.userManager.getTodayRegistUserCountFromIp(ip);
            if (count >= this.maxRigistCount) {
               model.addAttribute("msg", "注册失败，超过IP最大限制");
               return "/regist";
            }
         }

         user = new User();
         user.setName(name);
         user.setNickName(name);
         if (this.forceUseMailCheck) {
            user.setPassword("wait　ForMail　Confirm");
         } else {
            user.setPassword(this.defaultPass);
         }

         user.setDescription("备注");
         user.setRegisterIp(ip);
         user.setCanAddfriend(this.canJoinGroup);
         user.setCanCreateGroup(this.canCreateGroup);
         this.userManager.saveUser(user);
         if (this.forceUseMailCheck) {
            ResetCode resetCode = this.resetCodeManager.createResetCode(user);
            Email e = new Email();
            e.setTo(new String[]{name});
            e.setSubject("ConVNet 注册确认设置");
            String url = this.resetUrl + resetCode.getId();
            e.setBody("用户: " + user.getName() + ",请点击 <a href=" + url + " target=\"_blank\">" + url + "</a> 设置Convnet密码");
            this.emailSender.send(e);
            model.addAttribute("msg", "确认信件请已经发往邮箱，请注意查收");
         } else {
            model.addAttribute("msg", "账号已创建，默认密码:‘" + this.defaultPass + "’，请尽快登录修改");
         }
      } else {
         model.addAttribute("msg", "服务器暂时禁止注册");
      }

      return "regist";
   }

   @RequestMapping(
      value = {"/reset"},
      method = {RequestMethod.GET}
   )
   public String reset(@RequestParam(value = "code",required = false) String code, Model model) throws Exception {
      User user = this.resetCodeManager.getUserByResetCode(code);
      if (user != null) {
         model.addAttribute("user", user);
         return "/reset";
      } else {
         model.addAttribute("msg", "无法找到用户信息");
         return "/reset_msgonly";
      }
   }

   @RequestMapping(
      value = {"/reset"},
      method = {RequestMethod.POST}
   )
   public String doReset(@RequestParam("code") String code, @RequestParam("password") String password, @RequestParam("password1") String password1, Model model) throws Exception {
      User user = this.resetCodeManager.getUserByResetCode(code);
      if (user != null && password.equals(password1)) {
         this.userManager.updatePassword(user.getId(), password);
         this.resetCodeManager.removeResetCode(code);
         model.addAttribute("user", user);
         model.addAttribute("msg", "重置密码成功");
         return "reset_msgonly";
      } else {
         model.addAttribute("user", user);
         if (user == null) {
            model.addAttribute("msg", "不存在本次重置请求");
            return "reset_msgonly";
         } else {
            model.addAttribute("msg", "两次输入的密码不一致");
            return "reset";
         }
      }
   }
}
