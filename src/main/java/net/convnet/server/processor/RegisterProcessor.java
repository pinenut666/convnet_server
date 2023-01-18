package net.convnet.server.processor;

import net.convnet.server.email.Email;
import net.convnet.server.email.EmailSender;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.identity.ResetCodeManager;
import net.convnet.server.mybatis.pojo.ResetCode;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RegisterProcessor extends AbstractProcessor {
   @Value("#{props.allowregist}")
   private boolean allowregist;
   @Value("#{props.canCreateGroup}")
   private boolean canCreateGroup;
   @Value("#{props.canJoinGroup}")
   private boolean canJoinGroup;
   @Autowired
   private ResetCodeManager resetCodeManager;
   @Value("#{props['reset.url']}")
   private String resetUrl;
   @Value("#{props['forceUseMailCheck']}")
   private boolean forceUseMailCheck;
   @Value("#{props['maxRigistCount']}")
   private int maxRigistCount;
   @Autowired
   private EmailSender emailSender;

   public Cmd accept() {
      return Cmd.REGIST_USER;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      if (this.allowregist) {
         String name = request.getRequiredParam("name");
         User user = this.userManager.getUserByName(request.getRequiredParam("name"));
         if (user != null) {
            response.setAttr("status", "F");
            return;
         }

         if (this.maxRigistCount > 0) {
            int count = this.userManager.getTodayRegistUserCountFromIp(session.getIp());
            if (count >= this.maxRigistCount) {
               response.setAttr("status", "F");
               response.setAttr("info", "N");
               return;
            }
         }

         String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
         if (this.forceUseMailCheck && !name.matches(EMAIL_REGEX)) {
            response.setAttr("status", "F");
            response.setAttr("info", "M");
            return;
         }

         user = new User();
         user.setName(name);
         user.setNickName(request.getParam("nickName"));
         if (this.forceUseMailCheck) {
            user.setPassword("waitForMailConfirm");
         } else {
            user.setPassword(request.getRequiredParam("password"));
         }

         user.setDescription(request.getParam("description"));
         user.setRegisterIp(session.getIp());
         user.setCanAddfriend(this.canJoinGroup);
         user.setCanCreateGroup(this.canCreateGroup);
         this.userManager.saveUser(user);
         if (user != null && this.forceUseMailCheck) {
            ResetCode resetCode = this.resetCodeManager.createResetCode(user);
            Email e = new Email();
            e.setTo(new String[]{name});
            e.setSubject("ConVNet 密码设置");
            String url = this.resetUrl + resetCode.getId();
            e.setBody("用户: " + user.getName() + ",请点击 <a href=" + url + " target=\"_blank\">" + url + "</a> 设置Convnet密码");
            this.emailSender.send(e);
         }

         response.setAttr("status", "T");
      } else {
         response.setAttr("status", "F");
         response.setAttr("info", "N");
      }

   }
}
