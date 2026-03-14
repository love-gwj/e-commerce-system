package com.example.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ecommerce.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    User register(User user);
    User getUserInfo(Long userId);
    void updateUserInfo(User user);
}
