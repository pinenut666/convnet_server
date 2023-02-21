package net.convnet.server.web.pojo;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBean {
    /** 用户名 */
    private String name;
    /** 密码 */
    private String password;
    /** 验证码 */
    private String captcha;

    @Override
    public String toString()
    {
        //返回JSON的属性
        return JSON.toJSONString(this);
    }

}
