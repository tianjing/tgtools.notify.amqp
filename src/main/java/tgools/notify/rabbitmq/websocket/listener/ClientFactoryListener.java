package tgools.notify.rabbitmq.websocket.listener;

import tgools.notify.rabbitmq.websocket.listener.event.AddClientEvent;
import tgools.notify.rabbitmq.websocket.listener.event.RemoveClientEvent;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public interface ClientFactoryListener {


    void addClient(Object pSender,AddClientEvent pEvnet);

    void removeClient(Object pSender,RemoveClientEvent pEvnet);

}
