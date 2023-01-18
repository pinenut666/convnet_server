package net.convnet.server.util;

import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.memory.InMemoryContent;
import org.sitemesh.webapp.WebAppContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SitemeshHelper {
   public static void extractMeta(PageContext context) {
      Content content = (Content)context.getRequest().getAttribute(WebAppContext.CONTENT_KEY);
      if (content == null) {
         content = new InMemoryContent();
      }

      ContentProperty cp = ((Content)content).getExtractedProperties();
      context.setAttribute("_body", ((ContentProperty)cp.getChild("body")).getValue());
      context.setAttribute("_title", ((ContentProperty)cp.getChild("title")).getValue());
      context.setAttribute("_head", ((ContentProperty)cp.getChild("head")).getValue());
      context.setAttribute("_body", ((ContentProperty)cp.getChild("body")).getValue());
      Map<String, String> metaMap = new HashMap();
      Iterator i$ = ((ContentProperty)cp.getChild("meta")).getChildren().iterator();

      while(i$.hasNext()) {
         ContentProperty cp1 = (ContentProperty)i$.next();
         metaMap.put(cp1.getName(), cp1.getValue());
      }

      context.setAttribute("_meta", metaMap);
      context.setAttribute("ctx", ((HttpServletRequest)context.getRequest()).getContextPath());
   }
}
