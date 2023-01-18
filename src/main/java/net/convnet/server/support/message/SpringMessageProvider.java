package net.convnet.server.support.message;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
@Service
public class SpringMessageProvider implements MessageProvider {
   private MessageSource messageSource;

   public void setMessageSource(MessageSource messageSource) {
      this.messageSource = messageSource;
   }

   public String getMessage(String key) {
      return this.getMessage(key, (Object[])null, (String)null, (Locale)null);
   }

   public String getMessage(String key, Object[] args) {
      return this.getMessage(key, args, (String)null, (Locale)null);
   }

   public String getMessage(String key, Object[] args, String defaultMessage) {
      return this.getMessage(key, args, defaultMessage, (Locale)null);
   }

   public String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
      return this.messageSource.getMessage(key, args, defaultMessage, locale);
   }
}
