package net.convnet.server.identity.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.convnet.server.common.page.ColumnFilter;
import net.convnet.server.common.page.PageRequest;
import net.convnet.server.common.page.PageResult;
import net.convnet.server.ex.EntityNotFoundException;
import net.convnet.server.identity.*;
import net.convnet.server.mybatis.mapper.GroupAdminMapper;
import net.convnet.server.mybatis.mapper.GroupMapper;
import net.convnet.server.mybatis.mapper.GroupRequestMapper;
import net.convnet.server.mybatis.mapper.GroupUserMapper;
import net.convnet.server.mybatis.pojo.*;
import net.convnet.server.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(
   readOnly = false
)
@Service
public class GroupManagerImpl implements GroupManager {
   @Autowired
   protected UserManager userManager;

   //MyBatis添加内容
   @Autowired
   protected GroupMapper groupMapper;
   @Autowired
   protected GroupRequestMapper groupRequestMapper;
   @Autowired
   protected GroupUserMapper groupUserMapper;
   @Autowired
   protected GroupAdminMapper groupAdminMapper;


   //根据主键ID获取Group
   @Override
   public Group getGroup(int id) {
      return groupMapper.selectById(id);
   }

   //根据用户ID获取组
   @Override
   public List<Group> getGroupByUserId(Integer userid) {
      LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupUser::getUserId, userid);
      List<GroupUser> groupUserList = groupUserMapper.selectList(wrapper);
      List<Group> groups = new ArrayList<>();
      for(GroupUser groupUser:groupUserList)
      {
         groups.add(groupMapper.selectById(groupUser.getGroupId()));
      }
      return groups;
   }

   //获取组内管理员ID(和用户保持一致)
   @Override
   public List<Integer> getGroupAdminList(Integer group_id)
   {
      LambdaQueryWrapper<GroupAdmin> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupAdmin::getGroupId, group_id);
      List<Integer> adminList = new ArrayList<>();
      List<GroupAdmin> groupUserList = groupAdminMapper.selectList(wrapper);
      for(GroupAdmin groupAdmin:groupUserList)
      {
         adminList.add(groupAdmin.getUserId());
      }
      return adminList;
   }

   @Override
   //TODO:修正该方法，该方法目前尚不完善
   public Boolean setGroupAdminList(Integer group_id, Set<User> admins) {
      //根据groupid插入
      GroupAdmin groupAdmin = new GroupAdmin();

      for(User admin:admins)
      {
         groupAdmin.setGroupId(group_id);
         groupAdmin.setUserId(admin.getId());
         groupAdminMapper.insert(groupAdmin);
      }
      return true;
   }

   //根据名称获取ID
   @Override
   public Group getGroupByName(String groupName) {
      LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(Group::getName, groupName);
      //return (Group)this.getByNaturalId("name", groupName);
      return groupMapper.selectOne(wrapper);
   }
   //根据查询信息查询组，看上去像是给前端查询对应分组用的。
   @Override
   public List<Group> findGroup(FindType type, String value, int size) {
      String fieldName = null;
      switch(type) {
      case NICK_NAME:
         fieldName = "name";
         break;
      case DESCRIPTION:
         fieldName = "description";
         break;
      default:
         fieldName = "name";
      }
      LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
      if("name".equals(fieldName))
      {
         wrapper.like(Group::getName,value);
         //TODO:观察并分析size参数
         return groupMapper.selectList(wrapper);
      }
      else
      {
         wrapper.like(Group::getDescription,value);
         return groupMapper.selectList(wrapper);
      }

      //return this.list(this.criteria(new Criterion[]{Restrictions.like(fieldName, value, MatchMode.ANYWHERE)}).addOrder(Order.asc("name")).setMaxResults(size));
   }

   @Override
   @Transactional
   public Group saveGroup(Group group) {
      if (group.getId() == null) {
         group.setCreateAt(DateUtil.getLocalDateTime(new Date()));
         //默认设置启用组
         group.setEnabled(true);
      }
      //修改先查询一次
      if(groupMapper.selectById(group.getId())!=null)
      {
         groupMapper.updateById(group);
      }
      else
      {
         groupMapper.insert(group);
      }
      //TODO:不一致的逻辑
      return groupMapper.selectById(group.getId());
   }

   @Override
   @Transactional
   public void removeGroup(int id) {
      //由于JPA的缘故，jpa帮我们处理好了删除的问题
      //但是我们这里要手工删掉组和用户的对应关系，再删除组
      //虽然感觉确实很麻烦，也不知道有没有更好的解决方案
      LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupUser::getGroupId,id);
      LambdaQueryWrapper<GroupAdmin> wrapper2 = new LambdaQueryWrapper<>();
      wrapper2.eq(GroupAdmin::getGroupId,id);
      groupAdminMapper.delete(wrapper2);
      groupUserMapper.delete(wrapper);
      groupMapper.deleteById(id);
   }

   @Override
   @Transactional
   public void sendGroupRequest(int userId, int targetGroupId, String description) {
      LambdaQueryWrapper<GroupRequest> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupRequest::getUserId,userId).eq(GroupRequest::getTargetId,targetGroupId);
      GroupRequest request = groupRequestMapper.selectOne(wrapper);
      if (request == null) {
         request = new GroupRequest();
         request.setCreateAt(DateUtil.getLocalDateTime(new Date()));
         //由于Mybatis和JPA的机制不同,我们要手动插入更新目标
         //目前尚不清楚对前部分的影响，但缺失对应时，再回来添加mybatis的对应
         request.setTargetId(targetGroupId);
         request.setUserId(userId);
      }
      request.setDescription(description);
      //相当于保存
      groupRequestMapper.insert(request);
   }

   @Override
   public GroupRequest getGroupRequest(int userId, int groupid) {
      LambdaQueryWrapper<GroupRequest> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupRequest::getUserId,userId).eq(GroupRequest::getTargetId,groupid);
      return groupRequestMapper.selectOne(wrapper);
   }

   @Override
   @Transactional
   public GroupRequest dealGroupRequest(int userid, int groupid, boolean reject) {
      GroupRequest request = this.getGroupRequest(userid, groupid);
      if (request == null) {
         throw new EntityNotFoundException(GroupRequest.class, "id=" + userid);
      } else {
         groupRequestMapper.deleteById(request.getId());
         return null;
      }
   }

   @Override
   @Transactional
   //根据组查询用户是否可以加入
   //查询表cvn_group_user（原标记）
   public void joinGroup(User user, Group group) {
      LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupUser::getGroupId,group.getId());

      for(GroupUser groupUser:groupUserMapper.selectList(wrapper))
      {
         if(Objects.equals(groupUser.getUserId(), user.getId()))
         {
            //查找成功，已经在组里了，什么都不做
            return;
         }
      }
      //否则没查找成功，写入（这个写入是和jpa有关的，我们这样是不行的，要自己把自己加进去……
      this.saveGroup(group);
      //添加对应
      GroupUser groupUser = new GroupUser();
      groupUser.setGroupId(group.getId());
      groupUser.setUserId(user.getId());
      groupUserMapper.insert(groupUser);
      //TODO:目前尚不明确此处有无更多影响
      this.userManager.saveUser(user);
   }

   @Override
   @Transactional
   public void quitGroup(User user, Group group) {
      LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(GroupUser::getGroupId,group.getId());

      for(GroupUser groupUser:groupUserMapper.selectList(wrapper))
      {
         if(Objects.equals(groupUser.getUserId(), user.getId()))
         {
            LambdaQueryWrapper<GroupUser> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(GroupUser::getUserId,user.getId());
            //查找成功，在组里，退出组
            groupUserMapper.delete(wrapper2);
         }
      }
   }

   @Override
   public PageResult findPage(PageRequest pageRequest) {
      ColumnFilter columnFilter = pageRequest.getColumnFilters().get("name");
      LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
      if (columnFilter != null && !StringUtils.isEmpty(columnFilter.getValue())) {
         wrapper.eq(Group::getName, columnFilter.getValue());
      }
      int pageNum = pageRequest.getPageNum();
      int pageSize = pageRequest.getPageSize();
      Page<Group> page = new Page<>(pageNum, pageSize);
      IPage<Group> result = groupMapper.selectPage(page, wrapper);
      return new PageResult(result);
   }
}
