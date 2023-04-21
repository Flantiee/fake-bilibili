package com.fake.bilibili.dao;

import com.alibaba.fastjson.JSONObject;
import com.fake.bilibili.domain.RefreshTokenDetail;
import com.fake.bilibili.domain.User;
import com.fake.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/15 15:42
 * @Description:
 */
@Mapper
public interface UserDao {


     User getUserByPhone(String phone);

     Integer addUser(User user);

     Integer  addUserInfo(UserInfo userInfo);

     User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    Integer updateUserInfo(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer pageCountUserInfos(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(JSONObject params);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
                               @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId,
                            @Param("date") Date createTime);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);
}
