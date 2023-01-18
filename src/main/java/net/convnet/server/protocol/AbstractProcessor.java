package net.convnet.server.protocol;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.identity.GroupManager;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.mapper.GroupAdminMapper;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionManager;
import net.convnet.server.util.AttrMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class AbstractProcessor implements Processor {
   @Autowired
   protected UserManager userManager;
   @Autowired
   protected GroupManager groupManager;
   @Autowired
   protected SessionManager sessionManager;

   protected Session getSession(int userId) {
      Session session = this.sessionManager.getSession(userId);
      if (session == null) {
         throw new ConvnetException("Session for user [" + userId + "] not found");
      } else {
         return session;
      }
   }

   protected Response createResponse(Session session, Cmd cmd) {
      return session != null ? this.sessionManager.getProtocol(session).createResponse(cmd) : null;
   }

   protected void write(Session session, Response response) {
      if (session != null) {
         session.getChannel().writeAndFlush(response);
      }

   }

   protected void write(int userId, Cmd cmd, Map<String, Object> attrs) {
      Session session = this.getSession(userId);
      this.write(session, this.createResponse(session, cmd).setAttrs(attrs));
   }

   protected AttrMap attr(String name, Object value) {
      return new AttrMap(name, value);
   }
}
