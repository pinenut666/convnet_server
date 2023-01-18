package net.convnet.server.support.attr;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class JSONObjectAttrable extends AbstractAttrable implements Serializable {
   private static final long serialVersionUID = 460183492223957626L;
   private JSONObject attr;

   public JSONObjectAttrable(JSONObject attr) {
      this.attr = attr;
   }

   public JSONObjectAttrable(Map<String, Object> map) {
      this.attr = new JSONObject(map);
   }

   public JSONObjectAttrable() {
      this.attr = new JSONObject();
   }

   @JSONField(
      serialize = false
   )
   public JSONObject getAttr() {
      return this.attr;
   }

   public void setAttr(JSONObject attr) {
      this.attr = attr;
   }

   public boolean hasAttr(String key) {
      return this.attr.containsKey(key);
   }

   public <T> T getAttr(String key, Class<T> targetType) {
      Object obj = this.attr.get(key);
      return convert(obj, targetType);
   }

   @JSONField(
      serialize = false
   )
   public String[] getKeys() {
      Collection<String> keys = this.attr.keySet();
      return (String[])keys.toArray(new String[keys.size()]);
   }

   public void setAttr(String key, Object value) {
      this.attr.put(key, value);
   }

   public void setAttrs(Map<String, ?> map) {
      this.attr.putAll(map);
   }

   public void removeAttr(String key) {
      this.attr.remove(key);
   }

   protected Object clone() throws CloneNotSupportedException {
      JSONObjectAttrable obj = (JSONObjectAttrable)super.clone();
      JSONObject json = new JSONObject();
      json.putAll(this.getAttr());
      obj.setAttr(json);
      return obj;
   }

   public static <T> T convert(Object value, Class<T> targetType) {
      return FastjsonConverter.INSTANCE.convert(value, targetType);
   }
}
