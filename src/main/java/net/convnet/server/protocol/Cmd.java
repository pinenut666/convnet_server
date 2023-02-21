package net.convnet.server.protocol;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public enum Cmd {
   SERVER_TRANS,
   LOGIN,
   LOGIN_RESP,
   LOGOUT,
   LOGOUT_RESP,
   RENEW_USER_STATUS,
   RENEW_MY_INFO,
   RENEW_MY_INFO_RESP,
   GET_VERSION_RESP,
   GET_SERVER_PORT,
   GET_SERVER_PORT_RESP,
   REGIST_USER,
   REGIST_USER_RESP,
   SEND_MSGTO_ID,
   SEND_MSGTO_ID_RESP,
   GET_FRIEND_INFO,
   GET_FRIEND_INFO_RESP,
   GET_GROUP_INFO,
   GET_GROUP_INFO_RESP,
   GET_GROUP_DESC,
   GET_GROUP_DESCRESP,
   GET_USERINFO,
   GET_USERINFO_RESP,
   ONLINE_TELL,
   ONLINE_TELL_RESP,
   OFFLINE_TELL_RESP,
   JOIN_GROUP,
   JOIN_GROUP_RESP,
   MODIFY_GROUP,
   MODIFY_GROUP_RESP,
   MSG_CAN_TARRIVE,
   CREATE_GROUP,
   CREATE_GROUP_RESP,
   QUIT_GROUP,
   QUIT_GROUP_RESP,
   KICK_OUT,
   KICK_OUT_RESP,
   ADD_FRIEND,
   ADD_FRIEND_RESP,
   DEL_FRIEND,
   DEL_FRIEND_RESP,
   ORD_SERVER_TRANS,
   ORD_SERVER_TRANS_RESP,
   PEER_SURE_FRIEND,
   PEER_SURE_FRIEND_RESP,
   PEER_REFUSED_FRIEND,
   PEER_REFUSED_FRIEND_RESP,
   PEER_SURE_JOIN_GROUP,
   PEER_SURE_JOIN_GROUP_RESP,
   PEER_ORD_FRIEND,
   PEER_ORD_FRIEND_RESP,
   FIND_USER,
   FIND_USER_RESP,
   FIND_GROUP,
   FIND_GROUP_RESP,
   CALL_TO_USER,
   CALL_TO_USER_RESP,
   CALL_TO_USER_NEW_PORT,
   CALL_TO_USER_NEW_PORT_RESP,
   DISS_CONN_USER,
   DISS_CONN_USER_RESP,
   IS_CLIENT_UDP,
   P2P,
   KEEP_ONLINE,
   USER_NEED_PASS,
   SAMEIP_INFO,
   SAMEIP_INFO_RESP,
   CHECK_NAT_TYPE,
   SERVER_SEND_TO_CLIENT,
   SEND_GROUP_MSG,
   SEND_GROUP_MSG_RESP,
   GROUP_MSG_CAN_TARRIVE,
   ERROR,
   HANDSHAKE,
   NULLPACK;

   public static final Set<Cmd> ANONYMOUS_CMDS = Sets.newHashSet(LOGIN, REGIST_USER, HANDSHAKE, KEEP_ONLINE);

   public String toOrdinal() {
      return String.valueOf(this.ordinal());
   }

   //这里能获取到对应的数据
   public static void main(String[] args) {
      Cmd[] arr$ = values();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Cmd cmd = arr$[i$];
         System.out.println(StringUtils.leftPad(cmd.ordinal() + "", 3) + "=" + cmd.name());
      }

   }
}
