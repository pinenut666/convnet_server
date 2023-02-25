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
public class SureJoinGroupProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.PEER_SURE_JOIN_GROUP;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      response.setOutput(false);
      User orderUser = this.sessionManager.getSession(request.getIntParam("userid")).getUser();
      this.groupManager.dealGroupRequest(request.getIntParam("userid"), request.getIntParam("groupid"), false);
      int orderUserid = orderUser.getId();
      if ("T".equals(request.getParam("isallow"))) {
         Group group = this.groupManager.getGroup(request.getIntParam("groupid"));
         //修改
         if (!groupManager.getGroupAdminList(group.getId()).contains(session.getUser().getId())) {
            return;
         }
         this.groupManager.joinGroup(orderUser, group);
         Iterator<User> i$ = userManager.getUserByGroupId(group.getId()).iterator();

         while(i$.hasNext()) {
            User groupuser = i$.next();
            int userid = groupuser.getId();
            if (this.sessionManager.isOnline(userid)) {
               Session targetSession = this.sessionManager.getSession(userid);
               Response notify = this.createResponse(targetSession, Cmd.PEER_SURE_JOIN_GROUP_RESP);
               notify.setAttr("groupid", group.getId());
               notify.setAttr("whoid", orderUserid);
               notify.setAttr("name", orderUser.getName());
               notify.setAttr("isonline", this.sessionManager.isOnline(orderUser.getId()) ? "T" : "F");
               System.out.print("userjoinnotify:" + userid);
               this.write(targetSession, notify);
            }
         }
      }

   }
}
