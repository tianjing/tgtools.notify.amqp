package tgtools.notify.rabbitmq.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import tgtools.notify.rabbitmq.core.Constants;
import tgtools.notify.rabbitmq.service.RabbitMqService;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:53
 */

public abstract class RabbitConfig {
    @Autowired
    protected RabbitProperties rabbitProperties;

    @Bean("connectionFactory")
    public abstract ConnectionFactory getConnectionFactory();

    @Bean(name="rabbitAdmin")
    public RabbitAdmin getRabbitAdmin()
    {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(getConnectionFactory());
        rabbitAdmin.setAutoStartup(true);
        RabbitTemplate rabbitTemplate = rabbitAdmin.getRabbitTemplate();
        rabbitTemplate.setUseTemporaryReplyQueues(false);
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        rabbitTemplate.setMessagePropertiesConverter(new DefaultMessagePropertiesConverter());
        rabbitTemplate.setReceiveTimeout(60000);
        return rabbitAdmin;
    }

    @Bean(name="rabbitMqService")
    public RabbitMqService rabbitMqService()
    {
        RabbitMqService service=new RabbitMqService(getRabbitAdmin());
        service.setServerSystemExchange(new FanoutExchange(Constants.EXCHANGE_SERVER_SYSTEM,true,false));
        service.setClientSystemExchange(new FanoutExchange(Constants.EXCHANGE_CLIENT_SYSTEM,true,false));
        service.setTimeoutQueue(new Queue(Constants.QUEUE_TIMEOUT,true,false,false));
        service.initDeclare();
        return service;
    }
}
