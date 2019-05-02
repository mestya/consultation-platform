package com.tju.consultationPlatform.domain;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)//获取时间的注解
@Table(name = "groups")
public class Group {

    private String groupId;
    private String groupName;
    private String groupCreaterId;
    private Timestamp groupCreateTime;
    private String groupIntroduction;
    private int groupUserCount;
    private List<User> userList;

    public Group() {
    };

    public Group(String groupName, String groupCreaterId, String groupIntroduction, int groupUserCount) {
        this.groupName = groupName;
        this.groupCreaterId = groupCreaterId;
        this.groupIntroduction = groupIntroduction;
        this.groupUserCount = groupUserCount;
    }

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "groupId")
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(name = "groupName")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "groupCreaterId")
    public String getGroupCreaterId() {
        return groupCreaterId;
    }

    public void setGroupCreaterId(String     groupCreaterId) {
        this.groupCreaterId = groupCreaterId;
    }

    @CreatedDate
    @Column(name = "groupCreateTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getGroupCreateTime() {
        return groupCreateTime;
    }

    public void setGroupCreateTime(Timestamp groupCreateTime) {
        this.groupCreateTime = groupCreateTime;
    }

    @Column(name = "groupIntroduction")
    public String getGroupIntroduction() {
        return groupIntroduction;
    }

    public void setGroupIntroduction(String groupIntroduction) {
        this.groupIntroduction = groupIntroduction;
    }

    @Column(name = "groupUserCount")
    public int getGroupUserCount() {
        return groupUserCount;
    }

    public void setGroupUserCount(int groupUserCount) {
        this.groupUserCount = groupUserCount;
    }

    @JsonIgnoreProperties(value = {"groupList"})
    @ManyToMany(mappedBy = "groupList",fetch = FetchType.EAGER)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
