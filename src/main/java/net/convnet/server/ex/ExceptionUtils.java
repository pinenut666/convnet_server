package net.convnet.server.ex;

import net.convnet.server.support.message.NLS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedRuntimeException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

public class ExceptionUtils {
   private ExceptionUtils() {
   }

   public static Throwable getRootCause(Throwable cause) {
      Throwable rootCause;
      for(rootCause = null; cause != null && cause != rootCause; cause = cause.getCause()) {
         rootCause = cause;
      }

      return rootCause;
   }

   public static String buildMessage(int code, Object[] args, String defaultMessage, Throwable cause) {
      String message = null;
      if (code != 1) {
         message = NLS.getMessage("error." + code, args, null);
      }

      if (StringUtils.isNotEmpty(defaultMessage)) {
         message = message == null ? defaultMessage : message + "; " + defaultMessage;
      }

      if (message == null) {
         message = "errorcode:" + code;
      }

      return cause != null ? buildMessage(message, cause) : message;
   }

   public static String buildMessage(String message, Throwable cause) {
      StringBuilder sb = new StringBuilder(message);
      Set<Throwable> visitedExceptions = new HashSet<>();
      Throwable tmpEx = cause;

      do {
         if (sb.length() > 0) {
            sb.append(" -> ");
         }

         sb.append(cause);
         visitedExceptions.add(tmpEx);
         tmpEx = tmpEx.getCause();
      } while(tmpEx != null && !visitedExceptions.contains(tmpEx) && !(tmpEx instanceof ConvnetException) && !(tmpEx instanceof NestedRuntimeException));

      return sb.toString();
   }

   public static String buildMessage(Throwable ex) {
      return buildMessage("", ex);
   }

   public static String buildStackTrace(Throwable cause) {
      StringWriter sw = new StringWriter();
      cause.printStackTrace(new PrintWriter(sw));
      return sw.toString();
   }
}
