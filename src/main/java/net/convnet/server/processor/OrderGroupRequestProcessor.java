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
public class OrderGroupRequestProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.JOIN_GROUP;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int targetGroup = request.getIntParam("groupid");
      String description = request.getParam("description");
      response.setOutput(false);
      Group group = this.groupManager.getGroup(targetGroup);
      User user = session.getUser();
      User admin = userManager.getUser(group.getCreatorId());
      String grouppass = group.getPassword();
      if (grouppass.equals(description) && !"".equals(description)) {
         this.groupManager.joinGroup(user, group);
         Iterator i$ = userManager.getUserByGroupId(group.getId()).iterator();

         while(i$.hasNext()) {
            User groupuser = (User)i$.next();
            response.setOutput(false);
            int userid = groupuser.getId();
            if (this.sessionManager.isOnline(userid)) {
               Session targetSession = this.sessionManager.getSession(userid);
               Response notify = this.createResponse(targetSession, Cmd.PEER_SURE_JOIN_GROUP_RESP);
               notify.setAttr("groupid", group.getId());
               notify.setAttr("whoid", user.getId());
               notify.setAttr("name", user.getName());
               notify.setAttr("isonline", this.sessionManager.isOnline(user.getId()) ? "T" : "F");
               System.out.print("userjoinnotify:" + userid);
               this.write(targetSession, notify);
            }
         }

      } else {
         this.groupManager.sendGroupRequest(user.getId(), group.getId(), description);
         if (this.sessionManager.isOnline(admin.getId())) {
            Response resposne2 = this.createResponse(session, Cmd.JOIN_GROUP_RESP);
            resposne2.setAttr("username", user.getName());
            resposne2.setAttr("userid", user.getId());
            resposne2.setAttr("gourpid", targetGroup);
            resposne2.setAttr("gourpdesc", group.getDescription());
            resposne2.setAttr("orderdesc", description);
            this.write(this.sessionManager.getSession(admin.getId()), resposne2);
         }

      }
   }
}
