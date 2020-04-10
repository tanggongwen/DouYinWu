package com.example.qd.douyinwu.been;

import java.io.Serializable;

public class LivePushBean implements Serializable {

    /**
     * push_url : rtmp://84209.livepush.myqcloud.com/live/10111?txSecret=b6db628c48dd339dff6dbe3e26fed157&txTime=5E79A72B
     * id : 102
     * room : 10111
     */

    private String push_url;
    private String id;
    private String room;

    public String getPush_url() {
        return push_url;
    }

    public void setPush_url(String push_url) {
        this.push_url = push_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
