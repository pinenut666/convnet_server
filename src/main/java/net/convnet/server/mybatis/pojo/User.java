package net.convnet.server.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@TableName("CVN_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Boolean admin;

    private String allowpass1;

    private String allowpass2;

    private Boolean canAddfriend;

    private Boolean canCreateGroup;

    private LocalDateTime createAt;

    private String description;

    private String dospass;

    private String email;

    private Boolean enabled;

    private String friendpass;

    private String name;

    private String nickName;

    private String password;

    private String passwordHash;

    private Long reciveLimit;

    private String registerIp;

    private Long sendLimit;

    private LocalDateTime updateAt;

    public boolean SoIfICanAddfriend() {
        return this.canAddfriend;
    }


    public String[] getConnectPasswords() {
        return new String[]{this.allowpass1, this.allowpass2, this.friendpass, this.dospass};
    }
    @Override
    public String toString() {
        return "User{" +
            "id = " + id +
            ", admin = " + admin +
            ", allowpass1 = " + allowpass1 +
            ", allowpass2 = " + allowpass2 +
            ", canAddfriend = " + canAddfriend +
            ", canCreateGroup = " + canCreateGroup +
            ", createAt = " + createAt +
            ", description = " + description +
            ", dospass = " + dospass +
            ", email = " + email +
            ", enabled = " + enabled +
            ", friendpass = " + friendpass +
            ", name = " + name +
            ", nickName = " + nickName +
            ", password = " + password +
            ", passwordHash = " + passwordHash +
            ", reciveLimit = " + reciveLimit +
            ", registerIp = " + registerIp +
            ", sendLimit = " + sendLimit +
            ", updateAt = " + updateAt +
        "}";
    }
}
