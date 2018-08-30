package io.spring.context;

import io.spring.core.user.User;

public class UserContext {
    
    private static ThreadLocal<User> userContext = new ThreadLocal<>();

    public static User getUser(){
        return userContext.get();
    }

    public static void setUser(User user) {
        userContext.set(user);
    }
}
