package net.convnet.server.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("CVN_USER_EX")
public class UserEx implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    private String attr;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    private Long reciveFromServer;

    private Long sendToServer;

    private Boolean userIsOnline;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Long getReciveFromServer() {
        return reciveFromServer;
    }

    public void setReciveFromServer(Long reciveFromServer) {
        this.reciveFromServer = reciveFromServer;
    }

    public Long getSendToServer() {
        return sendToServer;
    }

    public void setSendToServer(Long sendToServer) {
        this.sendToServer = sendToServer;
    }

    public Boolean getUserIsOnline() {
        return userIsOnline;
    }

    public void setUserIsOnline(Boolean userIsOnline) {
        this.userIsOnline = userIsOnline;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UserEx{" +
            "id = " + id +
            ", attr = " + attr +
            ", lastLoginAt = " + lastLoginAt +
            ", lastLoginIp = " + lastLoginIp +
            ", reciveFromServer = " + reciveFromServer +
            ", sendToServer = " + sendToServer +
            ", userIsOnline = " + userIsOnline +
            ", userId = " + userId +
        "}";
    }
}
