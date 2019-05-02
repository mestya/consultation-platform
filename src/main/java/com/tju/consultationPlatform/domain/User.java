package com.tju.consultationPlatform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;
    @Column(name = "sex", nullable = true)
    private int sex;
    @Column(name = "role", nullable = false)
    private int role;
    @Column(name = "phonenumber", nullable = true)
    private String phonenumber;
    @Column(name="emergency",nullable = true)
    private String emergency;
    @JsonIgnoreProperties(value = {"userList"})
    @ManyToMany
    @JoinTable(name = "user_group", joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "groupId"))
    private List<Group> groupList;

    /**
     * 在线状态似乎不应该持久化到数据库，此处暂时保留，但修改时不使用。
     */
    @Transient
    private String plainPassword; // 登录时的密码，不持久化到数据库
    @Transient
    private int userIsOnline;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phoneNumber) {
        this.phonenumber = phoneNumber;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public int getUserIsOnline() {
        return userIsOnline;
    }

    public void setUserIsOnline(int userIsOnline) {
        this.userIsOnline = userIsOnline;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

}
