package net.convnet.server.support.attr;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import java.util.*;
import java.util.Map.Entry;

public abstract class AbstractAttrable implements Attrable {
   private static final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);

   @Override
   public boolean hasAttr(String key) {
      return this.getAttr(key) != null;
   }

   @Override
   public String getAttr(String key) {
      return (String)this.getAttr(key, String.class);
   }

   @Override
   public String getAttr(String key, String defaultValue) {
      String value = this.getAttr(key);
      return value == null ? defaultValue : value;
   }

   @Override
   public String getRequiredAttr(String key) throws IllegalStateException {
      String value = this.getAttr(key);
      if (value == null) {
         throw new IllegalStateException("required key [" + key + "] not found");
      } else {
         return value;
      }
   }

   @Override
   public String resolvePlaceholders(String text) {
      return helper.replacePlaceholders(text, new PlaceholderResolver() {
         @Override
         public String resolvePlaceholder(String placeholderName) {
            return AbstractAttrable.this.getAttr(placeholderName);
         }
      });
   }

   @Override
   public String[] getArrayAttr(String key) {
      return (String[])this.getAttr(key, String[].class);
   }

   @Override
   public <T> T getAttr(String key, Class<T> targetType, T defaultValue) {
      T value = this.getAttr(key, targetType);
      return value == null ? defaultValue : value;
   }

   @Override
   public <T> T getRequiredAttr(String key, Class<T> targetType) throws IllegalStateException {
      T value = this.getAttr(key, targetType);
      if (value == null) {
         throw new IllegalStateException("required key [" + key + "] not found");
      } else {
         return value;
      }
   }

   @Override
   public Map<String, String> getAttrs(String... keys) {
      return this.getAttrs((Collection)(keys == null ? Collections.EMPTY_LIST : Arrays.asList(keys)));
   }

   @Override
   public Map<String, String> getAttrs(Collection<String> keys) {
      if (CollectionUtils.isEmpty((Collection)keys)) {
         keys = Arrays.asList(this.getKeys());
      }

      Map<String, String> map = Maps.newHashMapWithExpectedSize(((Collection)keys).size());
      Iterator i$ = ((Collection)keys).iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         String value = this.getAttr(key);
         if (value != null) {
            map.put(key, value);
         }
      }

      return Collections.unmodifiableMap(map);
   }

   @Override
   public void setAttrs(Map<String, ?> map) {
      Iterator i$ = map.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, ?> entry = (Entry)i$.next();
         this.setAttr((String)entry.getKey(), entry.getValue());
      }

   }

   @Override
   public String toString() {
      return this.getAttrs().toString();
   }
}
