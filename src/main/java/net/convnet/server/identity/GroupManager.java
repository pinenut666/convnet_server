package net.convnet.server.identity;

import net.convnet.server.common.page.PageRequest;
import net.convnet.server.common.page.PageResult;
import net.convnet.server.mybatis.pojo.Group;
import net.convnet.server.mybatis.pojo.GroupRequest;
import net.convnet.server.mybatis.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public interface GroupManager {
   //根据ID获取一个组
   Group getGroup(int var1);

   //根据用户ID获取用户所在的组
   List<Group> getGroupByUserId(Integer userid);

   //获取组内管理员ID(和用户保持一致)
   List<Integer> getGroupAdminList(Integer group_id);

   //设置组的管理员
   Boolean setGroupAdminList(Integer group_id, Set<User> admins);

   //根据组名获取组
   Group getGroupByName(String var1);

   //提供给客户端的方法
   List<Group> findGroup(FindType var1, String var2, int var3);
   //保存这个组（如果没有则新增）
   Group saveGroup(Group var1);

   void removeGroup(int var1);

   void sendGroupRequest(int var1, int var2, String var3);

   GroupRequest dealGroupRequest(int var1, int var2, boolean var3);

   GroupRequest getGroupRequest(int var1, int var2);

   void joinGroup(User var1, Group var2);

   void quitGroup(User var1, Group var2);

   PageResult findPage(PageRequest pageRequest);
}
