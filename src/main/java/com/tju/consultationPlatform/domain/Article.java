package com.tju.consultationPlatform.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "article")
@Entity
public class Article {
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "headline", nullable = false)
    private String headline;
    @Column(name = "author")
    private String author;
    @Column(name = "content_url", nullable = false)
    private String content_url;
    @Column(name = "create_time", nullable = false)
    private String createTime;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime(){
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
