package net.convnet.server.mybatis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.convnet.server.mybatis.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigInteger;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-01-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
