package com.roaker.app.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roaker.app.ChatMessage;
import com.roaker.app.dao.ChatUserRepository;
import com.roaker.app.entity.ChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

import static com.roaker.app.chat.ChatUserCache.*;
import static com.roaker.app.chat.SocketHandler.createKey;

/**
 * @author Roaker
 * @version 1.0
 **/
@Component
@ServerEndpoint("/net/websocket/{name}")
@Slf4j
public class SocketEndPoint {
    private static ChatUserRepository chatUserRepository;

    @Autowired
    public void setUserService(ChatUserRepository chatUserRepository) {
        SocketEndPoint.chatUserRepository = chatUserRepository;
    }

    /**
     * 用户连接方法
     *
     * @param name    连接用户
     * @param session session
     */
    @OnOpen
    public void onOpen(@PathParam("name") String name, Session session) {
        ChatUser chatUser = chatUserRepository.queryByName(name);
        log.info("有新的连接：{}", session);
        add(createKey(name),chatUser, session);
        SocketHandler.sendMessageToAll("<div style='width: 100%; float: left;'>用户【" + chatUser.getName()+ "】已上线</div>", name);
        log.info("在线人数：{}", count());

        allOnlineUser().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : allOnlineUser().entrySet()) {
            log.info("{}", item.getKey());
        }
    }

    /**
     * 发送消息方法（私聊和群聊）
     *
     * @param message 消息对象
     * @throws IOException 异常
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        // 获取到前端返回的消息，消息是JSON字符串，将消息转换为ChatMessage实体类对象
        ObjectMapper objectMapper = new ObjectMapper();
        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);

        // 群发
        if (chatMessage.getSendType().equalsIgnoreCase("1")) {
            // 群发给除了自己以外的用户
            SocketHandler.sendMessageToAll(
                    "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" + chatMessage.getSendUser() + "群发消息</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + chatMessage.getMsg() + "</div>",
                    chatMessage.getSendUser());
        }
        // 私聊
        else {
            Session userSession;
            /*
             * 1. 遍历WebSocket连接用户池
             * 2. 判断如果是接收方，则调用私聊方法，并退出循环
             */
            for (Map.Entry<String, Session> item : allOnlineUser().entrySet()) {
                if (item.getKey().equals(chatMessage.getAcceptUser())) {
                    userSession = item.getValue();
                    // 调用单一消息发送方法，给对应用户发送消息
                    SocketHandler.sendSpecialMessage(userSession,
                            "<div style='width: 100%; float: left;'>&nbsp;&nbsp;" +chatMessage.getSendUser() + "</div><div style='width: 100%; font-size: 18px; font-weight: bolder; float: right;'>" + chatMessage.getMsg() + "</div>");
                    // 只给某一个发送之后，就不需要再循环发送了
                    break;
                }
            }
        }
        log.info("有新消息： {}", message);
    }

    /**
     * 连接关闭方法
     *
     * @param name    关闭用户
     * @param session session
     */
    @OnClose
    public void onClose(@PathParam("name") String name, Session session) {
        log.info("连接关闭： {}", session);
        remove(createKey(name));
        log.info("在线人数：{}", count());
        allOnlineUser().keySet().forEach(item -> log.info("在线用户：" + item));
        for (Map.Entry<String, Session> item : allOnlineUser().entrySet()) {
            log.info("12: {}", item.getKey());
        }
        Date date = new Date();
        DateFormat df = DateFormat.getDateTimeInstance();
        SocketHandler.sendMessageToAll("<div style='width: 100%; float: left;'>[" + df.format(date) + "] " + name + "已离开聊天室</div>", name);
    }

    /**
     * 聊天室异常方法
     *
     * @param session   session
     * @param throwable 异常类
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (Exception e) {
            log.error("退出发生异常", e);
        }
        log.error("连接出现异常", throwable);
    }
}