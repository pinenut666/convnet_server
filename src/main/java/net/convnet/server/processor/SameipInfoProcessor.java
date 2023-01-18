package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class SameipInfoProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.SAMEIP_INFO;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setOutput(false);
      Session targeetsession = this.getSession(request.getIntParam("targetid"));
      if (targeetsession != null) {
         Response response1 = this.createResponse(targeetsession, Cmd.SAMEIP_INFO_RESP);
         response1.setAttr("callerid", session.getUserId());
         response1.setAttr("callerudpport", request.getParam("myudpport"));
         response1.setAttr("callertcpport", request.getParam("mytcpport"));
         response1.setAttr("callermac", request.getParam("mymac"));
         response1.setAttr("callerinnerip", request.getParam("myinnerip"));
         this.write(targeetsession, response1);
      }

   }
}
