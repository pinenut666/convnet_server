package net.convnet.server.processor;

import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.protocol.AbstractProcessor;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.Request;
import net.convnet.server.protocol.Response;
import net.convnet.server.session.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ModifyGroupProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.MODIFY_GROUP;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      Group group1 = this.groupManager.getGroup(request.getIntParam("groupid"));
      if (!StringUtils.equals(request.getParam("pass"), "NOCHANGE")) {
         group1.setPassword(request.getParam("pass"));
      }

      group1.setDescription(request.getParam("desc"));
      this.groupManager.saveGroup(group1);
      response.setAttr("groupid", request.getIntParam("groupid"));
   }
}
