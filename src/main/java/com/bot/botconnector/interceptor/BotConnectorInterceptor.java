package com.bot.botconnector.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class BotConnectorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Request Method: {}",request.getMethod());
        log.info("Request Content Type: {}",request.getContentType());
        log.info("Request Headers: {}",request.getHeaderNames());
        return true;
    }
}
