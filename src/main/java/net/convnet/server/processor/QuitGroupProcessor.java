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
public class QuitGroupProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.QUIT_GROUP;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetGroup = request.getIntParam("groupid");
      Group group = this.groupManager.getGroup(targetGroup);
      if (group != null) {
         Iterator i$;
         User user;
         Session session1;
         Response response1;
         if (Objects.equals(session.getUser().getId(), group.getCreatorId())) {
            response.setOutput(false);
            i$ = userManager.getUserByGroupId(group.getId()).iterator();

            while(i$.hasNext()) {
               user = (User)i$.next();
               session1 = this.sessionManager.getSession(user.getId());
               if (session1 != null) {
                  response1 = this.createResponse(session1, Cmd.QUIT_GROUP_RESP);
                  response1.setAttr("msgtype", "groupdismiss");
                  response1.setAttr("groupid", targetGroup);
                  this.write(session1, response1);
               }
            }

            this.groupManager.removeGroup(group.getId());
         } else {
            i$ = userManager.getUserByGroupId(group.getId()).iterator();

            while(i$.hasNext()) {
               user = (User)i$.next();
               session1 = this.sessionManager.getSession(user.getId());
               if (session1 != null) {
                  response1 = this.createResponse(session1, Cmd.QUIT_GROUP_RESP);
                  response1.setAttr("msgtype", "userquit");
                  response1.setAttr("groupid", targetGroup);
                  response1.setAttr("userid", session.getUserId());
                  this.write(session1, response1);
               }
            }

            this.groupManager.quitGroup(session.getUser(), group);
         }

      }
   }
}
