package tgools.notify.rabbitmq.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import tgools.notify.rabbitmq.core.Constants;
import tgools.notify.rabbitmq.service.RabbitMqService;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:53
 */

public abstract class RabbitConfig {
    @Autowired
    private RabbitProperties rabbitProperties;

    @Bean("connectionFactory")
    public abstract ConnectionFactory getConnectionFactory();
    // {
//        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
//                new com.rabbitmq.client.ConnectionFactory();
//        rabbitConnectionFactory.setHost(rabbitProperties.getHost());
//        rabbitConnectionFactory.setPort(rabbitProperties.getPort());
//        rabbitConnectionFactory.setUsername(rabbitProperties.getUsername());
//        rabbitConnectionFactory.setPassword(rabbitProperties.getPassword());
//        rabbitConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
//
//        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
//        return connectionFactory;
//    }

    @Bean(name="rabbitAdmin")
    public RabbitAdmin getRabbitAdmin()
    {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(getConnectionFactory());
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean(name="serializerMessageConverter")
    public MessageConverter getMessageConverter(){
        return new SimpleMessageConverter();
    }

    @Bean(name="messagePropertiesConverter")
    public MessagePropertiesConverter getMessagePropertiesConverter()
    {
        return new DefaultMessagePropertiesConverter();
    }
    @Bean(name="rabbitTemplate")
    public RabbitTemplate getRabbitTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(getConnectionFactory());
        rabbitTemplate.setUseTemporaryReplyQueues(false);
        rabbitTemplate.setMessageConverter(getMessageConverter());
        rabbitTemplate.setMessagePropertiesConverter(getMessagePropertiesConverter());
        rabbitTemplate.setReceiveTimeout(60000);
        return rabbitTemplate;
    }


    @Bean(name="timeoutQueue")
    public Queue timeoutQueue(@Qualifier("rabbitAdmin")RabbitAdmin rabbitAdmin)
    {
        Queue sendQueue = new Queue(Constants.QUEUE_TIMEOUT,true,false,false);
        rabbitAdmin.declareQueue(sendQueue);
        return sendQueue;
    }

    @Bean(name="serverSystemExchange")
    public Exchange serverSystemExchange(@Qualifier("rabbitAdmin")RabbitAdmin rabbitAdmin)
    {
        FanoutExchange sendExchange = new FanoutExchange(Constants.EXCHANGE_SERVER_SYSTEM,true,false);
        rabbitAdmin.declareExchange(sendExchange);
        return sendExchange;
    }
    @Bean(name="clientSystemExchange")
    public Exchange clientSystemExchange(@Qualifier("rabbitAdmin")RabbitAdmin rabbitAdmin)
    {
        FanoutExchange sendExchange = new FanoutExchange(Constants.EXCHANGE_CLIENT_SYSTEM,true,false);
        rabbitAdmin.declareExchange(sendExchange);
        return sendExchange;
    }
    @Bean
    public RabbitMqService rabbitMqService()
    {
        return new RabbitMqService();
    }
}
