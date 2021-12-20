package com.roaker.app;

import com.roaker.app.chat.ChatUserCache;
import com.roaker.app.entity.ChatUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.IOException;

/**
 * @author Roaker
 * @version 1.0
 **/
@Controller
@SpringBootApplication
@EnableWebSocket
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class);
    }

    @GetMapping
    public String index(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/chat")
    public ModelAndView chat(ModelAndView modelAndView, @RequestParam String username, HttpServletResponse response) throws IOException {
        ChatUser chatUser =
                ChatUserCache.LOGIN_USER.get(username);
        if (chatUser==null){
            response.sendRedirect("/");
            modelAndView.addObject("msg","用户未登录,请先登录");
            return modelAndView;
        }
        modelAndView.addObject("username",username);
        return modelAndView;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
