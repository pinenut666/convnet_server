package net.convnet.server;

import io.netty.util.AttributeKey;
import org.apache.commons.lang3.time.FastDateFormat;

import java.nio.charset.Charset;
import java.text.Format;

public final class Constants {
   public static final String DEFAULT_CHARSET = "UTF-8";
   public static final Charset CHARSET = Charset.forName("UTF-8");
   public static final Format DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
   public static final Format DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
   public static final String USER_ID = "userId";
   public static AttributeKey<Long> READ_BYTES_KEY = AttributeKey.newInstance("READ_BYTES");
   public static AttributeKey<Long> WRITE_BYTES_KEY = AttributeKey.newInstance("WRITE_BYTES");

   private Constants() {
   }
}
