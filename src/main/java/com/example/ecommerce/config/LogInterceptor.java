package com.example.ecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String remoteAddr = getIpAddress(request);

        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        log.info("📌 请求开始 | IP: {} | Method: {} | URI: {} | Params: {}",
                remoteAddr, method, requestURI, queryString);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        Long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int status = response.getStatus();

        if (ex != null) {
            log.error("📕 请求异常 | Method: {} | URI: {} | Duration: {}ms | Status: {} | Error: {}",
                    method, requestURI, duration, status, ex.getMessage());
        } else {
            log.info("📗 请求完成 | Method: {} | URI: {} | Duration: {}ms | Status: {}",
                    method, requestURI, duration, status);
        }
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
