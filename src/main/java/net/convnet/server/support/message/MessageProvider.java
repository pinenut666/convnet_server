package net.convnet.server.support.message;

import java.util.Locale;

public interface MessageProvider {
   String getMessage(String var1);

   String getMessage(String var1, Object[] var2);

   String getMessage(String var1, Object[] var2, String var3);

   String getMessage(String var1, Object[] var2, String var3, Locale var4);
}
