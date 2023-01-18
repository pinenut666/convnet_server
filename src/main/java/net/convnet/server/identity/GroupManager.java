package net.convnet.server.identity;

import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.mybatis.pojo.GroupRequest;
import net.convnet.server.mybatis.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public interface GroupManager {
   Group getGroup(int var1);

   List<Group> getGroupByUserId(Integer userid);

   //获取组内管理员ID(和用户保持一致)
   List<Integer> getGroupAdminList(Integer group_id);

   //设置组的管理员
   Boolean setGroupAdminList(Integer group_id, Set<User> admins);

   Group getGroupByName(String var1);

   List<Group> findGroup(FindType var1, String var2, int var3);

   Group saveGroup(Group var1);

   void removeGroup(int var1);

   void sendGroupRequest(int var1, int var2, String var3);

   GroupRequest dealGroupRequest(int var1, int var2, boolean var3);

   GroupRequest getGroupRequest(int var1, int var2);

   void joinGroup(User var1, Group var2);

   void quitGroup(User var1, Group var2);

   List<Group> getAllGroup();
}
