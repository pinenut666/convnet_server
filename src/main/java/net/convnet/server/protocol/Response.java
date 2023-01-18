package net.convnet.server.protocol;

import java.util.Map;

public interface Response extends ResponseReader {
   void setCmd(Cmd var1);

   void setSuccess(boolean var1);

   Response setAttr(String var1, Object var2);

   Response setAttrs(Map<String, Object> var1);
}
