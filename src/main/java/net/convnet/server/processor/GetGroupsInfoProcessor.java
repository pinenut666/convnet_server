package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class GetGroupsInfoProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.GET_GROUP_INFO;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setAttr("groups", groupManager.getGroupByUserId(session.getUser().getId()));
   }
}
