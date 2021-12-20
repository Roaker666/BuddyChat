package com.roaker.app.chat;
import com.roaker.UserView;
import com.roaker.app.entity.ChatUser;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Roaker
 * @version 1.0
 **/
@Component
public class ChatUserCache {

    /**
     * 在线用户websocket连接池
     */
    private static final Map<String, Session> ONLINE_USER_SESSIONS = new ConcurrentHashMap<>();
    private static final Map<String, ChatUser> ONLINE_USER = new ConcurrentHashMap<>();
    public static final Map<String, ChatUser> LOGIN_USER = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 5000)
    public void scheduled(){
        for (String name : LOGIN_USER.keySet()) {
            if (ONLINE_USER.get(name)==null){
                LOGIN_USER.remove(name);
            }
        }
    }

    /**
     * 新增一则连接
     * @param key 设置主键
     * @param session 设置session
     */
    public static void add(String key, ChatUser chatUser,Session session) {
        if (session != null){
            ONLINE_USER_SESSIONS.put(key, session);
            ONLINE_USER.put(key,chatUser);
        }
    }

    /**
     * 根据Key删除连接
     * @param key 主键
     */
    public static void remove(String key) {
            ONLINE_USER_SESSIONS.remove(key);
            ONLINE_USER.remove(key);
            LOGIN_USER.remove(key);
    }

    /**
     * 获取在线人数
     * @return 返回在线人数
     */
    public static int count(){
        return ONLINE_USER_SESSIONS.size();
    }

    /**
     * 获取在线session池
     * @return 获取session池
     */
    public static Map<String, Session> allOnlineUser() {
        return ONLINE_USER_SESSIONS;
    }

    public static Collection<ChatUser> getAllOnlineUser(){
        return ONLINE_USER.values();
    }

    public static Collection<ChatUser> getAllLogin(){
        return LOGIN_USER.values();
    }
}