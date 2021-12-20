package com.roaker.app.chat;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
/**
 * @author Roaker
 * @version 1.0
 **/
@Slf4j
public class SocketHandler {


    /**
     * 根据key和用户名生成一个key值，简单实现下
     *
     * @param name 发送人
     * @return 返回值
     */
    public static String createKey(String name) {
        return name;
    }

    /**
     * 给指定用户发送信息
     *
     * @param session 指定用户的会话
     * @param msg     发送的消息
     */
    public static void sendSpecialMessage(Session session, String msg) {
        if (session == null) {
            return;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            basic.sendText(msg);
        } catch (IOException e) {
            log.error("消息发送异常，异常情况: {}", e.getMessage());
        }
    }

    /**
     * 给所有的在线用户发送消息
     *
     * @param message  发送的消息
     * @param username 发送人
     */
    public static void sendMessageToAll(String message, String username) {
        log.info("广播：群发消息");
        // 遍历map，只输出给其他客户端，不给自己重复输出
        ChatUserCache.allOnlineUser().forEach((key, session) -> {
            // 给除了自己以外的所有用户群发消息
            if (username!=null && !username.equals(key)) {
                sendSpecialMessage(session, message);
            }
        });
    }
}