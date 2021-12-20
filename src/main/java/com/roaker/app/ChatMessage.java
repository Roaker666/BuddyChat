package com.roaker.app;

import lombok.Data;

/**
 * @author Roaker
 * @version 1.0
 **/
@Data
public class ChatMessage {
    private String sendUser;
    private String msg;
    private String msgType;
    private String acceptUser;
    private String sendType;
}
