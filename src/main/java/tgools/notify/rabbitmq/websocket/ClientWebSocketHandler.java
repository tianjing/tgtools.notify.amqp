package tgools.notify.rabbitmq.websocket;

import org.apache.shiro.mgt.SecurityManager;
import tgtools.exceptions.APPErrorException;
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
        super();
        mClientFactory =getClientFactory();
        mSecurityManager=getSecurityManager();
        mUserService=getUserService();
        initCommand();
    }
    public void sendMessage(String pLoginName,String pMessage) throws APPErrorException {
        mClientFactory.sendMessage(pLoginName,pMessage);
    }

    protected abstract ClientFactory getClientFactory();
    protected abstract UserService getUserService();
    protected abstract SecurityManager getSecurityManager();
    protected abstract void initCommand();
}
