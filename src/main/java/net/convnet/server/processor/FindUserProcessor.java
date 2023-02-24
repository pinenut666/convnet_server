package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.identity.FindType;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

@Service
public class FindUserProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.FIND_USER;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      User user = session.getUser();
      if (user.SoIfICanAddfriend()) {
         response.setAttr("users", this.userManager.findUser(FindType.from(request.getParam("type")), request.getParam("value"), 20));
      } else {
         response.setAttr("users", (Object)null);
      }

   }
}
