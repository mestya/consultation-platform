package com.tju.consultationPlatform.controller;

import com.alibaba.fastjson.JSONObject;
import com.tju.consultationPlatform.domain.Message;
import com.tju.consultationPlatform.websocket.WebSocketServer;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static java.util.logging.Logger.getLogger;


/**
 * webSocket访问控制器
 */
@Controller
@CrossOrigin
public class WebsocketController {
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    SimpMessageSendingOperations simpMessageSendingOperations;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebsocketController.class);
    @RequestMapping("/index")
    public String index(String mesaage) {
        return "/index";
    }

    /**
     * 发送消息页面
     *
     * @param mesaage
     * @return
     */
    @RequestMapping("/webSocket/senMessage")
    public ModelAndView senMessage(String mesaage) {
        ModelAndView mav = new ModelAndView("/sendMessage");
        return mav;
    }

    /**
     * 接收消息页面
     *
     * @param mesaage
     * @return
     */
    @RequestMapping("/webSocket/receiveMessage")
    public ModelAndView receiveMessage(String mesaage) {
        ModelAndView mav = new ModelAndView("/receiveMessage");

        return mav;
    }

    //广播推送消息
    @Scheduled(fixedRate = 10000)
    public void sendTopicMessage() {
        System.out.println("后台广播推送！");
        Message msg = new Message();
        msg.setType(0);
        msg.setData("广播信息");
        this.template.convertAndSend("/topic/getResponse",msg);
    }

    //一对一推送消息
    @Scheduled(fixedRate = 10000)
    public void sendQueueMessage(String userId) {
        System.out.println("后台一对一推送！");
        Message msg = new Message();
        msg.setType(0);
        msg.setData("广播信息");
        this.template.convertAndSendToUser(userId,"/queue/getResponse",msg);
    }
    /**
     * 接收客户端发来的消息
     */
    @MessageMapping("/message")
    public void handleSubscribe(String msg) {
        logger.info("客户端发来消息：" + msg);
        JSONObject json = JSONObject.parseObject(msg);
        String to = json.getString("to");
        if (!StringUtils.isEmpty(to)) {
            simpMessageSendingOperations.convertAndSendToUser(to, "/message", msg);
        }
    }

    /**
     * 客户端发送群聊消息
     */
    @MessageMapping("/chat-group/{roomId}")
    public void handleGroupMsg(@DestinationVariable String roomId, String msg) {
        logger.info("客户端发来消息：" + msg);
        simpMessageSendingOperations.convertAndSend("/topic/chat-group/" + roomId, msg);
    }

    /**群发
    @MessageMapping("/groupRequest")
    //SendTo 发送至 Broker 下的指定订阅路径
    @SendTo("/mass/getResponse")
    public ChatRoomResponse mass(ChatRoomRequest chatRoomRequest){
        //方法用于群发测试
        System.out.println("name = " + chatRoomRequest.getName());
        System.out.println("chatValue = " + chatRoomRequest.getChatValue());
        ChatRoomResponse response=new ChatRoomResponse();
        response.setName(chatRoomRequest.getName());
        response.setChatValue(chatRoomRequest.getChatValue());
        return response;
    }

    //单独聊天
    @MessageMapping("/aloneRequest")
    public ChatRoomResponse alone(ChatRoomRequest chatRoomRequest){
        //方法用于一对一测试
        System.out.println("userId = " + chatRoomRequest.getUserId());
        System.out.println("name = " + chatRoomRequest.getName());
        System.out.println("chatValue = " + chatRoomRequest.getChatValue());
        ChatRoomResponse response=new ChatRoomResponse();
        response.setName(chatRoomRequest.getName());
        response.setChatValue(chatRoomRequest.getChatValue());
        this.template.convertAndSendToUser(chatRoomRequest.getUserId()+"","/alone/getResponse",response);
        return response;
    }
*/
}
