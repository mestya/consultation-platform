package com.tju.consultationPlatform.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * WebSocket消息模型
 */
@Entity
@Table(name = "message")
public class Message {

    private int id;
    private int type;//0为通知消息，1为单聊，2为群聊 3关系建立申请消息
    private String from;// 推送人ID
    private String to;// 定点推送人ID
    private String data;//消息体
    private Date createDate = new Date();//推送时间
    private int isTransport; // 消息状态
    private String group;//如果信息属于群聊则设置群聊组号

    public Message() {

    }

    public Message(int id, int type, String from, String to,String data, Date createDate, Integer isTransport) {
        this.id = id;
        this.type = type;
        this.from = from;
        this.to = to;
        this.data = data;
        this.createDate = createDate;
        this.isTransport = isTransport;
    }

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "msgId")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "from_id")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Column(name = "to_id")
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(name = "createDate")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "isTransport")
    public int getIsTransport() {
        return isTransport;
    }

    public void setIsTransport(Integer isTransport) {
        this.isTransport = isTransport;
    }
    @Column(name = "group_id")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "  type =" + type +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", data='" + data + '\'' +
                ", createDate=" + createDate +
                ", isTransport=" + isTransport +
                '}';
    }
}