package net.convnet.server.identity;

/**
 * 查找类型
 *
 * @author Administrator
 * @date 2024/01/02
 */
public enum FindType {
   /**
    * 名字
    */
   NAME,
   /**
    * 昵称
    */
   NICK_NAME,
   /**
    * 描述
    */
   DESCRIPTION;

   /**
    * 简称转换为对应枚举(B：存疑！）
    *
    * @param s s
    * @return {@link FindType}
    */
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
