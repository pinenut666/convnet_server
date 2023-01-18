package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class GetFriendsInfoProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.GET_FRIEND_INFO;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      //TODO:看来以后要从这里面究极折磨了
      response.setAttr("friends", userManager.getUserFriends(session.getUser().getId()));
   }
}
