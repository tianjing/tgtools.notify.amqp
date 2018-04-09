package tgtools.notify.rabbitmq.websocket.listener.event;

import org.springframework.web.socket.WebSocketSession;
import tgtools.interfaces.Event;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:03
 */
public class AddClientEvent extends Event {
    public AddClientEvent(){}
    public AddClientEvent(String pLoginName,WebSocketSession pWebSocketSession)
    {
        mLoginName=pLoginName;
        mWebSocketSession=pWebSocketSession;
    }
    private String mLoginName;
    private WebSocketSession mWebSocketSession;

    public String getLoginName() {
        return mLoginName;
    }

    public void setLoginName(String pLoginName) {
        mLoginName = pLoginName;
    }

    public WebSocketSession getWebSocketSession() {
        return mWebSocketSession;
    }

    public void setWebSocketSession(WebSocketSession pWebSocketSession) {
        mWebSocketSession = pWebSocketSession;
    }
}
