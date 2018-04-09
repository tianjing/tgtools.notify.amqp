package tgtools.notify.rabbitmq.core;

import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONArray;
import tgtools.json.JSONObject;

import java.util.Date;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 14:29
 */
public class NotifyMessage {

    protected String mSender;
    protected String mReceiver;
    protected Object mContent;
    protected Date mCTime;
    protected String mType;

    public String getSender() {
        return mSender;
    }

    public void setSender(String pSender) {
        mSender = pSender;
    }

    public String getReceiver() {
        return mReceiver;
    }

    public void setReceiver(String pReceiver) {
        mReceiver = pReceiver;
    }

    public Object getContent() {
        return mContent;
    }

    public void setContent(Object pContent) {
        mContent = pContent;
    }

    public Date getCTime() {
        return mCTime;
    }

    public void setCTime(Date pCTime) {
        mCTime = pCTime;
    }

    public String getType() {
        return mType;
    }

    public void setType(String pType) {
        mType = pType;
    }

    public JSONObject toJson() throws APPErrorException {
        JSONObject json =new JSONObject();
        json.put("sender",getSender());
        json.put("receiver",getReceiver());
        json.put("type",getType());

        if(null==getContent()||(getContent() instanceof JSONObject)||(getContent() instanceof JSONArray))
        {json.put("content",getContent());}
        else
        {
            json.put("content",tgtools.util.JsonParseHelper.parseToJsonObject(getContent()));
        }
        return json;
    }

    @Override
    public String toString() {
        try {
            return toJson().toString();
        } catch (APPErrorException e) {
            return e.getMessage();
        }
    }
}
