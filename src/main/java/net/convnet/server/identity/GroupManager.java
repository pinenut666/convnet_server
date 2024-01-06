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
   /**
    * 根据ID获取一个组
    *
    * @param id 组ID
    * @return {@link Group}
    */
   Group getGroup(int id);

   /**
    * 按用户 ID 获取组
    *
    * @param userid 用户 ID
    * @return {@link List}<{@link Group}>
    */
   List<Group> getGroupByUserId(Integer userid);

   /**
    * 获取群组管理员列表
    *
    * @param group_id 组 ID
    * @return {@link List}<{@link Integer}>
    */
   List<Integer> getGroupAdminList(Integer group_id);

   /**
    * 设置群组管理员列表
    *
    * @param group_id 组 ID
    * @param admins   管理员
    * @return {@link Boolean}
    */
   Boolean setGroupAdminList(Integer group_id, Set<User> admins);

   /**
    * 按名称获取组
    *
    * @param var1 变量1
    * @return {@link Group}
    */
   Group getGroupByName(String var1);

   /**
    * 客户端查找组
    *
    * @param var1 搜索方式（用户名，昵称，描述）
    * @param var2
    * @param var3 变量3
    * @return {@link List}<{@link Group}>
    */
   List<Group> findGroup(FindType var1, String var2, int var3);

   /**
    * 保存组
    *
    * @param var1 变量1
    * @return {@link Group}
    *///保存这个组（如果没有则新增）
   Group saveGroup(Group var1);

   /**
    * 删除组
    *
    * @param var1 变量1
    */
   void removeGroup(int var1);

   /**
    * 发送组请求
    *
    * @param var1 变量1
    * @param var2 变量2
    * @param var3 变量3
    */
   void sendGroupRequest(int var1, int var2, String var3);

   /**
    * 交易组请求
    *
    * @param var1 变量1
    * @param var2 变量2
    * @param var3 变量3
    * @return {@link GroupRequest}
    */
   GroupRequest dealGroupRequest(int var1, int var2, boolean var3);

   /**
    * 获取组请求
    *
    * @param var1 变量1
    * @param var2 变量2
    * @return {@link GroupRequest}
    */
   GroupRequest getGroupRequest(int var1, int var2);

   /**
    * 加入群组
    *
    * @param var1 变量1
    * @param var2 变量2
    */
   void joinGroup(User var1, Group var2);

   /**
    * 退出组
    *
    * @param var1 变量1
    * @param var2 变量2
    */
   void quitGroup(User var1, Group var2);

   /**
    * 分页查找对应信息
    *
    * @param pageRequest 页面请求
    * @return {@link PageResult}
    */
   PageResult findPage(PageRequest pageRequest);
}
