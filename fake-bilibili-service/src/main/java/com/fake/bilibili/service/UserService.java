package com.fake.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.fake.bilibili.dao.UserDao;
import com.fake.bilibili.domain.PageResult;
import com.fake.bilibili.domain.RefreshTokenDetail;
import com.fake.bilibili.domain.User;
import com.fake.bilibili.domain.UserInfo;
import com.fake.bilibili.domain.constant.UserConstant;
import com.fake.bilibili.domain.exception.ConditionException;
import com.fake.bilibili.service.utils.MD5Util;
import com.fake.bilibili.service.utils.RSAUtil;
import com.fake.bilibili.service.utils.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service
 * @Author: 潘星星
 * @Create: 2023/4/15 15:36
 * @Description:
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthService userAuthService;

    public void addUser(User user) {
        String phone = user.getPhone();

        // 1. check the phone is suit the pattern or not
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("Phone should not be empty");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser != null){
            throw new ConditionException("The phone is already registered");
        }

        // 2. Start to register
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        // Front-End will send us encrypted password
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("Fail to decrypt the password");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);
        //use mapper to send user in the db
        userDao.addUser(user);
        // and add userinfo
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
        //给新用户添加默认角色
        userAuthService.addUserDefaultRole(user.getId());
    }

    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        String phone = user.getPhone();

        // 1. check the phone is suit the pattern or not
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("Phone should not be empty");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("The user is not exist");
        }
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("Fail to decrypt the password");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("Password is wrong!");
        }
        // Generate token
        return  TokenUtil.generateToken(dbUser.getId());
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) throws Exception {
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if(dbUser == null){
            throw new ConditionException("The user is not exist");
        }
        if(!StringUtils.isNullOrEmpty(user.getPassword())){
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        userDao.updateUsers(user);
    }

    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        userDao.updateUserInfo(userInfo);
    }

    public User getUserById(Long followingId) {
        User user = userDao.getUserById(followingId);
        return  user;
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserInfoByUserIds(userIdList);
    }

    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        //计算页面起始
        params.put("star", (no-1)*size);
        params.put("limit", size);
        //先根据我们输入条件，判断满足条件的用户有多少
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total,list);
    }

    public Map<String, Object> loginForDts(User user) throws Exception {

        String phone = user.getPhone();

        // 1. check the phone is suit the pattern or not
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("Phone should not be empty");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser == null){
            throw new ConditionException("The user is not exist");
        }
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("Fail to decrypt the password");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("Password is wrong!");
        }
        Long userId = dbUser.getId();
        // Generate access token
        String accessToken =  TokenUtil.generateToken(dbUser.getId());

        //Generate Refresh token
        String refreshToken =TokenUtil.generateRefreshToken(dbUser.getId());
        //保存refreshtoken到数据库中
        userDao.deleteRefreshToken(refreshToken, userId);
        userDao.addRefreshToken(refreshToken, userId, new Date());
        Map<String,Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return  result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken, userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if(refreshTokenDetail == null){
            throw new ConditionException("555", "token过期!");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateToken(userId);
    }
}
