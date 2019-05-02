package com.tju.consultationPlatform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebListener
public class RequestListener implements ServletRequestListener {
    private static final Logger logger = LoggerFactory.getLogger(RequestListener.class);
    public void requestInitialized(ServletRequestEvent sre) {
        //将所有request请求都携带上httpSession
        HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();
        logger.info("调用了监听器,获取到httpSessionID:" + session.getId());
    }

    public RequestListener() {
    }

    public void requestDestroyed(ServletRequestEvent arg0) {
    }
}