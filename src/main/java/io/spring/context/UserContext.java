package io.spring.context;

import io.spring.core.user.User;

public class UserContext {
    
    private static ThreadLocal<User> userContext = new ThreadLocal<>();
    
    public static User getUser(){
        // TODO: 2018/8/30 没有User抛出异常
        return userContext.get();
    }
}
