package tgools.notify.rabbitmq.websocket;

import org.springframework.web.socket.WebSocketSession;
import tgools.notify.rabbitmq.websocket.listener.ClientFactoryListener;
import tgools.notify.rabbitmq.websocket.listener.event.AddClientEvent;
import tgools.notify.rabbitmq.websocket.listener.event.RemoveClientEvent;
import tgtools.web.develop.websocket.ClientFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 11:37
 */
public class WsClientFactory extends ClientFactory {
    protected ConsumerMap consumerMap =new ConsumerMap(this);
    protected ClientFactoryListener mClientFactoryListener;

    public void setClientFactoryListener(ClientFactoryListener pClientFactoryListener) {
        mClientFactoryListener = pClientFactoryListener;
    }

    /**
     * 添加一个客户端
     * @param pUserName
     * @param pClient
     */
    @Override
    public void addClient(String pUserName, WebSocketSession pClient) {
        if (!mClients.containsKey(pUserName)) {
            mClients.put(pUserName, pClient);
            consumerMap.createConsumer(pUserName);
            onAddClient(pUserName,pClient);
        }
    }

    /**
     * 根据value 删除客户端
     * @param pClient
     */
    @Override
    public  void removeClient(WebSocketSession pClient)
    {
        for(ConcurrentHashMap.Entry<String,WebSocketSession> item :mClients.entrySet())
        {
            if(item.getValue().equals(pClient))
            {
                mClients.remove(item.getKey());
                consumerMap.remove(item.getKey());
                onRemoveClient(item.getKey());
                return;
            }
        }
    }
    protected void onAddClient(String pUserName, WebSocketSession pClient)
    {
        if(null!=mClientFactoryListener)
        {
            mClientFactoryListener.addClient(this,new AddClientEvent(pUserName,pClient));
        }
    }
    protected void onRemoveClient(String pUserName)
    {
        if(null!=mClientFactoryListener)
        {
            mClientFactoryListener.removeClient(this,new RemoveClientEvent(pUserName));
        }
    }
}
