package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class GetUserInfoProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.GET_USERINFO;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      User user = this.userManager.getUser(request.getIntParam("userid"));
      response.setAttr("userdesc", user.getDescription());
   }
}
