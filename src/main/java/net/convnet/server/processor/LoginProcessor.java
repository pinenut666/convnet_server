package net.convnet.server.processor;

import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.FriendRequest;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.mybatis.pojo.UserEx;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import net.convnet.server.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;

@Service
public class LoginProcessor extends AbstractProcessor {
   @Override
   public Cmd accept() {
      return Cmd.LOGIN;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      if (session.isLogin()) {
         throw new ConvnetException("Already logined");
      } else {
         User user = null;

         try {
            user = this.userManager.validateUser(request.getParam("name"), request.getParam("password"));
         } catch (ConvnetException var13) {
            if ("USER_DISABLED".equals(var13.getMessage())) {
               response.setAttr("status", "R");
               return;
            }

            if ("USER_ERRORPASS".equals(var13.getMessage())) {
               response.setAttr("status", "F");
               return;
            }

            if ("USER_NOUSER".equals(var13.getMessage())) {
               response.setAttr("status", "F");
               return;
            }
         }

         if (user == null) {
            response.setAttr("status", "F");
         } else {
            session = this.sessionManager.createSession(user.getId(), session.getChannel());
            session.setAttr("mac", request.getParam("mac"));
            response.setAttr("status", "T");
            response.setAttr("ip", session.getIp());
            response.setAttr("id", user.getId());
            response.setAttr("nickName", user.getNickName());
            response.setAttr("name", user.getName());
            response.setAttr("password", "");
            response.setAttr("description", user.getDescription());
            String[] cp = user.getConnectPasswords();
            int len = cp == null ? 0 : cp.length;
            if (len > 0) {
               response.setAttr("p1", cp[0]);
            }

            if (len > 1) {
               response.setAttr("p2", cp[1]);
            }

            if (len > 2) {
               response.setAttr("p3", cp[2]);
            }

            if (len > 3) {
               response.setAttr("p4", cp[3]);
            }

            if (user.getReciveLimit() != 0L || user.getSendLimit() != 0L) {
               session.getChannel().pipeline().remove("speedLimit");
               session.getChannel().pipeline().addAfter("handshake", "speedLimit", new ChannelTrafficShapingHandler(user.getReciveLimit(), 0L));
            }
            //此处应该为获取对应userEx，若没有，则添加
            UserEx userEx = userManager.getUserEx(user.getId());
            if(userEx==null)
            {
               userEx = new UserEx();
               userEx.setUserId(user.getId());
            }
            userEx.setLastLoginAt(DateUtil.getLocalDateTime(new Date()));
            userEx.setLastLoginIp(session.getIp());
            userEx.setUserIsOnline(true);
            this.userManager.saveUserEx(userEx);

            for (FriendRequest friendRequest : userManager.getUserFriendRequests(user.getId())) {
               String description = friendRequest.getDescription();
               Response notify = this.createResponse(session, Cmd.PEER_ORD_FRIEND_RESP);
               User user1 = userManager.getUser(friendRequest.getTargetId());
               notify.setAttr("count", "1").setAttr("userId", user1.getId()).setAttr("nickName", user1.getName()).setAttr("description", description);
               this.write(session, notify);
            }

         }
      }
   }
}
