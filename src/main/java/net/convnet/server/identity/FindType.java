package net.convnet.server.identity;

public enum FindType {
   NAME,
   NICK_NAME,
   DESCRIPTION;

   public static FindType from(String s) {
      if ("U".equals(s)) {
         return NAME;
      } else if ("N".equals(s)) {
         return NICK_NAME;
      } else {
         return "B".equals(s) ? DESCRIPTION : null;
      }
   }
}
