package net.convnet.server.ex;

public class EntityException extends ConvnetException {
   private static final long serialVersionUID = -4686633812095775678L;

   public EntityException(Class clazz, String msg, Throwable cause) {
      super((Throwable)cause, 50, clazz.getSimpleName(), msg);
   }

   public EntityException(Class clazz, String msg) {
      this(clazz, msg, (Throwable)null);
   }

   public EntityException(Class clazz, Throwable cause) {
      this(clazz, (String)null, cause);
   }

   public EntityException(int code, Object... args) {
      super(code, args);
   }

   public EntityException(Throwable cause, int code, Object... args) {
      super(cause, code, args);
   }

   public EntityException(String defaultMessage, int code, Object... args) {
      super(defaultMessage, code, args);
   }

   public EntityException(String defaultMessage, Throwable cause, int code, Object... args) {
      super(defaultMessage, cause, code, args);
   }
}
