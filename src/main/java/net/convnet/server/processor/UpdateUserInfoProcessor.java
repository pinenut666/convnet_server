package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserInfoProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.RENEW_MY_INFO;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      User user = session.getUser();
      user.setNickName(request.getParam("nickName"));
      user.setAllowpass1(request.getParam("p1"));
      user.setAllowpass2(request.getParam("p2"));
      user.setFriendpass(request.getParam("p3"));
      user.setDospass(request.getParam("p4"));
      user.setDescription(request.getParam("description"));
      this.userManager.saveUser(user);
      String tmpstr = StringUtils.trim(request.getParam("password"));
      if (!tmpstr.equals("　") && !tmpstr.equals("") && !tmpstr.equals("NOTMODIFYED")) {
         this.userManager.updatePassword(user.getId(), request.getParam("password"));
      }

   }
}
