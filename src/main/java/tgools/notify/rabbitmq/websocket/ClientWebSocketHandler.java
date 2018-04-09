package tgools.notify.rabbitmq.websocket;

import org.apache.shiro.mgt.SecurityManager;
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

    protected ClientFactory getClientFactory(){
        return new WsClientFactory();
    }
    protected abstract UserService getUserService();
    protected abstract SecurityManager getSecurityManager();
    protected abstract void initCommand();
}
