package com.sum.utils;

import com.sum.domain.entity.User;

public class ContextUtil {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User users) {
        currentUser.set(users);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

}
