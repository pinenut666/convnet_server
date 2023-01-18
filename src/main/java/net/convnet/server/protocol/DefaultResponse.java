package net.convnet.server.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class DefaultResponse implements Response, Serializable {
   private static final long serialVersionUID = 3092376853691143104L;
   private final Map<String, Object> attrs = new HashMap();
   private final int version;
   private Cmd cmd;
   private boolean success = true;
   private boolean needOutput = true;

   public DefaultResponse(int version, Cmd cmd) {
      this.version = version;
      this.cmd = cmd;
   }

   public int getVersion() {
      return this.version;
   }

   public Cmd getCmd() {
      return this.cmd;
   }

   public void setCmd(Cmd cmd) {
      this.cmd = cmd;
   }

   public boolean isSuccess() {
      return this.success;
   }

   public boolean needOutput() {
      return this.needOutput;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public void setOutput(boolean needOutput) {
      this.needOutput = needOutput;
   }

   public Response setAttr(String name, Object value) {
      this.attrs.put(name, value);
      return this;
   }

   public Object getAttr(String name) {
      return this.attrs.get(name);
   }

   public Map<String, Object> getAttrs() {
      return this.attrs;
   }

   public Response setAttrs(Map<String, Object> attrs) {
      this.attrs.putAll(attrs);
      return this;
   }
}
