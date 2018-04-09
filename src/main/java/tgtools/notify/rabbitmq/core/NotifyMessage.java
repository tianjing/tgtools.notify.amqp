package tgtools.notify.rabbitmq.core;

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
    protected String mContent;
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

    public String getContent() {
        return mContent;
    }

    public void setContent(String pContent) {
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
}
