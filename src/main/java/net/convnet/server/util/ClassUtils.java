package net.convnet.server.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ClassUtils {
   public static Class getGenericParameter(Class clazz, int index) {
      Type genType = clazz.getGenericSuperclass();
      if (genType instanceof ParameterizedType) {
         Type param = ((ParameterizedType)genType).getActualTypeArguments()[index];
         if (param instanceof Class) {
            return (Class)param;
         }
      }

      return null;
   }

   public static Class getGenericParameter0(Class clazz) {
      return getGenericParameter(clazz, 0);
   }

   public static <T> T newInstance(Class<T> clazz) {
      try {
         return clazz.newInstance();
      } catch (Exception var2) {
         throw new RuntimeException(var2);
      }
   }

   private ClassUtils() {
   }
}
