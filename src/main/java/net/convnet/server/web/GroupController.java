package net.convnet.server.web;

import net.convnet.server.identity.GroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"group"})
public class GroupController {
   @Autowired
   private GroupManager groupManager;

   @RequestMapping(
      method = {RequestMethod.GET}
   )
   public String index(Model model) throws Exception {
      model.addAttribute("groups", this.groupManager.getAllGroup());
      return "group/index";
   }

   @RequestMapping(
      value = {"delete"},
      method = {RequestMethod.GET}
   )
   public String delete(@RequestParam("id") int id) throws Exception {
      this.groupManager.removeGroup(id);
      return "redirect:/group";
   }
}
