package net.convnet.server.identity.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.convnet.server.ex.ConvnetException;
import net.convnet.server.ex.EntityNotFoundException;
import net.convnet.server.identity.*;
import net.convnet.server.mybatis.mapper.*;
import net.convnet.server.mybatis.pojo.*;
import net.convnet.server.support.encrypt.EncryptService;
import net.convnet.server.util.Codecs;
import net.convnet.server.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(
   readOnly = false
)
@Service
public class UserManagerImpl implements UserManager {
   public String salt = "_t[er,z59)]x7c&d;x24v%";
   private EncryptService encryptService;
   @Autowired
   protected UserMapper userMapper;
   @Autowired
   protected UserExMapper userExMapper;
   @Autowired
   protected FriendRequestMapper friendRequestMapper;
   @Autowired
   protected FriendMapper friendMapper;
   @Autowired
   protected GroupUserMapper groupUserMapper;


   //所以这些盐的部分都没有了咩？
   public void setSalt(String salt) {
      this.salt = salt;
   }

   public void setEncryptService(EncryptService encryptService) {
      this.encryptService = encryptService;
   }

   //获取user
   public User getUser(int id) {

      return userMapper.selectById(id);

   }



   //根据user获取userex
   @Override
   public UserEx getUserEx(Integer userid) {
      LambdaQueryWrapper<UserEx> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(UserEx::getUserId, userid);
      return userExMapper.selectOne(wrapper);
   }

   //根据组获取组内所有用户
   @Override
   public List<User> getUserByGroupId(Integer groupid) {
      LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupUser::getGroupId, groupid);
      List<GroupUser> groupUserList = groupUserMapper.selectList(wrapper);
      List<User> users = new ArrayList<>();
      for(GroupUser groupUser:groupUserList)
      {
         users.add(userMapper.selectById(groupUser.getUserId()));
      }
      return users;
   }

   //根据用户获取用户朋友列表
   @Override
   public List<User> getUserFriends(Integer userid) {
      LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(Friend::getUserId, userid);
      List<Friend> friends = friendMapper.selectList(wrapper);
      List<User> userList = new ArrayList<>();
      for(Friend friend:friends)
      {
         userList.add(userMapper.selectById(friend.getFriendId()));
      }
      return userList;
   }

   public User getUserByName(String userName) {
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(User::getName, userName);
      return userMapper.selectOne(wrapper);
   }

   public List<User> findUser(FindType type, String value, int size) {
      String fieldName = null;
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
      switch(type) {
      case NAME:
         fieldName = "name";
         break;
      case NICK_NAME:
         fieldName = "nickName";
         break;
      case DESCRIPTION:
         fieldName = "description";
      }
      if("name".equals(fieldName))
      {
         wrapper.like(User::getName,value);
         return userMapper.selectList(wrapper);
      }
      if("nickName".equals(fieldName))
      {
         wrapper.like(User::getNickName,value);
         return userMapper.selectList(wrapper);
      }
      if("description".equals(fieldName))
      {
         wrapper.like(User::getDescription,value);
         return userMapper.selectList(wrapper);
      }
      return null;
   }

   @Override
   public List<FriendRequest> getUserFriendRequests(Integer userid) {
      LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(FriendRequest::getUserId,userid);
      return friendRequestMapper.selectList(wrapper);
   }

   @Override
   public boolean addGroupToUser(Integer userid, Integer groupid) {
      //设置并插入
      GroupUser groupUser = new GroupUser();
      groupUser.setGroupId(groupid);
      groupUser.setUserId(userid);
      return groupUserMapper.insert(groupUser)!=0;
   }

   //TODO:修正分页问题
/*   public Page<User> findUser(String name, Pageable request, boolean isOnline) {
      Criteria criteria = this.criteria(new Criterion[0]);
      if (StringUtils.isNotEmpty(name)) {
         criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE));
      }

      if (isOnline) {
         criteria.createAlias("userEx", "userEx");
         criteria.add(Restrictions.eq("userEx.userIsOnline", isOnline));
      }

      return this.find(criteria.addOrder(Order.desc("id")), request);
   }*/

   //鉴权
   public User validateUser(String userName, String password) throws ConvnetException {
      password = formatPassword(password);
      User user = this.getUserByName(userName);
      if (user == null) {
         throw new ConvnetException("USER_NOUSER");
      }
      //使用到了是否启动的鉴别
      else if (!user.getEnabled()) {
         throw new ConvnetException("USER_DISABLED");
      } else if (!user.getPasswordHash().equals(Codecs.hashHex(password + this.salt))) {
         throw new ConvnetException("USER_ERRORPASS");
      } else {
         return user;
      }
   }

   @Transactional
   public void updatePassword(int id, String password) {
      User user = this.getUser(id);
      this.setPassword(user, password);
      this.saveUser(user);
   }

   private static String formatPassword(String password) {
      password = StringUtils.trimToNull(password);
      if (password == null) {
         throw new IllegalArgumentException("Password can not be null");
      } else {
         return password;
      }
   }

   public void setPassword(User user, String password) {
      password = formatPassword(password);
      user.setPasswordHash(Codecs.hashHex(password + this.salt));

      try {
         user.setPassword(this.encryptService.encrypt(password));
      } catch (GeneralSecurityException var4) {
         var4.printStackTrace();
      }

   }

   @Transactional
   //该方法调用时，不能确保用户主键一定存在
   //需要我们手动插入后，再获取主键
   public User saveUser(User user) {
      if (user.getId() == null) {
         user.setCreateAt(DateUtil.getLocalDateTime(new Date()));
         if(user.getAdmin()==null)
         {
            user.setAdmin(false);
         }
         //使用自定义ID注解，生成ID
         userMapper.insert(user);
         //需要我们手动初始化userEx
         UserEx userEx = new UserEx();
         userEx.setUserIsOnline(false);
         userEx.setUserId(user.getId());
         userEx.setReciveFromServer(0L);
         userEx.setSendToServer(0L);
         userExMapper.insert(userEx);
         this.setPassword(user, user.getPassword());
      }
      user.setUpdateAt(DateUtil.getLocalDateTime(new Date()));
      if(userMapper.selectById(user.getId())!=null)
      {
         userMapper.updateById(user);
      }
      else
      {
         userMapper.insert(user);
      }

      return userMapper.selectById(user.getId());
   }

   @Transactional
   public UserEx saveUserEx(UserEx userEx) {
      //使用替代品查询
      LambdaQueryWrapper<UserEx> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(UserEx::getUserId,userEx.getUserId());
      UserEx verify = userExMapper.selectOne(wrapper);
      if(verify==null)
      {
         //没查到，添加参数
         userExMapper.insert(userEx);
      }
      else
      {
         //进行ID更新
         userEx.setId(verify.getId());
         userExMapper.updateById(userEx);
      }
      //重新获取数据
      return userExMapper.selectById(verify);
   }

   public int getTodayRegistUserCountFromIp(String IP) {
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
      wrapper.gt(User::getCreateAt,DateUtil.getLocalDateTime(new Date())).eq(User::getRegisterIp,IP);
      return userMapper.selectCount(wrapper);
   }

   @Transactional
   public void removeUser(int id) {
      userMapper.deleteById(id);
   }

   @Transactional
   public void sendFriendRequest(int userId, int targetUserId, String description) {
      FriendRequest request = this.getFriendRequest(userId, targetUserId);
      if (request == null) {
         request = new FriendRequest();
         request.setCreateAt(DateUtil.getLocalDateTime(new Date()));
         request.setTargetId(targetUserId);
         request.setUserId(userId);
      }
      request.setDescription(description);
      //TODO:弄清到底是不是插入
      friendRequestMapper.insert(request);
   }

   @Transactional
   public void dealFriendRequest(int userId, int targetUserId, boolean reject) {
      FriendRequest request = this.getFriendRequest(userId, targetUserId);
      if (request == null) {
         throw new EntityNotFoundException(FriendRequest.class, "userId=" + userId + ",targetUserId=" + targetUserId);
      } else {
         //如果没有拒绝，调整对应的表，互相添加好友
         if (!reject) {
            Integer userid = request.getUserId();
            Integer targetid = request.getTargetId();
            Friend friend = new Friend();
            friend.setUserId(userid);
            friend.setFriendId(targetid);
            Friend friend2 = new Friend();
            friend2.setUserId(targetid);
            friend2.setFriendId(userid);
            friendMapper.insert(friend);
            friendMapper.insert(friend2);
         }
         friendRequestMapper.deleteById(request);
      }
   }

   @Override
   public void deleteFriend(int userid, int friendid) {
      //没有唯一键
      LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(Friend::getUserId,userid).eq(Friend::getFriendId,friendid);
      friendMapper.delete(wrapper);
   }

   private FriendRequest getFriendRequest(int userId, int targetUserId) {
      LambdaQueryWrapper<FriendRequest> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(FriendRequest::getUserId,userId).eq(FriendRequest::getTargetId,targetUserId);
      return friendRequestMapper.selectOne(wrapper);
   }
}
