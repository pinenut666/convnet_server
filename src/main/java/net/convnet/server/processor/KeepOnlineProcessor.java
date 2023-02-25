package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class KeepOnlineProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.KEEP_ONLINE;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
   }
}
