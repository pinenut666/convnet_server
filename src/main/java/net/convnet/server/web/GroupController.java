package net.convnet.server.web;

import lombok.extern.slf4j.Slf4j;
import net.convnet.server.common.page.PageRequest;
import net.convnet.server.common.page.PageResult;
import net.convnet.server.common.result.CommonResult;
import net.convnet.server.identity.GroupManager;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/group")

/**
 * 用户组创建
 * 部分源代码来源：goufn
 * @author Administrator
 */
public class GroupController {
   @Autowired
   private GroupManager groupManager;
   @Autowired
   private UserManager userManager;
   //获取所有组信息->使用分页插件实现
   @PostMapping(value="/findPage")
   public CommonResult<Object> findPage(@RequestBody PageRequest pageRequest)
   {
      return CommonResult.success(groupManager.findPage(pageRequest));
   }
   //增加新组（新组需要创建者ID）
   @PostMapping("create")
   public CommonResult<Object> createNewGroup(@RequestBody Group group)
   {
      return CommonResult.success(groupManager.saveGroup(group));
   }
   //修改组
   @PostMapping("change")
   public CommonResult<Object> changeGroup(@RequestBody Group group)
   {
      if(userManager.getUser(group.getCreatorId())==null)
      {
         //说明创建者ID非法，不允许创建
         return CommonResult.error("创建者不存在，请重新输入创建者ID");
      }
      if(group.getId()==null)
      {
         return CommonResult.error("传参未能检测到组ID，请刷新页面！");
      }
      return CommonResult.success(groupManager.saveGroup(group));
   }
   //删除组
   @PostMapping("delete")
   public CommonResult<Object> deleteGroup(@RequestParam int id){
      try
      {
         groupManager.removeGroup(id);
         return CommonResult.success("删除成功");
      }
      catch(Exception e)
      {
         return CommonResult.error("失败，原因为" + e.getMessage());
      }
   }
   //查询组内用户信息
   @PostMapping("getgroupmember")
   public CommonResult<Object> getUserByGroupId(@RequestParam int id)
   {
      //很显然，我写错了这个方法的位置……
      return CommonResult.success(userManager.getUserByGroupId(id));
   }
}
