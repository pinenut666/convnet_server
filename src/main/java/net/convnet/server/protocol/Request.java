package net.convnet.server.protocol;

public interface Request {
   int getVersion();

   Cmd getCmd();

   boolean hasAttr(String var1);

   Object getAttr(String var1);

   Request setAttr(String var1, Object var2);

   boolean hasParam(String var1);

   String getParam(String var1);

   String getParam(String var1, String var2);

   Object getRawParam(String var1);

   String getRequiredParam(String var1) throws IllegalStateException;

   String[] getParams(String var1);

   Integer getIntParam(String var1);

   int getRequiredIntParam(String var1) throws IllegalStateException;

   int getIntParam(String var1, int var2);

   <T> T getParam(String var1, Class<T> var2);

   <T> T getParam(String var1, Class<T> var2, T var3);

   <T> T getRequiredParam(String var1, Class<T> var2) throws IllegalStateException;

   String[] getParamNames();
}
