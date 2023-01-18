package net.convnet.server.ex;

public class ConvnetException extends RuntimeException implements ErrorCode {
   private static final long serialVersionUID = 4595549517180869921L;
   private int code = 1;
   private Object[] args;

   public ConvnetException() {
   }

   public ConvnetException(String message) {
      super(message);
   }

   public ConvnetException(Throwable cause) {
      super(cause);
   }

   public ConvnetException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConvnetException(int code, Object... args) {
      this.code = code;
      this.args = args;
   }

   public ConvnetException(String defaultMessage, int code, Object... args) {
      super(defaultMessage);
      this.code = code;
      this.args = args;
   }

   public ConvnetException(Throwable cause, int code, Object... args) {
      super(cause);
      this.code = code;
      this.args = args;
   }

   public ConvnetException(String defaultMessage, Throwable cause, int code, Object... args) {
      super(defaultMessage, cause);
      this.code = code;
      this.args = args;
   }

   public int getCode() {
      return this.code;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public String getMessage() {
      return ExceptionUtils.buildMessage(this.code, this.args, super.getMessage(), this.getCause());
   }

   public String toString() {
      return this.getMessage();
   }

   public static ConvnetException fromRoot(Exception e) {
      return new ConvnetException(ExceptionUtils.getRootCause(e));
   }
}
