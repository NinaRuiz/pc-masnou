package org.vaadin.example.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "service_comments")
public class ServiceComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "serviceId")
    private Integer serviceId;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "commentMessage")
    private String commentMessage;

    @Column(name = "commentDatetime")
    private Date commentDatetime;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentMessage() {
        return commentMessage;
    }

    public void setCommentMessage(String commentMessage) {
        this.commentMessage = commentMessage;
    }

    public Date getCommentDatetime() {
        return commentDatetime;
    }

    public void setCommentDatetime(Date commentDatetime) {
        this.commentDatetime = commentDatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
