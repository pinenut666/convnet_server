package net.convnet.server.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import net.convnet.server.core.Server;
import net.convnet.server.core.ServerInitializer;
import net.convnet.server.core.UdpPortDetectServer;
import net.convnet.server.identity.UserManager;
import net.convnet.server.identity.impl.UserManagerImpl;
import net.convnet.server.protocol.Cmd;
import net.convnet.server.protocol.DefaultProtocolFactory;
import net.convnet.server.protocol.Protocol;
import net.convnet.server.protocol.ProtocolFactory;
import net.convnet.server.protocol.bin.BinaryProtocol;
import net.convnet.server.protocol.bin.CoderMapping;
import net.convnet.server.session.SessionManager;
import net.convnet.server.session.impl.SessionManagerImpl;
import net.convnet.server.support.encrypt.EncryptService;
import net.convnet.server.support.encrypt.RSAEncryptServiceImpl;
import net.convnet.server.support.message.MessageProvider;
import net.convnet.server.support.message.NLS;
import net.convnet.server.support.message.SpringMessageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static net.convnet.server.protocol.Cmd.*;


@Configuration
public class AppConfig extends AbstractConfig {
    @Value("${props.listen}")
    private String listen;

    @Value("${props.poolSize}")
    private int poolSize;

    @Value("${props.udpPorts}")
    private int[] udpPorts;


    @Bean
    public UserManager userManager(EncryptService encryptService) {
        UserManagerImpl userManager = new UserManagerImpl();
        userManager.setEncryptService(encryptService);
        return userManager;
    }

    //加密相关配置
    @Bean
    public EncryptService encryptService() {
        //原本是传入的某些参数,这里直接初始化了
        RSAEncryptServiceImpl rsaEncryptService = new RSAEncryptServiceImpl();
        //这么长是因为原来就这么长
        rsaEncryptService.setEncryptKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALa0vN" + "fgloabI7gj9QxJlv/3wAId/W2sXVK4BQ4kt+9LsS7L7rbsCAQc69" + "5zqJqyiit++ITl6Fz/Oakz5vDsDtGAn2Eh/OP6lmPEop9Smafn7D9pFl" + "JuVpjD6zt5a+M5ar2zkz/IGv0il0TYzLrUQKghTiSQP5DU43qV1v7+1LU" + "BAgMBAAECgYEAjZMJjgJCwbUvfX2nYckYT+pLh5hzi2t3uSRNDoOXX774" + "Yfm2VVoacA11HB/lM1JLzJ6LtndskbtXk+xa9K8GXzN/7ekyDBpRVm4uSY" + "gcFzcr00Rs8EipmF24JTad1RSAVG7lp7dUZEgJeMYFhO1+MIhvkFFTUriC" + "92avq+ReW9ECQQDflJJRgp1u+gl2n6We+AuVmZeKIIc00uoP9wJSqaOG6LV" + "UpP8ERZYeo+fXx9odlUV8RNCwXWvq8GUXwaoc0y/tAkEA0TLjIl1iPn7X+w" + "/84W5jBgqYvxrKqDOUc9wC6nZkfDlJGMtD1HvR2nqmrnxCprGbGfK5iwtQdE" + "2NG8Ro3D9u5QJACsimugm7wDONK0yN93ntTRzoY0AoZVLa+UQnt4b5HTOyM0" + "/2AOW+VOIA97Zq/cxKWrETA76tytondhrgMp25DQJAaMedHiQ5xeWQVg6O8X" + "ZxoKAEG4vWv2J+U3159GdyMwoaQ5HjmnSeCljuTWytkYJZliiNqEf1dxs0txi" + "sEMywxQJAE//uQXCZ+HRqeKQ3ckb1xYcXzqzuSCA1xdWhr+g81BQ5DvsARrg7" + "2aGTnCfkXfxwDfSa9qRPtFkuA8krc7g+7Q==");
        return rsaEncryptService;
    }

    //设置NLS
    @Bean
    public NLS nls(MessageProvider springMessageProvider) {
        NLS nls = new NLS();
        nls.setMessageProvider(springMessageProvider);
        return nls;
    }

    //嵌套 多语言 设置
    @Bean
    public SpringMessageProvider springMessageProvider(ReloadableResourceBundleMessageSource messageSource) {
        SpringMessageProvider springMessageProvider = new SpringMessageProvider();
        springMessageProvider.setMessageSource(messageSource);
        return springMessageProvider;
    }

    //多语言设置
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:message/errors,classpath:message/messages");
        return messageSource;
    }

    //分页查询设置
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }


    @Override
    public List<CoderMapping> coderMappings() {
        List<CoderMapping> coderMappings = new ArrayList<>();

        //添加关于用户登录,注册,登出,同步,在线,错误等回应
        coderMappings.add(new CoderMapping(ERROR, null, new String[]{"code", "msg"}, null, null));
        coderMappings.add(new CoderMapping(KEEP_ONLINE, null, null, null, null));
        coderMappings.add(new CoderMapping(SERVER_TRANS, null, null, new String[]{"userId", "payload"}, null));
        coderMappings.add(new CoderMapping(LOGIN, LOGIN_RESP, new String[]{"status", "ip", "id", "nickName", "name", "password", "description", "p1", "p2", "p3", "p4"}, new String[]{"name", "password", "mac"}, null));
        coderMappings.add(new CoderMapping(LOGOUT, LOGOUT_RESP, null, null, null));
        coderMappings.add(new CoderMapping(REGIST_USER, REGIST_USER_RESP, new String[]{"status", "info"}, new String[]{"name", "password", "nickName", "description"}, null));

        //获取服务器端口,版本数据发送等
        coderMappings.add(new CoderMapping(GET_VERSION_RESP, null, new String[]{"version", "updateURL"}, null, null));
        coderMappings.add(new CoderMapping(GET_SERVER_PORT, GET_SERVER_PORT_RESP, new String[]{"updPorts"}, null, null));
        coderMappings.add(new CoderMapping(CALL_TO_USER_NEW_PORT, CALL_TO_USER_NEW_PORT_RESP, new String[]{"userid,updPort"}, new String[]{"userid", "udpport"}, null));

        //上线,下线
        coderMappings.add(new CoderMapping(ONLINE_TELL_RESP, null, new String[]{"who"}, null, null));
        coderMappings.add(new CoderMapping(OFFLINE_TELL_RESP, null, new String[]{"who"}, null, null));
        //检查NAT类型,获取user讯息,告知等
        coderMappings.add(new CoderMapping(CHECK_NAT_TYPE, null, null, null, null));
        coderMappings.add(new CoderMapping(RENEW_USER_STATUS, null, null, null, null));
        coderMappings.add(new CoderMapping(ONLINE_TELL, null, null, null, null));
        //这个似乎是重复的,但我不清楚是否有影响
        coderMappings.add(new CoderMapping(KEEP_ONLINE, null, null, null, null));

        //刷新用户状态
        coderMappings.add(new CoderMapping(RENEW_USER_STATUS, null, null, new String[]{"nattype", "udpport", "tcpport"}, null));
        coderMappings.add(new CoderMapping(CHECK_NAT_TYPE, null, null, new String[]{"udpport", "tcpport"}, null));
        coderMappings.add(new CoderMapping(GET_GROUP_DESC, GET_GROUP_DESCRESP, new String[]{"groupid"}, new String[]{"groupdesc"}, null));
        coderMappings.add(new CoderMapping(MODIFY_GROUP, MODIFY_GROUP_RESP, new String[]{"groupid", "name", "desc", "pass"}, new String[]{"groupid"}, null));
        //加入,退出,被踢出
        coderMappings.add(new CoderMapping(JOIN_GROUP, JOIN_GROUP_RESP, new String[]{"username", "userid", "gourpid", "gourpdesc", "orderdesc"}, new String[]{"groupid", "description", "password"}, null));
        coderMappings.add(new CoderMapping(QUIT_GROUP, QUIT_GROUP_RESP, new String[]{"msgtype", "groupid", "userid"}, new String[]{"groupid"}, null));
        coderMappings.add(new CoderMapping(KICK_OUT, KICK_OUT_RESP, new String[]{"groupid", "userid"}, new String[]{"groupid", "userid"}, null));
        //PEER_SURE_JOIN_GROUP_RESP的反馈对象为已有组内的成员，通知新用户加入
        coderMappings.add(createCoderMapping(PEER_SURE_JOIN_GROUP, PEER_SURE_JOIN_GROUP_RESP, "groupid,whoid,name,isonline", "isallow,userid,groupid"));
        // 使用CHATGPT减少工作量
        coderMappings.add(createCoderMapping(Cmd.PEER_ORD_FRIEND, Cmd.PEER_ORD_FRIEND_RESP, "count,userId,nickName,description", "id,description"));
        coderMappings.add(createCoderMapping(Cmd.PEER_SURE_FRIEND, Cmd.PEER_SURE_FRIEND_RESP, "id,name,isonline", "id"));
        coderMappings.add(createCoderMapping(Cmd.DEL_FRIEND, Cmd.DEL_FRIEND_RESP, "id", "id"));
        // 转换第一个 XML 片段
        coderMappings.add(createCoderMapping(Cmd.PEER_REFUSED_FRIEND, Cmd.PEER_REFUSED_FRIEND_RESP, "", "id"));
// 转换第二个 XML 片段
        coderMappings.add(createCoderMapping(null, Cmd.SERVER_SEND_TO_CLIENT, "message", ""));

// 转换第三个 XML 片段
        coderMappings.add(createCoderMapping(Cmd.SAMEIP_INFO, Cmd.SAMEIP_INFO_RESP, "callerid,callerudpport,callertcpport,callermac,callerinnerip", "targetid,myudpport,mytcpport,mymac,myinnerip"));

// 转换第四个 XML 片段
        coderMappings.add(createCoderMapping(null, Cmd.USER_NEED_PASS, "id", ""));

// 转换第五个 XML 片段
        coderMappings.add(createCoderMapping(Cmd.ONLINE_TELL, null, "userid", ""));

// 转换第六个 XML 片段
        coderMappings.add(createCoderMapping(Cmd.CREATE_GROUP, Cmd.CREATE_GROUP_RESP, "iscreated","groupname,groupdesc,gourppass"));


        return coderMappings;
    }

    @Bean
    public BinaryProtocol binaryProtocol(List<CoderMapping> coderMappings){
        BinaryProtocol binaryProtocol = new BinaryProtocol();
        binaryProtocol.setCoderMappings(coderMappings);
        return binaryProtocol;
    }
    @Bean
    public DefaultProtocolFactory protocolFactory(BinaryProtocol binaryProtocol) {
        DefaultProtocolFactory factory = new DefaultProtocolFactory();
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(binaryProtocol);
        factory.setProtocols(protocols);
        return factory;
    }

    @Bean
    public SessionManager sessionManager(ProtocolFactory protocolFactory, UserManager userManager) {
        SessionManagerImpl sessionManager = new SessionManagerImpl();
        sessionManager.setProtocolFactory(protocolFactory);
        sessionManager.setUserManager(userManager);
        return sessionManager;
    }

    @Bean
    public ServerInitializer serverInitializer(ProtocolFactory protocolFactory,SessionManager sessionManager) {
        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.setSessionManager(sessionManager);
        serverInitializer.setProtocolFactory(protocolFactory);
        return serverInitializer;
    }

    @Bean
    public Server convnetServer(ServerInitializer serverInitializer) {
        Server server = new Server();
        server.setListen(listen);
        server.setPoolSize(poolSize);
        server.setServerInitializer(serverInitializer);
        return server;
    }

    @Bean
    public UdpPortDetectServer udpPortDetectServer() {
        UdpPortDetectServer udpPortDetectServer = new UdpPortDetectServer();
        udpPortDetectServer.setPorts(udpPorts);
        return udpPortDetectServer;
    }


    private CoderMapping createCoderMapping(Cmd cmd, Cmd rCmd, String encode, String decode) {
        CoderMapping coderMapping = new CoderMapping();
        String[] sureEncode = encode.split(",");
        String[] sureDecode = decode.split(",");
        coderMapping.setCmd(cmd);
        coderMapping.setrCmd(rCmd);
        coderMapping.setEncode(sureEncode);
        coderMapping.setDecode(sureDecode);
        return coderMapping;
    }
}