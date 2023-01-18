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
public class GetServerPortProcessor extends AbstractProcessor {
   @Value("#{props.udpPorts}")
   private String updPorts;

   public Cmd accept() {
      return Cmd.GET_SERVER_PORT;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setAttr("updPorts", this.updPorts);
   }
}
