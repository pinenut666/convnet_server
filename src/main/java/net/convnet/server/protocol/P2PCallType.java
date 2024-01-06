package net.convnet.server.protocol;


/**
 * P2 PCall类型
 *
 * @author Administrator
 * @date 2024/01/02
 */
public enum P2PCallType {
   /**
    * 所有数据
    */
   ALL_DATA,
   /**
    * UDP S2 操作系统
    */
   UDP_S2S,
   /**
    * UDP S2 SRESP
    */
   UDP_S2SResp,
   /**
    * UDP C2
    */
   UDP_C2S,
   /**
    * UDP C2 SRESP
    */
   UDP_C2SResp,
   /**
    * UDP C2C
    */
   UDP_C2C,
   /**
    * UDP C2 折痕
    */
   UDP_C2CResp,
   /**
    * UDP 获取端口
    */
   UDP_GETPORT,
   /**
    * UDP P2 预
    */
   UDP_P2PResp,
   /**
    * TCP C2S（英语：TCP C2S）
    */
   TCP_C2S,
   /**
    * TCP C2 SRESP系列
    */
   TCP_C2SResp,
   /**
    * TCP SVR 变速器
    */
   TCP_SvrTrans,
   /**
    * 全部未到达
    */
   ALL_NOTARRIVE,
   /**
    * 不连接
    */
   NOTCONNECT,
   /**
    * 断开
    */
   DISCONNECT,
   /**
    * SameIP调用
    */
   SAMEIP_CALL
}
