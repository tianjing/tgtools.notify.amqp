package tgtools.notify.rabbitmq.websocket;

import tgtools.notify.rabbitmq.core.NotifyMessage;
import tgtools.exceptions.APPErrorException;
import tgtools.web.develop.command.CommandFactory;
import tgtools.web.develop.websocket.AbstractSingleWebSocketHandler;
import tgtools.web.develop.websocket.ClientFactory;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 17:05
 */

public abstract class ClientWebSocketHandler extends AbstractSingleWebSocketHandler {

    public ClientWebSocketHandler()
    {
        mClientFactory =getClientFactory();
        mWebsocketCommand = new CommandFactory(getCommandType());
        initCommand();
    }
    public void sendMessage(String pLoginName,String pMessage) throws APPErrorException {
        mClientFactory.sendMessage(pLoginName,pMessage);
    }
    public void sendNotifyMessage(String pLoginName,NotifyMessage pMessage) throws APPErrorException {
        ((WsClientFactory)mClientFactory).sendNotifyMessage(pLoginName,pMessage);
    }
    protected ClientFactory getClientFactory(){
        return new WsClientFactory();
    }
    protected abstract void initCommand();
}