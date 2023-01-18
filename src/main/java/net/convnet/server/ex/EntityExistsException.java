package net.convnet.server.ex;

import java.io.Serializable;

public class EntityExistsException extends EntityException {
   private static final long serialVersionUID = 5977817365829480718L;

   public EntityExistsException(Class clazz, String msg, Throwable cause) {
      super((Throwable)cause, 52, clazz.getSimpleName(), msg);
   }

   public EntityExistsException(Class clazz, String msg) {
      this(clazz, msg, (Throwable)null);
   }

   public EntityExistsException(Class clazz, String key, Serializable value) {
      this(clazz, key + "=" + value);
   }
}
