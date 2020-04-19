package com.example.demo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String title;
    private String description;
    private String mdUrl;
    private String belong;
    private String label;
    private Date time;
    private Integer count;
    private String type;

    @ManyToOne(optional = false)
    private User user;

    @Override
    public String toString() {
        return "{" +
                "'id':'" + getId() + "'" +
                ", 'title':'" + getTitle() + "'" +
                ", 'mdUrl':'" + getMdUrl() + "'" +
                ", 'belong':'" + getBelong() + "'" +
                ", 'label':'" + getLabel() + "'" +
                ", 'time':'" + getTime() + "'" +
                ", 'count':'" + getCount() + "'" +
                ", 'type':'" + getType() + "'" +
                ", 'user':'" + getUser().toString() + "'" +
                "}";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMdUrl() {
        return mdUrl;
    }

    public void setMdUrl(String mdUrl) {
        this.mdUrl = mdUrl;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
