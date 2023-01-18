package net.convnet.server.session;

import io.netty.channel.Channel;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.Protocol;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public interface SessionManager {
   Session createAnonymousSession(Channel var1);

   Session createSession(int var1, Channel var2);

   boolean isOnline(int var1);

   Session getSession(int var1);

   Session getSession(Channel var1);

   Protocol getProtocol(Channel var1);

   Protocol getProtocol(Session var1);

   Collection<Session> getSessions();

   boolean sendMessageToUser(User var1, String var2);
}
