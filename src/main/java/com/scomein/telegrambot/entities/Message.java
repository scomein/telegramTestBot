package com.scomein.telegrambot.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message implements Serializable{

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id", unique = true, nullable = false, length = 10)
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "createDate", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Message() {

    }

    public Message(long id, String text, Date createDate, User user) {
        this.id = id;
        this.text = text;
        this.createDate = createDate;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
