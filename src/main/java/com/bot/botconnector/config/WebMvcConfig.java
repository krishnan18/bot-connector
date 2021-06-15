package com.bot.botconnector.config;

import com.bot.botconnector.interceptor.BotConnectorInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final BotConnectorInterceptor botConnectorInterceptor;

    public WebMvcConfig(BotConnectorInterceptor botConnectorInterceptor) {
        this.botConnectorInterceptor = botConnectorInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(botConnectorInterceptor);
    }
}
