package net.convnet.server.support.attr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

public final class FastjsonConverter implements Converter {
   public static Converter INSTANCE = new FastjsonConverter();

   @Override
   public <T> T convert(Object value, Class<T> targetType) {
      if (value == null) {
         return null;
      } else {
         Class valueClass = value.getClass();
         if (targetType == valueClass) {
            return (T) value;
         } else if (targetType == Object.class) {
            return (T) value;
         } else if (targetType.isAssignableFrom(valueClass)) {
            return (T) value;
         } else if (targetType == String.class) {
            return valueClass == String.class ? (T) value : (T) JSON.toJSONString(value);
         } else {
            return value instanceof String ? JSON.parseObject((String)value, targetType) : TypeUtils.castToJavaBean(JSON.toJSON(value), targetType);
         }
      }
   }
}
