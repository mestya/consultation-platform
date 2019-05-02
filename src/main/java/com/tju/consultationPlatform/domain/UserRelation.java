package com.tju.consultationPlatform.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)//获取时间的注解
@Table(name = "relation")
public class UserRelation {
    private int id;
    private String userA;
    private String userB;
    private int relationType;//[1family/2friend/3doctor]
    private String MemoName;//备注名
    private Timestamp relationCreateTime;

    @Id
    @GenericGenerator(name = "generator", strategy = "identity")
    @GeneratedValue(generator = "generator")
    @Column(name = "id")
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "userA")
    public void setUserA(String id) {
        this.userA = id;
    }
    public String getUserA() {
        return this.userA;
    }

    @Column(name = "userB")
    public void setUserB(String id) {
        this.userB = id;
    }
    public String getUserB() {
        return this.userB;
    }

    @Column(name = "relationType")
    public void setRelationType(int type) {
        this.relationType = type;
    }
    public int getRelationType() {
        return relationType;
    }

    @Column(name = "memo_name")
    public void setMemoName(String name){this.MemoName = name;}
    public String getMemoName(){return this.MemoName;}

    @CreatedDate
    @Column(name = "createTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Timestamp getRelationCreateTime() {
        return this.relationCreateTime;
    }
    public void setRelationCreateTime(Timestamp createTime) {
        this.relationCreateTime = createTime;
    }
}
