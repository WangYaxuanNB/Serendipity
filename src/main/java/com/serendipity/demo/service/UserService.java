package com.serendipity.demo.service;

import com.serendipity.demo.entity.User;

public interface UserService {
    void register(User user);
    User login(String username, String password);
    User loginByPhone(String phoneNumber, String password);
}
