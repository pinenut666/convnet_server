package net.convnet.server.support.attr;

public interface Converter {
   <T> T convert(Object var1, Class<T> var2) throws IllegalArgumentException;
}
