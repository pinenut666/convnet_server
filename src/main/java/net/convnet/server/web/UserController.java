/*
package net.convnet.server.web;

import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"user"})
public class UserController {
   @Autowired
   private UserManager userManager;
   @Autowired
   private SessionManager sessionManager;

   @ModelAttribute("user")
   public User getFile(@RequestParam(value = "id",required = false) Integer id) throws Exception {
      return id == null ? new User() : this.userManager.getUser(id);
   }

   @RequestMapping(
      method = {RequestMethod.GET}
   )
   public String index(Model model, @RequestParam(value = "isonline",required = false) Boolean isonline, @RequestParam(value = "name",required = false) String name) throws Exception {
      if (isonline == null) {
         isonline = false;
      }
      //TODO：实现该部分
     // Page<User> page = this.userManager.findUser(name, request, isonline);
      model.addAttribute("name", name);
      model.addAttribute("isonline", isonline);
    //  model.addAttribute("page", page);
      return "user/index";
   }

   @RequestMapping(
      value = {"delete"},
      method = {RequestMethod.GET}
   )
   public String delete(@RequestParam("id") int id) throws Exception {
      this.userManager.removeUser(id);
      return "redirect:/user";
   }

   @RequestMapping(
      value = {"edit"},
      method = {RequestMethod.GET}
   )
   public String edit() throws Exception {
      return "user/edit";
   }

   @RequestMapping(
      value = {"edit"},
      method = {RequestMethod.POST}
   )
   public String save(@ModelAttribute("user") User user, RedirectAttributes ra) throws Exception {
      try {
         if (StringUtils.isNotBlank(user.getPassword())) {
            this.userManager.setPassword(user, user.getPassword());
         }

         this.userManager.saveUser(user);
      } catch (Exception var4) {
         this.failed(ra, var4.getMessage());
      }

      this.success(ra);
      return "redirect:/user";
   }

   @RequestMapping(
      value = {"sendmessage"},
      method = {RequestMethod.GET}
   )
   public String sendmessage(Model model, @RequestParam("id") int id, RedirectAttributes ra) throws Exception {
      model.addAttribute("user", this.userManager.getUser(id));
      return "user/sendmessage";
   }

   @RequestMapping(
      value = {"serverban"},
      method = {RequestMethod.GET}
   )
   public String serverban(Model model, @RequestParam("id") int id, RedirectAttributes ra) throws Exception {
      Session session = this.sessionManager.getSession(id);
      if (session != null) {
         session.destory();
      }

      return "redirect:/user";
   }

   @RequestMapping(
      value = {"sendmessagetouser"},
      method = {RequestMethod.POST}
   )
   public String sendmessagetouser(Model model, @RequestParam("id") int id, @RequestParam("message") String message, RedirectAttributes ra) throws Exception {
      model.addAttribute("id", id);
      if (this.sessionManager.sendMessageToUser(this.userManager.getUser(id), message)) {
         model.addAttribute("msg", message + "发送成功");
      } else {
         model.addAttribute("msg", message + "发送失败");
      }

      return "user/sendmessage";
   }
}
*/
