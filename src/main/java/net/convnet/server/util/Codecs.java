package net.convnet.server.util;

import net.convnet.server.Constants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

public final class Codecs {
   public static byte[] hash(byte[] bytes) {
      return DigestUtils.md5(bytes);
   }

   public static String hash(String str) {
      return encode(hash(getBytes(str)));
   }

   public static String hash(String str, int len) {
      return StringUtils.left(hash(str), len);
   }

   public static String hashHex(String str) {
      return DigestUtils.md5Hex(str);
   }

   public static String encode(byte[] bytes) {
      return Base64.encodeBase64URLSafeString(bytes);
   }

   public static String encode(long l) {
      ByteBuffer buf = ByteBuffer.allocate(8);
      buf.putLong(l);
      return encode(buf.array());
   }

   public static byte[] decode(String str) {
      return Base64.decodeBase64(str);
   }

   public static String uuid() {
      UUID uuid = UUID.randomUUID();
      ByteBuffer buf = ByteBuffer.allocate(16);
      buf.putLong(uuid.getMostSignificantBits());
      buf.putLong(uuid.getLeastSignificantBits());
      return encode(hash(buf.array()));
   }

   public static String uuid(int len) {
      return StringUtils.left(uuid(), len);
   }

   public static byte[] getBytes(String s) {
      return s == null ? null : s.getBytes(Constants.CHARSET);
   }

   public static String toString(byte[] bytes) {
      return new String(bytes, Constants.CHARSET);
   }

   private Codecs() {
   }
}
