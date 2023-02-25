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
public class DeleteFriendProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.DEL_FRIEND;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetUserId = request.getIntParam("id");
      User targetuser = this.userManager.getUser(targetUserId);
      User user = session.getUser();
      //删除对应的朋友（互相删除捏）
      userManager.deleteFriend(user.getId(),targetUserId);
      userManager.deleteFriend(targetUserId,user.getId());
      this.userManager.saveUser(user);
      this.userManager.saveUser(targetuser);
      response.setAttr("id", targetUserId);
      Session targetsession = this.sessionManager.getSession(targetUserId);
      if (targetsession != null) {
         Response response1 = this.createResponse(targetsession, Cmd.DEL_FRIEND_RESP);
         response1.setAttr("id", user.getId());
         this.write(targetsession, response1);
      }

   }
}
