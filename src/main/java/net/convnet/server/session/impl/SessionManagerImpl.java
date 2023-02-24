package net.convnet.server.session.impl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.internal.PlatformDependent;
import net.convnet.server.identity.*;
import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.mybatis.pojo.UserEx;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Protocol;
import net.convnet.server.protocol.ProtocolFactory;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionListener;
import net.convnet.server.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public final class SessionManagerImpl implements SessionManager, DisposableBean {
   private static final Logger LOG = LoggerFactory.getLogger(SessionManagerImpl.class);
   private Map<Integer, Session> sessions = PlatformDependent.newConcurrentHashMap();
   private List<SessionListener> listeners = new ArrayList<>();
   private ProtocolFactory protocolFactory;
   private UserManager userManager;
   @Value("#{props.allUserQuitGroup}")
   private boolean allUserQuitGroup;
   @Autowired
   private GroupManager groupManager;

   public void setListeners(List<SessionListener> listeners) {
      this.listeners.addAll(listeners);
   }

   public void setProtocolFactory(ProtocolFactory protocolFactory) {
      this.protocolFactory = protocolFactory;
   }

   public void setUserManager(UserManager userManager) {
      this.userManager = userManager;
   }

   @Override
   public Session createAnonymousSession(Channel channel) {
      return new DefaultSession(0, channel) {
         private static final long serialVersionUID = 1326446241742267020L;

         @Override
         public boolean isLogin() {
            return false;
         }

         @Override
         public User getUser() {
            return null;
         }
      };
   }

   @Override
   public Session createSession(int userId, Channel channel) {
      Session oldSession = (Session)this.sessions.get(userId);
      if (oldSession != null) {
         Response kick = this.getProtocol(oldSession).createResponse(Cmd.LOGIN_RESP);
         kick.setAttr("status", "D").setAttr("ip", oldSession.getIp());
         oldSession.getChannel().writeAndFlush(kick);
         oldSession.destory();
      }

      DefaultSession session = new DefaultSession(userId, channel) {
         private static final long serialVersionUID = 1326446241742267020L;

         @Override
         public boolean isLogin() {
            return true;
         }

         @Override
         public User getUser() {
            return userManager.getUser(this.getUserId());
         }

         @Override
         public void destory() {
            User user = this.getUser();
            if (!this.isClosed()) {
               //根据user的属性获取userEx
               UserEx userEx = userManager.getUserEx(user.getId());
               userEx.setSendToServer(userEx.getSendToServer() + this.getReadBytes());
               userEx.setReciveFromServer(userEx.getReciveFromServer() + this.getWriteBytes());
               userEx.setUserIsOnline(false);
               //设置属性并保存
               userManager.saveUserEx(userEx);
               final long id = this.getId();
               final int userId = this.getUserId();
               //设置迭代器
               Iterator mydiedai;
               //满足这些条件时
               if (SessionManagerImpl.this.sessions.get(userId) == this && userId > 0) {
                  mydiedai = userManager.getUserFriends(userId).iterator();
                  while(mydiedai.hasNext()) {
                     User tmpuser = (User)mydiedai.next();
                     Session session1x = SessionManagerImpl.this.getSession(tmpuser.getId());
                     if (session1x != null) {
                        Response response = SessionManagerImpl.this.getProtocol(session1x).createResponse(Cmd.OFFLINE_TELL_RESP);
                        response.setAttr("who", userId);
                        session1x.getChannel().writeAndFlush(response);
                     }
                  }

                  List<Group> groups = groupManager.getGroupByUserId(userId);
                  Iterator i$ = groups.iterator();

                  label76:
                  while(true) {
                     Group group;
                     label74:
                     do {
                        if (!i$.hasNext()) {
                           break label76;
                        }

                        group = (Group)i$.next();
                        //
                        Iterator i$xx = userManager.getUserByGroupId(group.getId()).iterator();//group.getUsers().iterator();

                        while(true) {
                           Session session1;
                           do {
                              User user1;
                              int useridtmpid;
                              do {
                                 if (!i$xx.hasNext()) {
                                    continue label74;
                                 }

                                 user1 = (User)i$xx.next();
                                 useridtmpid = user1.getId();
                              } while(userId == useridtmpid);

                              session1 = SessionManagerImpl.this.getSession(user1.getId());
                           } while(session1 == null);

                           Response response2;
                           if ((group.getName().startsWith("@") || SessionManagerImpl.this.allUserQuitGroup) && !Objects.equals(group.getCreatorId(), user.getId())) {
                              response2 = SessionManagerImpl.this.getProtocol(session1).createResponse(Cmd.QUIT_GROUP_RESP);
                              response2.setAttr("msgtype", "userquit");
                              response2.setAttr("groupid", group.getId());
                              response2.setAttr("userid", userId);
                              session1.getChannel().writeAndFlush(response2);
                           }

                           response2 = SessionManagerImpl.this.getProtocol(session1).createResponse(Cmd.OFFLINE_TELL_RESP);
                           response2.setAttr("who", userId);
                           session1.getChannel().writeAndFlush(response2);
                        }
                     } while(!group.getName().startsWith("@") && !SessionManagerImpl.this.allUserQuitGroup);

                     if (!Objects.equals(group.getCreatorId(), user.getId())) {
                        SessionManagerImpl.this.groupManager.quitGroup(user, group);
                     }
                  }
               }

               mydiedai = SessionManagerImpl.this.listeners.iterator();

               while(mydiedai.hasNext()) {
                  SessionListener listener = (SessionListener)mydiedai.next();
                  listener.onDestroy(this);
               }

               this.getChannel().close().addListener(new ChannelFutureListener() {
                  @Override
                  public void operationComplete(ChannelFuture future) {
                     SessionManagerImpl.LOG.debug("Channel [sessionId:" + id + ",userId:" + userId + "] closed");
                  }
               });
               if (SessionManagerImpl.this.sessions.get(userId) == this) {
                  SessionManagerImpl.this.sessions.remove(userId);
               }

               this.setClosed(true);
               super.destory();
            }
         }
      };
      this.sessions.put(userId, session);
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         SessionListener listener = (SessionListener)i$.next();

         try {
            listener.onCreate(session);
         } catch (Exception var8) {
            LOG.error("Listener [" + listener + "] onCreate error", var8);
         }
      }

      return session;
   }

   @Override
   public boolean isOnline(int userId) {
      return this.sessions.containsKey(userId);
   }

   @Override
   public Session getSession(int userId) {
      return (Session)this.sessions.get(userId);
   }

   @Override
   public Session getSession(Channel channel) {
      return (Session)channel.attr(DefaultSession.ATTR_KEY).get();
   }

   @Override
   public Protocol getProtocol(Session session) {
      int version = session.getProtocolVersion();
      return version > 0 ? this.protocolFactory.getProtocol(version) : this.protocolFactory.getDefaultProtocol();
   }

   @Override
   public Protocol getProtocol(Channel channel) {
      return this.getProtocol(this.getSession(channel));
   }

   @Override
   public Collection<Session> getSessions() {
      return this.sessions.values();
   }

   @Override
   public boolean sendMessageToUser(User user, String message) {
      Session session = this.getSession(user.getId());
      if (session == null) {
         return false;
      } else {
         Protocol protocol = this.getProtocol(session);
         Response response1 = protocol.createResponse(Cmd.SERVER_SEND_TO_CLIENT);
         response1.setAttr("message", message);
         if (session != null) {
            session.getChannel().writeAndFlush(response1);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public void destroy() throws Exception {
      Iterator i$ = this.sessions.values().iterator();

      while(i$.hasNext()) {
         Session session = (Session)i$.next();
         Iterator diedai = this.listeners.iterator();

         while(diedai.hasNext()) {
            SessionListener listener = (SessionListener)diedai.next();

            try {
               listener.onDestroy(session);
            } catch (Exception var6) {
               LOG.error("Listener [" + listener + "] onDestroy error", var6);
            }
         }

         session.destory();
      }

      this.sessions.clear();
   }
}
