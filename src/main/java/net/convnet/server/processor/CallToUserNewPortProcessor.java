package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.*;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class CallToUserNewPortProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.CALL_TO_USER_NEW_PORT;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetuserid = request.getIntParam("userid");
      Session targetsession = this.sessionManager.getSession(targetuserid);
      response.setOutput(false);
      if (targetsession != null) {
         Response response1 = this.createResponse(session, Cmd.CALL_TO_USER_RESP);
         response1.setAttr("callType", P2PCallType.UDP_C2C);
         response1.setAttr("callerid", session.getUserId());
         response1.setAttr("callerip", session.getIp());
         response1.setAttr("callerudpport", request.getIntParam("udpport"));
         response1.setAttr("callermac", session.getMac());
         this.write(targetsession, response1);
      }

   }
}
