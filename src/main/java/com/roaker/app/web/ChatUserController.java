package com.roaker.app.web;

import com.alibaba.fastjson.JSON;
import com.roaker.UserView;
import com.roaker.app.chat.ChatUserCache;
import com.roaker.app.dao.ChatUserRepository;
import com.roaker.app.entity.ChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Roaker
 * @version 1.0
 **/
@Controller
@RequestMapping("/user")
@Slf4j
public class ChatUserController {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @GetMapping("/greeting")
    @ResponseBody
    public String greeting() {
        log.info("测试服务正确性");
        return "你好，这里是Roaker聊天小程序，请问有什么可以帮助你。";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(String username, String password) {
        UserView userView = new UserView();
        List<ChatUser> chatUser = chatUserRepository.findAllByNameAndPassword(username, password);
        if (chatUser == null || chatUser.isEmpty()) {
            userView.setMsg("登录失败，请仔细检查用户名和密码");
        } else {
            userView.setMsg("登录成功");
            userView.setUsername(username);
            ChatUserCache.LOGIN_USER.put(username,chatUser.get(0));
        }
        return JSON.toJSONString(userView);
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(String username,
                           String password) {
        Integer count = chatUserRepository.countByName(username);
        if (count > 0) {
            return "注册失败,该用户" + username + "已存在";
        }

        ChatUser chatUser = ChatUser.builder()
                .name(username)
                .password(password)
                .build();
        try {
            chatUserRepository.save(chatUser);
        } catch (Exception e) {
            log.error("注册用户失败:", e);
        }
        return "注册成功";
    }

    @PostMapping("/getUserList")
    @ResponseBody
    public String getUserList() {
        return JSON.toJSONString(ChatUserCache.getAllOnlineUser());
    }
}
