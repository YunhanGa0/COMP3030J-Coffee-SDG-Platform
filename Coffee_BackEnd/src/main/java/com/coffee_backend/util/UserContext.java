package com.coffee_backend.util;

import com.coffee_backend.dto.UserContextDTO;

public class UserContext {
    private static final ThreadLocal<UserContextDTO> tl = new ThreadLocal<>();

    public static void setUser(UserContextDTO userId){
        tl.set(userId);
    }

    public static UserContextDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
