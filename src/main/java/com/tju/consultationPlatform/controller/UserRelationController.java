package com.tju.consultationPlatform.controller;


import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.service.UserRelationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class UserRelationController {

    @Resource
    UserRelationService userRelationService;

    @RequestMapping(value = "/applyFriend", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult applyFriend(String userId, String friendId, String content) {
        try {
            userRelationService.applyRelation(userId,friendId,2,content);
            return new JsonResult(1,"success",null);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }
    @RequestMapping(value = "/buildFriend", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult buildFriend(String userId, String friendId) {
        try {
            userRelationService.setRelation(userId, friendId,2,null);
            userRelationService.setRelation(friendId,userId,2,null);
            return new JsonResult(1,"success",null);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }

    @RequestMapping(value = "/buildFamily", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult buildFamily(String userA, String userB) {
        try {
            userRelationService.setRelation(userA, userB,1,null);
            userRelationService.setRelation(userB,userA,1,null);
            return new JsonResult(1,"success",null);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }

    }

    @RequestMapping(value = "/buildDoctor", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult buildDoctor(String doctorId, String userId) {
        try {
            userRelationService.setRelation(doctorId, userId,3,null);
            userRelationService.setRelation(userId,doctorId,3,null);
            return new JsonResult(1,"success",null);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }

    @RequestMapping(value = "/deleteRelation",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult deleteRelation(String userA, String userB, int relationType){
       try {
           userRelationService.cancelRelation(userA,userB,relationType);
           return new JsonResult(1,"success",null);
       }catch (Exception e){
           return new JsonResult(-1,"failure",e.getMessage());
       }
    }
    @RequestMapping(value = "/getAllFriends", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getAllFriends(String userId) {
        try {
            List<User> users= userRelationService.getAllRelations(userId,2);
            return new JsonResult(1,"success",users);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }

    @RequestMapping(value = "/getFamily", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getFamily(String userId) {
        try {
            List<User> users= userRelationService.getAllRelations(userId,1);
            return new JsonResult(1,"success",users);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }

    @RequestMapping(value = "/getDoctor", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult getDoctor(String userId) {
        try {
            List<User> users= userRelationService.getAllRelations(userId,3);
            return new JsonResult(1,"success",users);
        }catch (Exception e){
            return new JsonResult(-1,"failure",e.getMessage());
        }
    }




}
