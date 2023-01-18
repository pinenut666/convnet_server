package net.convnet.server.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-01-17
 */
@TableName("CVN_RESET_CODE")
public class ResetCode implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private LocalDateTime createAt;

    private LocalDateTime expireAt;

    private Integer userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(LocalDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ResetCode{" +
            "id = " + id +
            ", createAt = " + createAt +
            ", expireAt = " + expireAt +
            ", userId = " + userId +
        "}";
    }
}
