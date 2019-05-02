package com.tju.consultationPlatform.controller;

import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.domain.Message;
import com.tju.consultationPlatform.service.MessageService;
import com.tju.consultationPlatform.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.tju.consultationPlatform.websocket.WebSocketServer.onLineList;

@Controller
@CrossOrigin
public class PushController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MessageService messageService;




    //个人推送文章接口
    @RequestMapping(value = "/pushMsg",method = RequestMethod.POST)
    public String pushMsg(String from,String to,String message) {
        WebSocketServer toWebsocket = onLineList.get(to);
        try {
            if (toWebsocket != null && !toWebsocket.equals("")) {
                toWebsocket.sendMessage(message);
                logger.info("信息接收用户:"+to+"收到文章推送");

            } else {

                Message msg = new Message();
                msg.setFrom(from);
                msg.setTo(to);
                msg.setData(message);
                msg.setType(0);
                msg.setIsTransport(0);
                messageService.addMessage(msg);
                logger.info("信息接收用户:"+to+" 不在线，信息保存至数据库");
            }
            return "success";
        }catch (Exception e){
            logger.info("消息推送失败");
            return "failure";
        }

    }


    /**
     * 服务器端推送消息
     */
    @RequestMapping("/serverSendMessage")
    @ResponseBody
    public JsonResult sendMessage(String message) {
        JsonResult result = new JsonResult();
        WebSocketServer webSocketServer=new WebSocketServer();
        try {
            result.setStatus(1);
            result.setMessage("发送消息成功");
            webSocketServer.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(-1);
            result.setMessage("发送消息失败");
            result.setContent(null);
        }
        return result;
    }

    //系统推送文章
  /**  @RequestMapping("/pollArticle")
    @ResponseBody
    public JsonResult pollArticle(){

    }*/
}
