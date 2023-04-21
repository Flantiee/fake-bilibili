package com.fake.bilibili.dao;

import com.fake.bilibili.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/16 18:20
 * @Description:
 */
@Mapper
public interface UserFollowingDao {
    // New method of param
    Integer deleteUserFollowing(@Param("userId") Long userId, @Param("followingId") Long followingId);

    Integer addUserFollowing(@Param("UserFollowing")UserFollowing userFollowing);

    List<UserFollowing> getUserFollowings(@Param("userId")Long userId); //获取当前用户关注的对象列表

    List<UserFollowing> getUserFans(@Param("followingId") Long userId); //进行转换
}
