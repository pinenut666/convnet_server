package net.convnet.server.web;

import net.convnet.server.common.page.PageRequest;
import net.convnet.server.common.result.CommonResult;
import net.convnet.server.identity.UserManager;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.session.Session;
import net.convnet.server.session.SessionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/***
 * @author Administrator
 * @date 2023/12/27/027 9:47
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserManager userManager;
    @Autowired
    private SessionManager sessionManager;

    //获取所有用户信息
    @PostMapping(value = "/findPage")
    public CommonResult<Object> findPage(@RequestBody PageRequest pageRequest) {
        return CommonResult.success(userManager.findPage(pageRequest));
    }

    //删除用户
    @PostMapping("delete")
    public CommonResult<Object> deleteUser(@RequestParam int id) {
        try {
            userManager.removeUser(id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.error("失败，原因为" + e.getMessage());
        }
    }

    //给用户发送信息
    @PostMapping("sendMessage")
    public CommonResult<Object> sendMessage(@RequestParam("id") int id, @RequestParam("message") String message) throws Exception {
        if (this.sessionManager.sendMessageToUser(this.userManager.getUser(id), message)) {
            return CommonResult.success("发送成功");
        }
        return CommonResult.error("发送失败");
    }

    //修改用户信息
    @PostMapping("edit")
    public CommonResult<Object> save(User user) throws Exception {
        //修改密码需要特殊加密
        if (StringUtils.isNotBlank(user.getPassword())) {
            this.userManager.setPassword(user, user.getPassword());
        }
        this.userManager.saveUser(user);
        return CommonResult.success("修改成功");
    }
    //封禁用户
    @PostMapping("serverban")
    public CommonResult<Object> serverBan( int id) throws Exception {
        Session session = this.sessionManager.getSession(id);
        if (session != null) {
            session.destory();
        }
        return CommonResult.success("封禁成功");
    }

}
