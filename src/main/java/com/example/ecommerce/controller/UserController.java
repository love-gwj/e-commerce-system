package com.example.ecommerce.controller;

import com.example.ecommerce.annotation.Idempotent;
import com.example.ecommerce.annotation.RateLimiter;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.config.JwtUtils;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    @RateLimiter(time = 60, count = 5, msg = "注册过于频繁，请60秒后再试")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        
        User newUser = userService.register(user);
        String token = jwtUtils.generateToken(newUser.getId(), newUser.getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("user", newUser);
        map.put("token", token);
        return Result.success(map);
    }

    @PostMapping("/login")
    @RateLimiter(time = 60, count = 10, msg = "登录过于频繁，请60秒后再试")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
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
