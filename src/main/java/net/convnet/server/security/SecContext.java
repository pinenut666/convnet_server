package net.convnet.server.security;

import java.util.HashMap;
import java.util.Map;

public final class SecContext {
   private static ThreadLocal<SecContext> LOCAL = new InheritableThreadLocal<SecContext>() {
      @Override
      protected SecContext initialValue() {
         return new SecContext();
      }
   };
   private final Map<String, Object> values;

   private SecContext() {
      this.values = new HashMap();
   }

   public static SecContext getContext() {
      return (SecContext)LOCAL.get();
   }

   public static void clearContext() {
      LOCAL.remove();
   }

   public SecContext set(String key, Object value) {
      if (value == null) {
         this.values.remove(key);
      } else {
         this.values.put(key, value);
      }

      return this;
   }

   public SecContext remove(String key) {
      this.values.remove(key);
      return this;
   }

   public Object get(String key) {
      return this.values.get(key);
   }

   // $FF: synthetic method
   SecContext(Object x0) {
      this();
   }
}
