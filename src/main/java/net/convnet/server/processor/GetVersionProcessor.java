package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetVersionProcessor extends AbstractProcessor {
   @Value("#{props.updateURL}")
   private String updateURL;

   @Override
   public Cmd accept() {
      return Cmd.GET_VERSION_RESP;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setAttr("version", this.sessionManager.getProtocol(session).getVersionCode());
      response.setAttr("updateURL", this.updateURL);
   }
}
