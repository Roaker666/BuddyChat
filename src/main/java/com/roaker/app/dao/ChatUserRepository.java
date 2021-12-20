package com.roaker.app.dao;

import com.roaker.app.entity.ChatUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Roaker
 * @version 1.0
 **/
@Repository
public interface ChatUserRepository extends CrudRepository<ChatUser,Long> {

    /**
     * 通过用户名和密码查询用户
     * @param name 用户名
     * @param password 密码
     * @return 聊天用户
     */
    List<ChatUser> findAllByNameAndPassword(String name,String password);

    /**
     *  根据用户名查询
     * @param name 用户名
     * @return 多少
     */
    Integer countByName(String name);

    /**
     * 根据用户名查询
     * @param name 用户名
     * @return 用户
     */
    ChatUser queryByName(String name);
}
