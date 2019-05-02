package com.tju.consultationPlatform.service;

import com.tju.consultationPlatform.domain.Group;
import com.tju.consultationPlatform.domain.JsonResult;
import com.tju.consultationPlatform.domain.User;

import com.tju.consultationPlatform.repository.GroupRepository;
import com.tju.consultationPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    public Group getGroup(String id) {
        return groupRepository.findByGroupId(id);
    }

    public JsonResult buildGroup(String userId, String groupName, String groupIntro) {
        List<User> userList = new ArrayList<User>();
        User user = userRepository.findByUserId(userId);
        userList.add(user);

        Group newGroup = new Group(groupName, userId, groupIntro, 1);
        newGroup.setUserList(userList);
        newGroup = groupRepository.save(newGroup); //新建群

        if (newGroup != null) {
            //建立群聊和成员关系
            user.getGroupList().add(newGroup);
            userRepository.save(user);
            return new JsonResult(1, "群聊建立成功", newGroup);
        } else {
            return new JsonResult(-1, "群聊建立失败", null);
        }

    }

    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(String id) {
        groupRepository.deleteById(id);
    }

    public int updateGroup(Group group) {
        return groupRepository.updateGroup(group.getGroupId(), group.getGroupName(), group.getGroupCreaterId(),
                group.getGroupIntroduction(), group.getGroupUserCount());
    }

    //获取一个群的所有成员id
    // public List<Integer> getGroupMember(int groupId){ return groupMemRepository.getAllmembers(groupId); }
    public List<User> getGroupMembers(String groupId) {
        return groupRepository.findByGroupId(groupId).getUserList();
    }

    //public List<Integer> getAllGroupsByUserIds(int userId){ return groupMemRepository.getAllGroupsByUser(userId); }
    public List<Group> getAllGroupsByUserId(String userId) {
        return userRepository.findByUserId(userId).getGroupList();
    }

    public JsonResult joinGroup(String userId, String groupId) {
        Group group = groupRepository.findByGroupId(groupId);
        if (group != null) {
            User user = userRepository.findByUserId(userId);
            List<User> users = group.getUserList();
            if (users.contains(user)) {
                return new JsonResult(-1, "成员已在当前群聊", group);
            } else {
                group.setGroupUserCount(group.getGroupUserCount() + 1);
                users.add(user);
                group.setUserList(users);
                groupRepository.save(group);

                user.getGroupList().add(group);
                userRepository.save(user);

                return new JsonResult(1, "加入群聊成功", group);
            }

        }

        return new JsonResult(-1, "未找到该群聊", null);
    }


    public JsonResult quitGroup(String userId, String groupId) {
        User user = userRepository.findByUserId(userId);
        Group group = groupRepository.findByGroupId(groupId);
        List<Group> groupList = user.getGroupList();
        List<User> userList = group.getUserList();
        if (userList.contains(user) && groupList.contains(group)) {

            userList.remove(user);
            groupList.remove(group);

            group.setUserList(userList);
            group.setGroupUserCount(group.getGroupUserCount() - 1);
            user.setGroupList(groupList);

            userRepository.save(user);
            groupRepository.save(group);

            if (userId == group.getGroupCreaterId()) {
                //如果群主退出群聊
            }
            if (group.getGroupUserCount() == 0) {//如果群聊人员为1，则解散群聊
                groupRepository.deleteById(groupId);
                return new JsonResult(1, "群聊解散", null);
            }

            return new JsonResult(1, "退出成功", null);

        } else {
            return new JsonResult(0, "失败", null);
        }
    }


    public JsonResult searchGroupById(String id) {
        Group group = groupRepository.findByGroupId(id);
        System.out.println(group);
        if (group != null) {
            return new JsonResult(1, "找到群聊", group);
        } else {
            return new JsonResult(-1, "未找到群聊", null);
        }
    }

    public JsonResult searchGroupByName(String name) {
        List<Group> groupList = groupRepository.findByGroupNameContaining(name);
        if (groupList.size() != 0) {
            return new JsonResult(groupList.size(), "找到", groupList);
        } else {
            return new JsonResult(-1, "未找到", null);
        }

    }
}
