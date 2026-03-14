package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.config.JwtUtils;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@RequestBody User user) {
        User newUser = userService.register(user);
        String token = jwtUtils.generateToken(newUser.getId(), newUser.getUsername());
        
        Map<String, Object> map = new HashMap<>();
        map.put("user", newUser);
        map.put("token", token);
        return Result.success(map);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        
        User user = userService.login(username, password);
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", token);
        return Result.success(map);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result<?> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        userService.updateUserInfo(user);
        return Result.success();
    }
}
