package net.convnet.server.protocol;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class DefaultRequest implements Request, RequestBuilder, Serializable {
   private static final long serialVersionUID = 3348082539153902075L;
   private final Map<String, Object> attrs = new HashMap();
   private final JSONObject params = new JSONObject();
   private final int version;
   private final Cmd cmd;

   public DefaultRequest(Cmd cmd, int version) {
      this.cmd = cmd;
      this.version = version;
   }

   public int getVersion() {
      return this.version;
   }

   public Cmd getCmd() {
      return this.cmd;
   }

   public RequestBuilder set(String name, Object value) {
      this.params.put(name, value);
      return this;
   }

   public RequestBuilder set(Map<String, Object> map) {
      this.params.putAll(map);
      return this;
   }

   public boolean hasAttr(String name) {
      return this.attrs.containsKey(name);
   }

   public Object getAttr(String name) {
      return this.attrs.get(name);
   }

   public Request setAttr(String name, Object value) {
      this.attrs.put(name, value);
      return this;
   }

   public boolean hasParam(String name) {
      return this.params.containsKey(name);
   }

   public String getParam(String name) {
      return this.params.getString(name);
   }

   public Object getRawParam(String name) {
      return this.params.get(name);
   }

   public String getParam(String name, String defaultValue) {
      return (String)def(this.getParam(name), defaultValue);
   }

   public String getRequiredParam(String name) throws IllegalStateException {
      return (String)required(name, this.getParam(name));
   }

   public String[] getParams(String name) {
      return (String[])this.getParam(name, String[].class);
   }

   public Integer getIntParam(String name) {
      return this.params.getInteger(name);
   }

   public int getRequiredIntParam(String name) throws IllegalStateException {
      return (Integer)required(name, this.getIntParam(name));
   }

   public int getIntParam(String name, int defaultValue) {
      return (Integer)def(this.getIntParam(name), defaultValue);
   }

   public <T> T getParam(String name, Class<T> targetType) {
      return this.params.getObject(name, targetType);
   }

   public <T> T getParam(String name, Class<T> targetType, T defaultValue) {
      return def(this.getParam(name, targetType), defaultValue);
   }

   public <T> T getRequiredParam(String name, Class<T> targetType) throws IllegalStateException {
      return required(name, this.getParam(name, targetType));
   }

   public String[] getParamNames() {
      return (String[])this.params.keySet().toArray(new String[this.params.size()]);
   }

   private static <T> T def(T value, T defaultValue) {
      return value == null ? defaultValue : value;
   }

   private static <T> T required(String name, T value) {
      if (value == null) {
         throw new IllegalStateException("required name [" + name + "] not found");
      } else {
         return value;
      }
   }
}
