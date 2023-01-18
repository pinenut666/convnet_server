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

@Service
public class OnlineTellResp extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.ONLINE_TELL;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      this.TellUserOnline(session.getUser());
   }

   private void TellUserOnline(User user) {
      int userid = user.getId();
      Iterator i$ = userManager.getUserFriends(user.getId()).iterator();
      while(i$.hasNext()) {
         User tmpuser = (User)i$.next();
         Session session = this.sessionManager.getSession(tmpuser.getId());
         if (session != null) {
            Response resposne1 = this.createResponse(session, Cmd.ONLINE_TELL_RESP);
            resposne1.setAttr("who", userid);
            this.write(session, resposne1);
         }
      }


      for(Group group1:groupManager.getGroupByUserId(user.getId())) {
         for (User user1 : userManager.getUserByGroupId(group1.getId())) {
            int userid2 = user1.getId();
            if (userid != userid2) {
               Session session = this.sessionManager.getSession(user1.getId());
               if (session != null) {
                  Response resposne1 = this.createResponse(session, Cmd.ONLINE_TELL_RESP);
                  resposne1.setAttr("who", userid);
                  this.write(session, resposne1);
               }
            }
         }
      }

   }
}
