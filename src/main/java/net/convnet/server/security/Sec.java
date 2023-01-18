package net.convnet.server.security;

public final class Sec {
   private Sec() {
   }

   public static Integer getUserId() {
      return (Integer)SecContext.getContext().get("userId");
   }
}
