package net.convnet.server.security;

import net.convnet.server.support.spring.ConfigurableInterceptor;
import net.convnet.server.util.RequestUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecContextInterceptor extends ConfigurableInterceptor {
   private String[] needLogins;
   private String redirectUrl;

   public void setRedirectUrl(String redirectUrl) {
      this.redirectUrl = redirectUrl;
   }

   public void setNeedLogins(String[] needLogins) {
      this.needLogins = needLogins;
   }

   public boolean internalPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      SecContext.getContext().set("userId", request.getSession().getAttribute("userId"));
      if (RequestUtils.matchAny(request, this.urlPathHelper, this.pathMatcher, this.needLogins) && Sec.getUserId() == null) {
         if (this.redirectUrl != null) {
            if (this.redirectUrl.startsWith("/") && !this.redirectUrl.startsWith(request.getContextPath())) {
               this.redirectUrl = request.getContextPath() + this.redirectUrl;
            }

            response.sendRedirect(this.redirectUrl + (this.redirectUrl.contains("?") ? "&" : "?") + "url=" + ServletUriComponentsBuilder.fromRequest(request).build().encode());
            return false;
         } else {
            throw new RuntimeException("Need login");
         }
      } else {
         return true;
      }
   }

   public void internalAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      SecContext.clearContext();
   }
}
