package com.product.images.common;

/**
 *
 * @author sohel rana
 */
public class BaseResponse {

    protected String message;
    protected int messageType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

}
