package com.taiji.fxsqjw.views.token;


/**
 * @Author:lighktin
 * @Mail:localwyh@gmail.com
 * @Project:JBox
 * @Description:Token的Model类，可以增加字段提高安全性，例如时间戳、url签名
 * @Date:下午2:39 17-7-22
 */
public class TokenModel {
    //用户id
    private String userId;

    private String uPwd;

    //随机生成的uuid
    private String token;

    public TokenModel(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public TokenModel(String userId,String uPwd, String token) {
        this.userId = userId;
        this.token = token;
        this.uPwd = uPwd;
    }

    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String uPwd) {
        this.uPwd = uPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
