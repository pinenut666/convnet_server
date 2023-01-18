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

import java.util.Collections;

@Service
public class CreateGroupProcessor extends AbstractProcessor {
   public Cmd accept() {
      return Cmd.CREATE_GROUP;
   }

   public void process(Session session, Request request, Response response) throws ConvnetException {
      Group group = this.groupManager.getGroupByName(request.getParam("groupname"));
      if (group != null) {
         response.setAttr("iscreated", "F");
      } else {
         Group group1 = new Group();
         group1.setName(request.getParam("groupname"));
         User user = session.getUser();
         //如果用户可以创建Group
         if (user.getCanCreateGroup()) {
            //设置创建者
            group1.setCreatorId(user.getId());

            //设置账号密码
            group1.setPassword(request.getParam("gourppass"));
            group1.setDescription(request.getParam("groupdesc"));
            //组内添加该用户
            //group1.setUsers(Collections.singletonList(user));
            //只有保存group后我们才能够设置管理员等属性，否则会找不到外键？
            this.groupManager.saveGroup(group1);
            //设置管理员
            groupManager.setGroupAdminList(group1.getId(),Collections.singleton(user));
            //用户添加这个组
            this.userManager.saveUser(user);
            //用户加入该组，更新用户组表
            this.userManager.addGroupToUser(user.getId(), group1.getId());
            response.setAttr("iscreated", "S");
         } else {
            response.setAttr("iscreated", "N");
         }
      }

   }
}
