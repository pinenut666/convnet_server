package net.convnet.server.util;

import java.util.HashMap;

/**
 * 属性Map，这是一个HashMap的简单封装。
 *
 * @author Administrator
 * @date 2024/01/07
 */
public final class AttrMap extends HashMap<String, Object> {
   private static final long serialVersionUID = 52850833589542098L;

   public AttrMap() {
   }

   public AttrMap(String name, Object value) {
      this.put(name, value);
   }

   public AttrMap set(String name, Object value) {
      this.put(name, value);
      return this;
   }

   public AttrMap sets(HashMap<String, Object> attrs) {
      this.putAll(attrs);
      return this;
   }
}
