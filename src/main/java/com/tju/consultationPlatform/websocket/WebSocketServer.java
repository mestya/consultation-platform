package com.tju.consultationPlatform.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tju.consultationPlatform.domain.Message;
import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.service.GroupService;
import com.tju.consultationPlatform.service.MessageService;
import com.tju.consultationPlatform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sty
 * @create 2019-4-10 16:34
 */
@Component
@ServerEndpoint(value = "/WebSocketServer", configurator = HttpSessionConfig.class)
public class WebSocketServer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static int onlineCount = 0;//在线人数
    public static Map<String, WebSocketServer> onLineList = new ConcurrentHashMap<String, WebSocketServer>();// 连接集合 userId => server 键值对 线程安全
    private Session session;//与某个客户端的连接会话，需要通过它来给客户端发送数据
    private HttpSession httpSession;  // 当前会话的httpsession
    private String userId = null;


    private static MessageService messageService;
    @Autowired
    public void setChatService(MessageService messageService) {
        this.messageService = messageService;
    }

    private static UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private static GroupService groupService;
    @Autowired
    public void setGroupService(GroupService groupService){this.groupService=groupService;}


    /**
     * 建立连接
     * @param session 在连接建立的时候，
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

        this.session = session;//websocket会话,获取session对象(这个就是java web登入后的保存的session对象，此处为用户信息，包含了userId)
        this.httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());//通过websocket会话获取用户属性并取出http会话
        User user = (User) httpSession.getAttribute("currentUser");
        if (user != null) {
            userId = user.getUserId();
            //将当前用户id和WebSocket对象加入在线清单
            onLineList.put(userId, this);
            addOnlineCount();
            logger.info("[{} : {}] has be connected...", userId, httpSession.getId());
            logger.info("当前在线人数:" + getOnlineCount());

            //检查自己是否有未接收的消息
            List<Message> messageList = messageService.getMessageUnReceive(userId);
            if (messageList != null && messageList.size() !=0 ) {
                logger.info(userId + "有未接收信息");
                for (int i = 0; i < messageList.size(); i++) {
                    Message message = messageList.get(i);
                    sendMessage(JSON.toJSONString(message));
                    message.setIsTransport(1);
                    messageService.updateMessage(message);
                }
            }
        }else {
            logger.info("user is not found");
        }
    }


    @OnError
    public void onError(Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        logger.info("断开连接" + this);

        //将用户在线状态置0
        if(userId!=null){
            User user = userService.getUserById(userId);
            user.setUserIsOnline(0);
            userService.updateUser(user);
            //从在线清单中移除当前用户
            onLineList.remove(userId);
            subOnlineCount();
            logger.info("[{} : {}]", userId, httpSession.getId());
            logger.info("当前在线人数" + onlineCount);
        }else {
            logger.info("未获取到要断开连接的用户");
        }
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            logger.info("来自客户端: "+userId+" 的消息: " + message);
            JSONObject jsonObject = JSON.parseObject(message);
            String textMessage = jsonObject.getString("data");
            String toId = jsonObject.getString("to");
            String heartBeat = jsonObject.getString("heart-beat");
            int type = jsonObject.getInteger("type");
            Message msg = new Message();
            msg.setFrom(userId);
            msg.setTo(toId);
            msg.setType(type);
            msg.setCreateDate(new Date());
            msg.setData(textMessage);
            if(heartBeat!=null){
                this.sendMessage("connected");
            }
            //单聊
            if (type == 1) {
                logger.info("请求单聊");
                //先判断该用户是否在线,在线则发送并且设置发送状态为1，不在线则直接设置发送状态为0，并且添加到数据库
                WebSocketServer toWebsocket = onLineList.get(toId);
                if (toWebsocket != null && !toWebsocket.equals("")) {
                    sendMessageTo(toId, msg);
                    msg.setIsTransport(1);
                    messageService.addMessage(msg);
                    logger.info("信息接收用户:"+toId+" 在线，转发信息");
                    logger.info("返回信息：" + msg);
                } else {
                    msg.setIsTransport(0);
                    messageService.addMessage(msg);
                    logger.info("信息接收用户:"+toId+" 不在线，信息保存至数据库");
                }
            }
            //群消息
            if (type == 2) {
                logger.info("请求群聊");
                List<User> users = groupService.getGroup(toId).getUserList();
                for(int i = 0;i < users.size();i++){
                    String userId = users.get(i).getUserId();
                    msg.setTo(userId);//分别发送给每个指定用户
                    msg.setGroup(toId);
                    WebSocketServer toWebSocketServer = onLineList.get(userId);
                   //先判断该用户是否在线,在线则发送并且设置发送状态为1，不在线则直接设置发送状态为0，并且添加到数据库
                    if(toWebSocketServer !=null && !toWebSocketServer.equals("")){
                        sendMessageTo(toId,msg);
                        msg.setIsTransport(1);
                        messageService.addMessage(msg);
                    }
                    else{
                        msg.setIsTransport(0);
                        messageService.addMessage(msg);
                    }
                }
            }

        } catch (Exception e) {
            logger.info("发生了错误了");
            e.printStackTrace();
        }
    }


    public void sendMessageTo(String toUser, Message msg) {
        WebSocketServer toWebsocket = onLineList.get(toUser);
        if (toWebsocket != null && !toWebsocket.equals("")) {
            //sendMessageTo(toWebsocket, JSON.toJSONString(msg));
            logger.info("信息接收用户:"+toUser+" 在线，转发信息");
            msg.setIsTransport(1);
            messageService.addMessage(msg);
            try {
                toWebsocket.sendMessage(JSON.toJSONString(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.info("信息接收用户:"+toUser+" 不在线，信息保存至数据库");
            msg.setIsTransport(0);
            messageService.addMessage(msg);
        }

    }

    public void sendMessage(String message) {
        try {
            this.session.getAsyncRemote().sendText(message);
            logger.info("推送消息成功，消息为：" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**public void sendPicture(WebSocketServer webSocketServer,String fileName){
        FileInputStream input;
        try {
            File file=new File("D:\\images\\"+fileName);
            input = new FileInputStream(file);
            byte bytes[] = new byte[(int) file.length()];
            input.read(bytes);
            BinaryMessage byteMessage=new BinaryMessage(bytes);
            session.sendMessage(byteMessage);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public void sendMessageAll(String message, String FromUserName) throws IOException {
        for (WebSocketServer item : onLineList.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    // 获取连接数
    private static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount;
    }

    // 增加连接数
    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    // 减少连接数
    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}