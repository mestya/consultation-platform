package com.tju.consultationPlatform.service;

import com.alibaba.fastjson.JSON;
import com.tju.consultationPlatform.domain.Message;
import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.domain.UserRelation;

import com.tju.consultationPlatform.repository.UserRelationRepository;
import com.tju.consultationPlatform.repository.UserRepository;
import com.tju.consultationPlatform.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tju.consultationPlatform.websocket.WebSocketServer.onLineList;

@Service
public class UserRelationService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserRelationRepository relationRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private MessageService messageService;

    /**public void setFriend(String userA, String userB) throws Exception{

        if (isExist(userA, userB, "friend") || isExist(userB, userA, "friend")) {
            throw new Exception("已经是好友关系");
        }
        if (!this.setRelation(userA, userB, "friend",null)) {
            throw new Exception("加好友失败");
        }
    }

    public JsonResult setFamily(String userA, String userB, String AtoBType, String BtoAType) {

        //判断是否已经是亲属关系
        if (isExist(userA, userB, AtoBType)) {
            return new JsonResult(-1, "已经为亲属关系", null);
        }
        try {
            this.setRelation(userA, userB, AtoBType);
            this.setRelation(userB, userA, BtoAType);
            return new JsonResult(1, "建立亲属关系成功", userB);
        } catch (Exception e) {
            System.out.println("建立失败");
            return new JsonResult(-1, "建立亲属关系失败", null);
        }

    }

    /**
     * userA为医生ID，userB为患者

    public JsonResult setDoctor(String userA, String userB) {
        //判断是否已经是亲属关系
        if (isExist(userA, userB, "docter")) {
            return new JsonResult(-1, "已经建立医患关系", null);
        }
        try {
            this.setRelation(userA, userB, "docter");
            return new JsonResult(1, "建立医患关系成功", userA);
        } catch (Exception e) {
            System.out.println("建立失败");
            return new JsonResult(-1, "建立医患关系失败", null);
        }

    }

    public JsonResult cancelFriend(String userA, String userB) {
        if (isExist(userA, userB, "friend")) {
            this.cancelRelation(userA, userB, "friend");
            return new JsonResult(1, "删除成功", null);
        }
        if (isExist(userB, userA, "friend")) {
            this.cancelRelation(userB, userA, "friend");
            return new JsonResult(1, "删除成功", null);
        }

        return new JsonResult(-1, "删除失败，对方不是您的好友", null);
    }


    public JsonResult cancelFamily(String userA, String userB, String AtoBType, String BtoAType) {
        if (isExist(userA, userB, AtoBType)) {
            this.cancelRelation(userA, userB, BtoAType);
            return new JsonResult(1, "删除成功", null);
        }
        if (isExist(userB, userA, BtoAType)) {
            this.cancelRelation(userB, userA, BtoAType);
            return new JsonResult(1, "删除成功", null);
        }
        return new JsonResult(-1, "删除失败", null);
    }

    public JsonResult cancelDoctor(String userA, String userB, String AtoBType, String BtoAType) {
        if (isExist(userA, userB, AtoBType)) {
            this.cancelRelation(userA, userB, BtoAType);
            return new JsonResult(1, "删除成功", null);
        }

        return new JsonResult(-1, "删除失败", null);
    }
*/
    public List<User> getAllRelations(String userId,int relationType)throws Exception {

        List<User> users = new ArrayList<>();
        //有朋友关系记录的集合
        List<UserRelation> list = relationRepository.findRelationsByOwnerId(userId, relationType);
        if(list==null||list.size()==0){
            throw new Exception("RelationType:"+relationType+"NOT FOUND");
        }else {
            for (UserRelation pair : list) {
                users.add(userRepository.findByUserId(pair.getUserB()));
            }
            return users;
        }
    }


    //判断某关系是否存在
    public boolean isExist(String userA, String userB, int relationType) {
        if (relationRepository.findOne(userA, userB, relationType) != null) {
            return true;
        } else {
            return false;
        }
    }

    public void applyRelation(String userA,String userB,int relationType,String content)throws Exception{
        if(userA.equals(userB)){
            throw new Exception("无法和自己建立关系");
        }
        if (isExist(userA, userB, relationType)) {
            throw new Exception("已经是"+relationType+"关系");
        }
        Message msg = new Message();
        msg.setType(3);
        msg.setFrom(userA);
        msg.setTo(userB);
        msg.setData(content);
        msg.setIsTransport(0);
        msg.setCreateDate(new Date());
        WebSocketServer toWebsocket = onLineList.get(userB);
        if (toWebsocket != null && !toWebsocket.equals("")) {
            toWebsocket.sendMessage(JSON.toJSONString(msg));
            msg.setIsTransport(1);
            messageService.addMessage(msg);
            logger.info("信息接收用户:"+userB+" 在线，转发信息");
        } else {
            msg.setIsTransport(0);
            messageService.addMessage(msg);
            logger.info("信息接收用户:"+userB+" 不在线，信息保存至数据库");
        }

    }

    //建立关系通用函数
    public boolean setRelation(String userA, String userB, int relationType,String name ) throws Exception{
        UserRelation relation = new UserRelation();
        relation.setRelationType(relationType);
        relation.setUserA(userA);
        relation.setUserB(userB);
        relation.setMemoName(name);
        if (isExist(userA, userB, relationType)) {
            throw new Exception("已经是"+relationType+"关系");
        }

        if (relationRepository.save(relation) != null) {
            return true;
        } else {
            throw new Exception("建立"+relationType+"关系失败");
        }


    }

    //取消关系
    public int cancelRelation(String userA, String userB, int relationType)throws Exception {
        if (isExist(userA, userB, relationType)) {
            return relationRepository.deleteUserRelation(userA, userB, relationType);
        }else {
            throw new Exception("relation is not exist");
        }
    }

}
