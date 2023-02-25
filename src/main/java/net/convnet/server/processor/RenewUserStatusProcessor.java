package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class RenewUserStatusProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.RENEW_USER_STATUS;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      if ("".equals(request.getParam("udpport"))) {
         session.setAttr("udpport", "0");
      } else {
         session.setAttr("udpport", request.getParam("udpport"));
      }

      if ("".equals(request.getParam("tcpport"))) {
         session.setAttr("tcpport", "0");
      } else {
         session.setAttr("tcpport", request.getParam("tcpport"));
      }

      session.setAttr("nattype", request.getParam("nattype"));
      response.setOutput(false);
   }
}
