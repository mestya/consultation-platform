package com.tju.consultationPlatform.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 注册webSocket端点
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 添加一个/WebSocketServer端点，客户端就可以通过这个端点来进行连接；withSockJS作用是添加SockJS支持
        registry.addEndpoint("/WebSocketServer").setAllowedOrigins("*").withSockJS();
    }

}