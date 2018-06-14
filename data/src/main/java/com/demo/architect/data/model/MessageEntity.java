package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Yen on 3/26/2018.
 */

public class MessageEntity {
    @SerializedName("messageId")
    @Expose
    private String messageId;

    @SerializedName("messageName")
    @Expose
    private String messageName;

    @SerializedName("messageContent")
    @Expose
    private String messageContent;

    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessageId() {
        return messageId;
    }

    public String getMessageName() {
        return messageName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
