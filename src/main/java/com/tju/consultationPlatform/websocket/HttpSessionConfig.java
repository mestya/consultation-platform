package com.tju.consultationPlatform.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

/**
 * @DESC :  重写modifyHandshake，并将http会话存入websocket会话的用户属性内
 */
public class HttpSessionConfig extends Configurator {
    private static final Logger logger = LoggerFactory.getLogger(HttpSessionConfig.class);
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        // 获取当前Http连接的session
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        // 将httpSession信息注入websocketSession
        config.getUserProperties().put(HttpSession.class.getName(), httpSession);
        super.modifyHandshake(config, request, response);
        logger.info("调用modifyHandshake方法获取到Httpsession id:" + httpSession.getId());

    }
}
