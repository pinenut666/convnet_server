package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class TransmitProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.SERVER_TRANS;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setOutput(false);
      Session session1 = this.sessionManager.getSession(request.getIntParam("userId"));
      if (session1 == null) {
         Response resposne1 = this.createResponse(session, Cmd.OFFLINE_TELL_RESP);
         resposne1.setAttr("who", request.getIntParam("userId"));
         this.write(session, resposne1);
      } else {
         this.write(request.getIntParam("userId"), response.getCmd(), this.attr("payload", request.getRawParam("payload")));
      }

   }
}
