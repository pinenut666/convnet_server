package net.convnet.server.mybatis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.convnet.server.mybatis.cache.MybatisCache;
import net.convnet.server.mybatis.pojo.Group;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-01-17
 */
@Mapper
@CacheNamespace(implementation= MybatisCache.class,eviction=MybatisCache.class)
public interface GroupMapper extends BaseMapper<Group> {

}
