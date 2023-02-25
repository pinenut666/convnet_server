package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class PeerSureFriendProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.PEER_SURE_FRIEND;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetUserId = request.getIntParam("id");
      User targetuser = this.userManager.getUser(targetUserId);
      this.userManager.dealFriendRequest(targetUserId, session.getUserId(), false);
      User user = session.getUser();
      response.setAttr("id", targetUserId);
      response.setAttr("name", targetuser.getNickName());
      response.setAttr("isonline", this.sessionManager.isOnline(targetUserId) ? "T" : "F");
      Session targetsession = this.sessionManager.getSession(targetUserId);
      if (targetsession != null) {
         Response response1 = this.createResponse(targetsession, Cmd.PEER_SURE_FRIEND_RESP);
         response1.setAttr("id", user.getId());
         response1.setAttr("name", user.getName());
         response1.setAttr("isonline", this.sessionManager.isOnline(targetUserId) ? "T" : "F");
         this.write(targetsession, response1);
      }

   }
}
