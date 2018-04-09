package tgtools.notify.rabbitmq.websocket;

import tgtools.notify.rabbitmq.core.NotifyMessage;
import tgtools.exceptions.APPErrorException;
import tgtools.web.develop.command.CommandFactory;
import tgtools.web.develop.service.UserService;
import tgtools.web.develop.websocket.AbstractSingleWebSocketHandler;
import tgtools.web.develop.websocket.ClientFactory;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 17:05
 */

public abstract class AbstractClientWebSocketHandler<T extends UserService> extends AbstractSingleWebSocketHandler<T> {

    public AbstractClientWebSocketHandler()
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
    protected abstract ClientFactory getClientFactory();

    protected abstract void initCommand();
}
