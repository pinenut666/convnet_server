package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Objects;

@Service
public class KickoutGroupProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.KICK_OUT;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      User optuser = this.userManager.getUser(request.getIntParam("userid"));
      Group group = this.groupManager.getGroup(request.getIntParam("groupid"));
      response.setOutput(false);
      if (Objects.equals(session.getUser().getId(), group.getCreatorId())) {
         Iterator i$ = userManager.getUserByGroupId(group.getId()).iterator();

         while(i$.hasNext()) {
            User user = (User)i$.next();
            Session usersession = this.sessionManager.getSession(user.getId());
            if (usersession != null) {
               Response response1 = this.createResponse(usersession, Cmd.KICK_OUT_RESP);
               response1.setAttr("groupid", group.getId());
               response1.setAttr("userid", optuser.getId());
               this.write(usersession, response1);
            }
         }

         this.groupManager.quitGroup(optuser, group);
      }
   }
}
