package net.convnet.server.support.spring;

import net.convnet.server.util.RequestUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConfigurableInterceptor implements HandlerInterceptor {
   private final String CACHE_KEY = "hi_" + this.hashCode() + "_";
   private String[] excludes;
   private String[] includes;
   protected UrlPathHelper urlPathHelper;
   protected PathMatcher pathMatcher;

   public ConfigurableInterceptor() {
      this.urlPathHelper = RequestUtils.URL_PATH_HELPER;
      this.pathMatcher = RequestUtils.PATH_MATCHER;
   }

   public void setExcludes(String[] excludes) {
      this.excludes = excludes;
   }

   public void setIncludes(String[] includes) {
      this.includes = includes;
   }

   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
      this.urlPathHelper = urlPathHelper;
   }

   public void setPathMatcher(PathMatcher pathMatcher) {
      this.pathMatcher = pathMatcher;
   }

   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      return !this.needProcess(request) || this.internalPreHandle(request, response, handler);
   }

   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
      if (this.needProcess(request)) {
         this.internalPostHandle(request, response, handler, modelAndView);
      }

   }

   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      if (this.needProcess(request)) {
         this.internalAfterCompletion(request, response, handler, ex);
      }

      request.removeAttribute(this.getCacheKey(request));
   }

   public boolean internalPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      return true;
   }

   public void internalPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
   }

   public void internalAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
   }

   private boolean needProcess(HttpServletRequest request) {
      String key = this.getCacheKey(request);
      Boolean need = (Boolean)request.getAttribute(key);
      if (need != null) {
         return need;
      } else {
         need = !RequestUtils.matchAny(request, this.urlPathHelper, this.pathMatcher, this.excludes);
         if (need) {
            need = this.includes == null || RequestUtils.matchAny(request, this.urlPathHelper, this.pathMatcher, this.includes);
         }

         request.setAttribute(key, need);
         return need;
      }
   }

   private String getCacheKey(HttpServletRequest request) {
      return this.CACHE_KEY + request.getRequestURI();
   }
}
