package io.spring.context;

import io.spring.core.user.User;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
public class UserContext {
    
    private static ThreadLocal<User> userContext = new ThreadLocal<>();

    public static User getUser(){
        return userContext.get();
    }

    public static void setUser(User user) {
        userContext.set(user);
    }

    public void remove(){
        userContext.remove();
    }
}
