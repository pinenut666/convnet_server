package net.convnet.server.ex;

import java.io.Serializable;

public class EntityNotFoundException extends EntityException {
   private static final long serialVersionUID = 5977817365829480718L;

   public EntityNotFoundException(Class clazz, String msg, Throwable cause) {
      super((Throwable)cause, 51, clazz.getSimpleName(), msg);
   }

   public EntityNotFoundException(Class clazz, String msg) {
      this(clazz, msg, (Throwable)null);
   }

   public EntityNotFoundException(Class clazz, String key, Serializable value) {
      this(clazz, key + "=" + value);
   }
}
