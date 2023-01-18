package net.convnet.server.support.attr;

import java.util.Collection;
import java.util.Map;

public interface Attrable {
   boolean hasAttr(String var1);

   String getAttr(String var1);

   String getAttr(String var1, String var2);

   String getRequiredAttr(String var1) throws IllegalStateException;

   String resolvePlaceholders(String var1);

   String[] getArrayAttr(String var1);

   <T> T getAttr(String var1, Class<T> var2);

   <T> T getAttr(String var1, Class<T> var2, T var3);

   <T> T getRequiredAttr(String var1, Class<T> var2) throws IllegalStateException;

   String[] getKeys();

   Map<String, String> getAttrs(String... var1);

   Map<String, String> getAttrs(Collection<String> var1);

   void setAttr(String var1, Object var2);

   void setAttrs(Map<String, ?> var1);

   void removeAttr(String var1);
}
