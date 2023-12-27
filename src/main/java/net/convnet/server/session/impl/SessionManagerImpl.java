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
   @Value("${props.allUserQuitGroup}")
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
               //ChatGPT优化过后的代码如下：
               if (SessionManagerImpl.this.sessions.get(userId) == this && userId > 0) {
                  // 通知在线好友用户已下线
                  for (User friend : userManager.getUserFriends(userId)) {
                     Session friendSession = SessionManagerImpl.this.getSession(friend.getId());
                     if (friendSession != null) {
                        Response response = SessionManagerImpl.this.getProtocol(friendSession).createResponse(Cmd.OFFLINE_TELL_RESP);
                        response.setAttr("who", userId);
                        friendSession.getChannel().writeAndFlush(response);
                     }
                  }

                  // 处理用户所在的群组
                  List<Group> groups = groupManager.getGroupByUserId(userId);
                  for (Group group : groups) {
                     List<User> users = userManager.getUserByGroupId(group.getId());
                     for (User userInGroup : users) {
                        if (userInGroup.getId() == userId) {
                           continue;
                        }

                        Session userSession = SessionManagerImpl.this.getSession(userInGroup.getId());
                        if (userSession == null) {
                           continue;
                        }

                        // 通知群组成员用户已下线
                        Response response = SessionManagerImpl.this.getProtocol(userSession).createResponse(Cmd.OFFLINE_TELL_RESP);
                        response.setAttr("who", userId);
                        userSession.getChannel().writeAndFlush(response);

                        // 如果用户不是创建者，并且群组名以 "@" 开头或者全员退群，则通知用户退出群组
                        if ((group.getName().startsWith("@") || SessionManagerImpl.this.allUserQuitGroup) && !Objects.equals(group.getCreatorId(), user.getId())) {
                           Response quitResponse = SessionManagerImpl.this.getProtocol(userSession).createResponse(Cmd.QUIT_GROUP_RESP);
                           quitResponse.setAttr("msgtype", "userquit");
                           quitResponse.setAttr("groupid", group.getId());
                           quitResponse.setAttr("userid", userId);
                           userSession.getChannel().writeAndFlush(quitResponse);
                        }
                     }

                     // 如果用户不是创建者，并且群组名以 "@" 开头或者全员退群，则让用户退出群组
                     if ((group.getName().startsWith("@") || SessionManagerImpl.this.allUserQuitGroup) && !Objects.equals(group.getCreatorId(), user.getId())) {
                        SessionManagerImpl.this.groupManager.quitGroup(user, group);
                     }
                  }
               }



               //源代码
//               if (SessionManagerImpl.this.sessions.get(userId) == this && userId > 0) {
//                  for (User tmpuser : userManager.getUserFriends(userId)) {
//                     Session session1x = SessionManagerImpl.this.getSession(tmpuser.getId());
//                     if (session1x != null) {
//                        Response response = SessionManagerImpl.this.getProtocol(session1x).createResponse(Cmd.OFFLINE_TELL_RESP);
//                        response.setAttr("who", userId);
//                        session1x.getChannel().writeAndFlush(response);
//                     }
//                  }
//
//                  List<Group> groups = groupManager.getGroupByUserId(userId);
//                  Iterator i$ = groups.iterator();
//
//                  label76:
//                  while(true) {
//                     Group group;
//                     label74:
//                     do {
//                        if (!i$.hasNext()) {
//                           break label76;
//                        }
//
//                        group = (Group)i$.next();
//                        //
//                        Iterator i$xx = userManager.getUserByGroupId(group.getId()).iterator();//group.getUsers().iterator();
//
//                        while(true) {
//                           Session session1;
//                           do {
//                              User user1;
//                              int useridtmpid;
//                              do {
//                                 if (!i$xx.hasNext()) {
//                                    continue label74;
//                                 }
//
//                                 user1 = (User)i$xx.next();
//                                 useridtmpid = user1.getId();
//                              } while(userId == useridtmpid);
//
//                              session1 = SessionManagerImpl.this.getSession(user1.getId());
//                           } while(session1 == null);
//
//                           Response response2;
//                           if ((group.getName().startsWith("@") || SessionManagerImpl.this.allUserQuitGroup) && !Objects.equals(group.getCreatorId(), user.getId())) {
//                              response2 = SessionManagerImpl.this.getProtocol(session1).createResponse(Cmd.QUIT_GROUP_RESP);
//                              response2.setAttr("msgtype", "userquit");
//                              response2.setAttr("groupid", group.getId());
//                              response2.setAttr("userid", userId);
//                              session1.getChannel().writeAndFlush(response2);
//                           }
//
//                           response2 = SessionManagerImpl.this.getProtocol(session1).createResponse(Cmd.OFFLINE_TELL_RESP);
//                           response2.setAttr("who", userId);
//                           session1.getChannel().writeAndFlush(response2);
//                        }
//                     } while(!group.getName().startsWith("@") && !SessionManagerImpl.this.allUserQuitGroup);
//
//                     if (!Objects.equals(group.getCreatorId(), user.getId())) {
//                        SessionManagerImpl.this.groupManager.quitGroup(user, group);
//                     }
//                  }
//               }

               for (SessionListener listener : SessionManagerImpl.this.listeners) {
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

      for (SessionListener listener : this.listeners) {
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
         session.getChannel().writeAndFlush(response1);
         return true;
      }
   }

   @Override
   public void destroy() throws Exception {

      for (Session session : this.sessions.values()) {
         for (SessionListener listener : this.listeners) {
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
