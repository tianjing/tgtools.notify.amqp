package tgtools.notify.rabbitmq.core;

import tgtools.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 15:05
 */
public class Constants {
    public final static Map<String, Object> QUEUE_ARGS = new HashMap<String, Object>() {{
        //队列过期时间
        //put("x-expires", 30000);
        //队列上消息过期时间，应小于队列过期时间
        put("x-message-ttl", 12000);
        //过期消息转向路由
        put("x-dead-letter-exchange", StringUtil.EMPTY_STRING);
        //过期消息转向路由相匹配routingkey(实际转向 QUEUE_TIMEOUT 的队列)
        put("x-dead-letter-routing-key", Constants.QUEUE_TIMEOUT);
    }};

    /**
     * 服务器 处理 过期消息
     */
    public static final String QUEUE_TIMEOUT="server.message.timeout";
    /**
     * 客户端 用户 消息 client.user.用户登录名
     */
    public static final String QUEUE_USER="client.user";

    /**
     * 客户端 系统 消息
     */
    public static final String EXCHANGE_CLIENT_SYSTEM="client.system";
    /**
     * 服务器 之间 消息
     */
    public static final String EXCHANGE_SERVER_SYSTEM="server.system";

}
