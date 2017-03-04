package com.liuyk.draggridview.model;

import java.io.Serializable;

public class Channel implements Serializable {

    private static final long serialVersionUID = -7415501530039818851L;
    private String channelName;
    private String channelUrl;

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelUrl='" + channelUrl + '\'' +
                '}';
    }
}
