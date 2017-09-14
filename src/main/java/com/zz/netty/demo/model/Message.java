package com.zz.netty.demo.model;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private String id;
    private String content;
    private String response;

    public  Message(){};
    public Message(String content) {
        this.content = content;
    }

    public String getId() {
        if (null == id){
            id = UUID.randomUUID().toString().replace("-","");
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
