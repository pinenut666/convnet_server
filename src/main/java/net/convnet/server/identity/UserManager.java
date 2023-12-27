package net.convnet.server.identity;

import net.convnet.server.common.page.PageRequest;
import net.convnet.server.common.page.PageResult;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.mybatis.pojo.FriendRequest;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.mybatis.pojo.UserEx;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserManager {
   //添加用户
   User getUser(int var1);
   //添加用户信息
   UserEx getUserEx(Integer userid);
   //根据组获取用户列表（这玩意不应该在groupManager吗）
   List<User> getUserByGroupId(Integer groupid);
   //获取用户朋友列表
   List<User> getUserFriends(Integer userid);
   //根据姓名获取用户
   User getUserByName(String var1);
   //根据不同的搜索方式查找用户
   List<User> findUser(FindType var1, String var2, int var3);

   //查找用户请求列表
   List<FriendRequest> getUserFriendRequests(Integer userid);
   //添加用户
   boolean addGroupToUser(Integer userid,Integer groupid);
   //验证用户
   User validateUser(String var1, String var2) throws ConvnetException;
   //设置用户密码
   void setPassword(User var1, String var2);
   //更新用户密码
   void updatePassword(int var1, String var2);
   //保存用户
   User saveUser(User var1);
   //保存用户额外信息
   UserEx saveUserEx(UserEx var1);
   //获取今日某个IP的注册人数
   int getTodayRegistUserCountFromIp(String var1);
   //移除用户
   void removeUser(int var1);
   //获取好友请求
   void sendFriendRequest(int var1, int var2, String var3);
   //处理好友请求
   void dealFriendRequest(int var1, int var2, boolean var3);
   //删除好友
   void deleteFriend(int userid,int friendid);

    PageResult findPage(PageRequest pageRequest);
}
