package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.*;
import net.convnet.server.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class CallToUserProcessor extends AbstractProcessor implements ApplicationContextAware, InitializingBean {
   @Value("${props.forceTrans}")
   private boolean forceTrans;
   private static final Logger LOG = LoggerFactory.getLogger(CallToUserProcessor.class);
   private ApplicationContext appCtx;
   private Map<P2PCallType, CallProcessor> callProcessors = new HashMap<>();

   public void setCallProcessors(Map<P2PCallType, CallProcessor> callProcessors) {
      this.callProcessors = callProcessors;
   }

   @Override
   public Cmd accept() {
      return Cmd.CALL_TO_USER;
   }

   @Override
   public void process(Session session, Request request, Response response) throws ConvnetException {
      int count = request.getIntParam("count");
      int targetUserId = request.getIntParam("id");
      String password = request.getParam("password");
      User targetuser = this.userManager.getUser(targetUserId);
      User caller = session.getUser();
      Session targetsession = this.sessionManager.getSession(targetUserId);
      response.setOutput(false);
      Response response1;
      if (targetsession == null) {
         response1 = this.createResponse(session, Cmd.OFFLINE_TELL_RESP);
         response1.setAttr("who", targetUserId);
         this.write(session, response1);
      } else {
         if (this.forceTrans) {
            count = 6;
         }

         if (this.CheckPass(targetuser, password)) {
            if (count < 2 && session.getIp().equals(targetsession.getIp())) {
               response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
               response1.setAttr("callType", P2PCallType.SAMEIP_CALL);
               response1.setAttr("id", caller.getId());
               this.write(targetsession, response1);
            } else {
               if (targetsession.getAttr("udpport") == null || "".equals(targetsession.getAttr("udpport"))) {
                  targetsession.setAttr("udpport", "0");
               }

               if (targetsession.getAttr("tcpport") == null || "".equals(targetsession.getAttr("tcpport"))) {
                  targetsession.setAttr("tcpport", "0");
               }

               if (session.getAttr("udpport") == null || "".equals(session.getAttr("udpport"))) {
                  session.setAttr("udpport", "0");
               }

               if (session.getAttr("tcpport") == null || "".equals(session.getAttr("tcpport"))) {
                  session.setAttr("tcpport", "0");
               }

               if (targetsession.getAttr("nattype") == null) {
                  targetsession.setAttr("nattype", "UK");
               }

               if (session.getAttr("nattype") == null) {
                  session.setAttr("nattype", "UK");
               }

               Response response2;
               if (count < 3 && !"UK".equals(session.getAttr("nattype")) && Integer.parseInt(targetsession.getAttr("udpport")) > 0 && Integer.parseInt(session.getAttr("udpport")) > 0) {
                  response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                  response1.setAttr("callType", P2PCallType.UDP_S2S);
                  response1.setAttr("callerid", caller.getId());
                  response1.setAttr("callerip", session.getIp());
                  response1.setAttr("callerudpport", session.getAttr("udpport"));
                  response1.setAttr("callermac", session.getMac());
                  this.write(targetsession, response1);
                  response2 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                  response2.setAttr("callType", P2PCallType.UDP_S2S);
                  response2.setAttr("callerid", targetUserId);
                  response2.setAttr("callerip", targetsession.getIp());
                  response2.setAttr("callerudpport", targetsession.getAttr("udpport"));
                  response2.setAttr("callermac", targetsession.getMac());
                  this.write(session, response2);
               } else {
                  if (count < 3) {
                     if (!"UK".equals(targetsession.getAttr("nattype")) && Integer.parseInt(session.getAttr("udpport")) > 0) {
                        response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                        response1.setAttr("callType", P2PCallType.UDP_C2S);
                        response1.setAttr("callerid", caller.getId());
                        response1.setAttr("callerip", session.getIp());
                        response1.setAttr("callerudpport", session.getAttr("udpport"));
                        response1.setAttr("callermac", session.getMac());
                        this.write(targetsession, response1);
                        return;
                     }

                     if (!"UK".equals(session.getAttr("nattype")) && Integer.parseInt(targetsession.getAttr("udpport")) > 0) {
                        response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                        response1.setAttr("callType", P2PCallType.UDP_C2S);
                        response1.setAttr("callerid", targetUserId);
                        response1.setAttr("callerip", targetsession.getIp());
                        response1.setAttr("callerudpport", targetsession.getAttr("udpport"));
                        response1.setAttr("callermac", targetsession.getMac());
                        this.write(session, response1);
                        return;
                     }
                  }

                  if (count < 3 && !"UK".equals(session.getAttr("nattype")) && !"UK".equals(targetsession.getAttr("nattype"))) {
                     response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                     response1.setAttr("callType", P2PCallType.UDP_GETPORT);
                     response1.setAttr("callerid", targetUserId);
                     response1.setAttr("callermac", targetsession.getMac());
                     this.write(session, response1);
                     response2 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                     response2.setAttr("callType", P2PCallType.UDP_GETPORT);
                     response2.setAttr("callerid", caller.getId());
                     response2.setAttr("callermac", session.getMac());
                     this.write(targetsession, response2);
                  } else {
                     if (count <= 5) {
                        if (Integer.parseInt(session.getAttr("tcpport")) > 0) {
                           response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                           response1.setAttr("callType", P2PCallType.TCP_C2S);
                           response1.setAttr("callerid", caller.getId());
                           response1.setAttr("callerip", session.getIp());
                           response1.setAttr("callertcpport", session.getAttr("tcpport"));
                           response1.setAttr("callermac", session.getMac());
                           this.write(targetsession, response1);
                           return;
                        }

                        if (Integer.parseInt(targetsession.getAttr("tcpport")) > 0) {
                           response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                           response1.setAttr("callType", P2PCallType.TCP_C2S);
                           response1.setAttr("callerid", targetUserId);
                           response1.setAttr("callerip", targetsession.getIp());
                           response1.setAttr("callertcpport", targetsession.getAttr("tcpport"));
                           response1.setAttr("callermac", targetsession.getMac());
                           this.write(session, response1);
                           return;
                        }
                     }

                     if (count > 5) {
                        response.setOutput(true);
                        response.setAttr("callType", P2PCallType.TCP_SvrTrans);
                        response.setAttr("id", targetUserId);
                        response.setAttr("mac", targetsession.getMac());
                        response1 = this.createResponse(targetsession, Cmd.CALL_TO_USER_RESP);
                        response1.setAttr("callType", P2PCallType.TCP_SvrTrans);
                        response1.setAttr("id", session.getUserId());
                        response1.setAttr("mac", session.getMac());
                        this.write(targetsession, response1);
                     }

                  }
               }
            }
         } else {
            response1 = this.createResponse(session, Cmd.USER_NEED_PASS);
            response1.setAttr("id", targetUserId);
            this.write(session, response1);
         }
      }
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.appCtx = applicationContext;
   }

   @Override
   public void afterPropertiesSet() throws Exception {

      for (CallProcessor callProcessor : this.appCtx.getBeansOfType(CallProcessor.class).values()) {
         this.callProcessors.put(callProcessor.accept(), callProcessor);
      }

   }

   public boolean CheckPass(User targetuser, String password) {
      String p1 = targetuser.getAllowpass1();
      String p2 = targetuser.getAllowpass2();
      if (p1 == null && p2 == null) {
         return true;
      } else if (StringUtils.equals(p1, "") && StringUtils.equals(p2, "")) {
         return true;
      } else if (StringUtils.equals(p1, password) && !StringUtils.equals(p1, "")) {
         return true;
      } else {
         return StringUtils.equals(p2, password) && !StringUtils.equals(p2, "");
      }
   }
}
