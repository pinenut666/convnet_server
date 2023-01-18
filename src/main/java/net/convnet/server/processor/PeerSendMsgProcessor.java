package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class PeerSendMsgProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.SEND_MSGTO_ID;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetuserid = request.getIntParam("userid");
      this.userManager.getUser(targetuserid);
      Session targetsession = this.sessionManager.getSession(targetuserid);
      response.setOutput(false);
      Response response1;
      if (targetsession == null) {
         response1 = this.createResponse(session, Cmd.MSG_CAN_TARRIVE);
         response1.setAttr("userid", targetuserid);
         response1.setAttr("msg", request.getParam("msg"));
         this.write(session, response1);
      } else {
         response1 = this.createResponse(targetsession, Cmd.SEND_MSGTO_ID_RESP);
         response1.setAttr("userid", session.getUserId());
         response1.setAttr("msg", request.getParam("msg"));
         this.write(targetsession, response1);
      }

   }
}
