package net.convnet.server.identity.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.convnet.server.identity.ResetCodeManager;
import net.convnet.server.mybatis.mapper.ResetCodeMapper;
import net.convnet.server.mybatis.mapper.UserMapper;
import net.convnet.server.mybatis.pojo.FriendRequest;
import net.convnet.server.mybatis.pojo.ResetCode;
import net.convnet.server.mybatis.pojo.User;
import net.convnet.server.util.DateUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class ResetCodeManagerImpl implements ResetCodeManager {
   @Autowired
   protected ResetCodeMapper resetCodeMapper;
   @Autowired
   protected UserMapper userMapper;



   @Transactional
   public ResetCode findByUserID(int userid) {
      LambdaQueryWrapper<ResetCode> wrapper = new LambdaQueryWrapper<>();
      wrapper.eq(ResetCode::getUserId,userid);
      return resetCodeMapper.selectOne(wrapper);
   }

   @Transactional
   public ResetCode createResetCode(User user) {
      ResetCode resetCode = this.findByUserID(user.getId());
      if (resetCode == null) {
         resetCode = new ResetCode();
         resetCode.setId(RandomStringUtils.randomAlphanumeric(8));
         User user1 = userMapper.selectById(user.getId());
         resetCode.setUserId(user.getId());
         Date now = new Date();
         resetCode.setCreateAt(DateUtil.getLocalDateTime(now));
         resetCode.setExpireAt(DateUtil.getLocalDateTime(DateUtils.addHours(now, 1)));
         resetCodeMapper.insert(resetCode);
      } else {
         Date now = new Date();
         resetCode.setCreateAt(DateUtil.getLocalDateTime(now));
         resetCode.setExpireAt(DateUtil.getLocalDateTime(DateUtils.addHours(now, 1)));
         resetCodeMapper.insert(resetCode);
      }

      return resetCode;
   }

   @Transactional(
      readOnly = true
   )
   public User getUserByResetCode(String code) {
      ResetCode resetCode = resetCodeMapper.selectById(code);
      return userMapper.selectById(resetCode.getUserId());
   }

   @Transactional
   public void removeResetCode(String code) {
      resetCodeMapper.deleteById(code);
   }
}
