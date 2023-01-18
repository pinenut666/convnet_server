package net.convnet.server.protocol;

import java.util.Map;

public interface RequestBuilder {
   RequestBuilder set(String var1, Object var2);

   RequestBuilder set(Map<String, Object> var1);
}
