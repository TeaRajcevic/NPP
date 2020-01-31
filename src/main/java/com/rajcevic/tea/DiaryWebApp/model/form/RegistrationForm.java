package com.rajcevic.tea.DiaryWebApp.model.form;

import com.rajcevic.tea.DiaryWebApp.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationForm {

    private String username;
    private String password;
    private String account;

    public User toUser(PasswordEncoder passwordEncoder) throws ParseException {
        return new User(username, passwordEncoder.encode(password), account);
    }

    public RegistrationForm() {
    }

    public RegistrationForm(String username, String password, String account) {
        this.username = username;
        this.password = password;
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


}
