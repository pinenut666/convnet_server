package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class OrderFriendRequestProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.PEER_ORD_FRIEND;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetUserId = request.getIntParam("id");
      String description = request.getParam("description");
      User user = session.getUser();
      int userId = user.getId();
      User targetuser = this.userManager.getUser(targetUserId);
      response.setOutput(false);
      if (StringUtils.equals(targetuser.getFriendpass(), request.getParam("description"))) {
         if (!userManager.getUserFriends(user.getId()).contains(targetuser)) {
            userManager.deleteFriend(userId,targetUserId);
            userManager.deleteFriend(targetUserId,userId);
            //user.getFriends().add(targetuser);
            //targetuser.getFriends().add(user);
            this.userManager.saveUser(user);
            this.userManager.saveUser(targetuser);
            Response response1 = this.createResponse(session, Cmd.PEER_SURE_FRIEND_RESP);
            response1.setAttr("id", targetUserId);
            response1.setAttr("name", targetuser.getName());
            response1.setAttr("isonline", this.sessionManager.isOnline(targetUserId) ? "T" : "F");
            this.write(session, response1);
            if (this.sessionManager.getSession(targetUserId) != null) {
               Session targetSession = this.sessionManager.getSession(targetUserId);
               Response response2 = this.createResponse(targetSession, Cmd.PEER_SURE_FRIEND_RESP);
               response2.setAttr("id", userId);
               response2.setAttr("name", user.getName());
               response2.setAttr("isonline", this.sessionManager.isOnline(userId) ? "T" : "F");
               this.write(targetSession, response2);
            }

         }
      } else {
         this.userManager.sendFriendRequest(session.getUserId(), targetUserId, request.getParam("description"));
         if (this.sessionManager.isOnline(targetUserId)) {
            Session targetSession = this.sessionManager.getSession(targetUserId);
            Response notify = this.createResponse(targetSession, Cmd.PEER_ORD_FRIEND_RESP);
            notify.setAttr("count", "1").setAttr("userId", user.getId()).setAttr("nickName", user.getNickName()).setAttr("description", description);
            this.write(targetSession, notify);
         }

      }
   }
}
