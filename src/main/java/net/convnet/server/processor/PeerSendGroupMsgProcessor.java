package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class PeerSendGroupMsgProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.SEND_GROUP_MSG;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetgroupid = request.getIntParam("groupid");
      Group targetgroup = this.groupManager.getGroup(targetgroupid);
      Iterator i$ = userManager.getUserByGroupId(targetgroup.getId()).iterator();

      while(i$.hasNext()) {
         User targetuser = (User)i$.next();
         int targetuserid = targetuser.getId();
         Session targetsession = this.sessionManager.getSession(targetuserid);
         response.setOutput(false);
         if (targetsession != null) {
            Response response1 = this.createResponse(targetsession, Cmd.SEND_GROUP_MSG_RESP);
            response1.setAttr("groupid", targetgroupid);
            response1.setAttr("userid", session.getUserId());
            response1.setAttr("msg", request.getParam("msg"));
            this.write(targetsession, response1);
         }
      }

   }
}
