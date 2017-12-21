package com.taiji.fxsqjw.views.dao;

public class AppUser{

    private String username;

    private String uPw;

    private String nickname;

    private String phonenum;

    private String registerTime;

    private boolean isPwdEncrypt;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getuPw() {
        return uPw;
    }

    public void setuPw(String uPw) {
        this.uPw = uPw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isPwdEncrypt() {
        return isPwdEncrypt;
    }

    public void setPwdEncrypt(boolean pwdEncrypt) {
        isPwdEncrypt = pwdEncrypt;
    }
}
