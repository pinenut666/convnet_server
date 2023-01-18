package net.convnet.server.processor.call;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.CallProcessor;
import net.convnet.server.protocol.P2PCallType;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class AllDataCallProcessor implements CallProcessor {
   public P2PCallType accept() {
      return P2PCallType.ALL_DATA;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
   }
}
