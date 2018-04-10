package tgtools.notify.rabbitmq.websocket;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import tgtools.exceptions.APPErrorException;
import tgtools.notify.rabbitmq.core.NotifyMessage;
import tgtools.web.develop.websocket.AbstractSingleWebSocketHandler;
import tgtools.web.develop.websocket.listener.ClientFactoryListener;
import tgtools.web.develop.websocket.listener.event.AddClientEvent;
import tgtools.web.develop.websocket.listener.event.RemoveClientEvent;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 17:05
 */

public abstract class AbstractClientWebSocketHandler extends AbstractSingleWebSocketHandler {


    protected ConsumerMap mConsumerMap =new ConsumerMap(this);
    public abstract RabbitAdmin getRabbitAdmin();


    public AbstractClientWebSocketHandler()
    {
        super();
        mClientFactory.setClientFactoryListener(new ClientMessageListener());
    }
    public void sendMessage(String pLoginName, String pMessage) throws APPErrorException {
        mClientFactory.sendMessage(pLoginName, pMessage);
    }

    public void sendNotifyMessage(String pLoginName, NotifyMessage pMessage) throws APPErrorException {
        sendMessage(pLoginName, pMessage.toString());
    }

    public class ClientMessageListener implements ClientFactoryListener{

        @Override
        public void addClient(Object pSender, AddClientEvent pEvnet) {
            AbstractClientWebSocketHandler.this.mConsumerMap.createConsumer(pEvnet.getLoginName());
        }

        @Override
        public void removeClient(Object pSender, RemoveClientEvent pEvnet) {
            AbstractClientWebSocketHandler.this.mConsumerMap.removeConsumer(pEvnet.getLoginName());
        }
    }
}
