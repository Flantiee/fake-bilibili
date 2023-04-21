package com.fake.bilibili.domain;

import java.util.Date;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.domain
 * @Author: 潘星星
 * @Create: 2023/4/15 15:23
 * @Description:
 */
public class User {

    private Long id;

    private String phone;

    private String email;

    private String password;

    private String salt;

    private Date CreateTime;

    private Date UpdateTime;

    // combine two tables together
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }

    public Date getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(Date updateTime) {
        UpdateTime = updateTime;
    }



}
