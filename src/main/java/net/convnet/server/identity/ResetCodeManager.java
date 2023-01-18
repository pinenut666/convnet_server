package net.convnet.server.identity;

import net.convnet.server.mybatis.pojo.ResetCode;
import net.convnet.server.mybatis.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface ResetCodeManager {
   ResetCode findByUserID(int var1);

   ResetCode createResetCode(User var1);

   User getUserByResetCode(String var1);

   void removeResetCode(String var1);
}
