package net.convnet.server.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-01-17
 */
@TableName("CVN_GROUP_USER")
public class GroupUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer groupId;

    private Integer userId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GroupUser{" +
            "groupId = " + groupId +
            ", userId = " + userId +
        "}";
    }
}
