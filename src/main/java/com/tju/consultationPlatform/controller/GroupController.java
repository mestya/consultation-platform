package com.tju.consultationPlatform.controller;

import com.tju.consultationPlatform.domain.Group;
import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.domain.User;
import com.tju.consultationPlatform.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class GroupController {

    @Resource
    GroupService groupService;

    @RequestMapping(value = "/createGroup", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult createGroup(String userId, String groupName, String groupIntro) {
        return groupService.buildGroup(userId, groupName, groupIntro);
    }

    @RequestMapping(value = "/joinGroup", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult joinGroup(String userId, String groupId) {
        return groupService.joinGroup(userId, groupId);

    }

    @RequestMapping(value = "/quitGroup", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult quitGroup(String userId, String groupId) {
        return groupService.quitGroup(userId, groupId);

    }

    @RequestMapping(value = "/searchGroupById", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult searchGroupById(String id) {
        return groupService.searchGroupById(id);
    }

    @RequestMapping(value = "/searchGroupByName", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult searchGroupByName(String name) {
        return groupService.searchGroupByName(name);
    }

    @RequestMapping(value = "/getAllGroupMembers", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllGroupMembers(String groupId) {
        return groupService.getGroupMembers(groupId);
    }

    @RequestMapping(value = "/getAllGroupsByUser", method = RequestMethod.GET)
    @ResponseBody
    public List<Group> getAllGroupByUser(String userId) {
        return groupService.getAllGroupsByUserId(userId);
    }


}
