package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class PeerRefusedFriendProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.PEER_REFUSED_FRIEND;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetUserId = request.getIntParam("id");
      this.userManager.dealFriendRequest(targetUserId, session.getUserId(), true);
   }
}
