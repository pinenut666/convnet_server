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
@TableName("CVN_FRIEND")
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer friendId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "Friend{" +
            "userId = " + userId +
            ", friendId = " + friendId +
        "}";
    }
}
