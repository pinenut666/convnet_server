package net.convnet.server.protocol;

public enum P2PCallType {
   ALL_DATA,
   UDP_S2S,
   UDP_S2SResp,
   UDP_C2S,
   UDP_C2SResp,
   UDP_C2C,
   UDP_C2CResp,
   UDP_GETPORT,
   UDP_P2PResp,
   TCP_C2S,
   TCP_C2SResp,
   TCP_SvrTrans,
   ALL_NOTARRIVE,
   NOTCONNECT,
   DISCONNECT,
   SAMEIP_CALL
}
