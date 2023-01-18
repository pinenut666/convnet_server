package net.convnet.server.protocol;

import java.util.Map;

public interface ResponseReader {
   int getVersion();

   Cmd getCmd();

   boolean isSuccess();

   void setOutput(boolean var1);

   boolean needOutput();

   <T> T getAttr(String var1);

   Map<String, Object> getAttrs();
}
