package net.convnet.server.email;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Future;

@Service
public class EmailSenderImpl implements EmailSender{
    @Override
    public Future<Email> send(Email var1) {
        //设置发送方
        String[] setTo = var1.getTo();
        //设置主题
        String subject = var1.getSubject();
        //设置content
        String content = var1.getBody();
        MailUtil.send(Arrays.asList(setTo), subject, content, true);
        return null;
    }
}
