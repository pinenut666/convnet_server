package net.convnet.server.web;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public abstract class BaseController {
   protected void success(RedirectAttributes ra) {
      ra.addFlashAttribute("ret", true);
   }

   protected void failed(RedirectAttributes ra, String msg) {
      ra.addFlashAttribute("msg", msg);
      ra.addFlashAttribute("ret", false);
   }
}
