package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Random;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String announce;
    private String rnd;
    private String icode;

    @Override
    public String toString() {
        return "{" +
                "'id':'" + getId() + "'" +
                ", 'username':'" + getUsername() + "'" +
                ", 'nickname':'" + getNickname() + "'" +
                ", 'password':'" + getPassword() + "'" +
                ", 'email':'" + getEmail() + "'" +
                ", 'announce':'" + getAnnounce() + "'" +
                ", 'rnd':'" + getRnd() + "'" +
                ", 'icode':'" + getIcode() + "'" +
                "}";
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnnounce() { return announce; }

    public void setAnnounce(String announce) { this.announce = announce; }

    public String getRnd() { return rnd; }

    private String getRandom(int length) {
        char []a = new char[62];
        String res = "";
        for (int i = 0; i <= 9; ++i)
            a[i] = (char)(i + '0');
        for (int i = 0; i <= 25; ++i) {
            a[i + 10] = (char)(i + 'a');
            a[i + 36] = (char)(i + 'A');
        }
        Random random = new Random();
        for (int i = 0; i < length; ++i) {
            res += a[random.nextInt(61)];
        }
        return res;
    }

    public void setRnd() {
        this.rnd = getRandom(10);
    }

    public String getIcode() {
        return icode;
    }

    public void setIcode() {
        this.icode = getRandom(5);
    }

    public void setIcode(String icode) {
        this.icode = icode;
    }
}
