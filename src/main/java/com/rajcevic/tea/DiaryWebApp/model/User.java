package com.rajcevic.tea.DiaryWebApp.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    private final String username;
    private final String password;
    private final Boolean enabled;
    private String account;
    private int uploadLimit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "UPLOAD_LIMIT_DATE")
    private Date createdAt;

    public User() {
        this.username = null;
        this.password = null;
        this.enabled = null;
        this.account = null;
    }

    public User(String username, String password, String account) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.account = account;
        if(account.equals("standard")) {
            this.uploadLimit = 3;
        }
        else if(account.equals("gold")) {
            this.uploadLimit = 4;
        }
        else {
            this.uploadLimit = 2;
        }

        Date date = new Date(System.currentTimeMillis());
        this.setCreatedAt(date);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public int getUploadLimit() {
        return uploadLimit;
    }

    public void setUploadLimit(int value) {
        this.uploadLimit = value;
    }

    public String getCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy ");
        return formatter.format(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
