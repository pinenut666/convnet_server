package net.convnet.server.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
   public static UrlPathHelper URL_PATH_HELPER = new UrlPathHelper() {
      public String getLookupPathForRequest(HttpServletRequest request) {
         String key = request.getRequestURI() + "_lookupPath";
         String path = (String)request.getAttribute(key);
         if (path == null) {
            request.setAttribute(key, path = super.getLookupPathForRequest(request));
         }

         return path;
      }
   };
   public static PathMatcher PATH_MATCHER = new AntPathMatcher();

   public static String getClientIP(HttpServletRequest request) {
      String xForwardedFor = StringUtils.trimToNull(request.getHeader("$wsra"));
      if (xForwardedFor != null) {
         return xForwardedFor;
      } else {
         xForwardedFor = StringUtils.trimToNull(request.getHeader("X-Real-IP"));
         if (xForwardedFor != null) {
            return xForwardedFor;
         } else {
            xForwardedFor = StringUtils.trimToNull(request.getHeader("X-Forwarded-For"));
            if (xForwardedFor != null) {
               int spaceIndex = xForwardedFor.indexOf(44);
               return spaceIndex > 0 ? xForwardedFor.substring(0, spaceIndex) : xForwardedFor;
            } else {
               return request.getRemoteAddr();
            }
         }
      }
   }

   public static String getDomain(HttpServletRequest request) {
      StringBuffer url = request.getRequestURL();
      int end = url.indexOf(".");
      if (end == -1) {
         return "";
      } else {
         int start = url.indexOf("//");
         return url.substring(start + 2, end);
      }
   }

   public static boolean isPost(HttpServletRequest request) {
      return "POST".equals(request.getMethod());
   }

   public static boolean matchAny(HttpServletRequest request, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, String[] patterns) {
      if (ArrayUtils.isNotEmpty(patterns)) {
         String lookupPath = urlPathHelper.getLookupPathForRequest(request);
         String[] arr$ = patterns;
         int len$ = patterns.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String pattern = arr$[i$];
            if (pathMatcher.match(pattern, lookupPath)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean matchAll(HttpServletRequest request, UrlPathHelper urlPathHelper, PathMatcher pathMatcher, String[] patterns) {
      if (ArrayUtils.isNotEmpty(patterns)) {
         String lookupPath = urlPathHelper.getLookupPathForRequest(request);
         String[] arr$ = patterns;
         int len$ = patterns.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String pattern = arr$[i$];
            if (!pathMatcher.match(pattern, lookupPath)) {
               return false;
            }
         }
      }

      return true;
   }

   public static Boolean getBool(HttpServletRequest request, String name) {
      String value = request.getParameter(name);
      return StringUtils.isEmpty(value) ? null : BooleanUtils.toBooleanObject(value);
   }

   public static boolean getBool(HttpServletRequest request, String name, boolean def) {
      Boolean value = getBool(request, name);
      return value == null ? def : value;
   }

   public static Integer getInt(HttpServletRequest request, String name) {
      String value = request.getParameter(name);
      return StringUtils.isEmpty(value) ? null : NumberUtils.createInteger(value);
   }

   public static int getInt(HttpServletRequest request, String name, int def) {
      Integer value = getInt(request, name);
      return value == null ? def : value;
   }

   public static Double getDouble(HttpServletRequest request, String name) {
      String value = request.getParameter(name);
      return StringUtils.isEmpty(value) ? null : NumberUtils.createDouble(value);
   }

   public static double getDouble(HttpServletRequest request, String name, double def) {
      Double value = getDouble(request, name);
      return value == null ? def : value;
   }
}
