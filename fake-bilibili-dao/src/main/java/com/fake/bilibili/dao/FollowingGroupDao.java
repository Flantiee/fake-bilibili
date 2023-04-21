package com.fake.bilibili.dao;

import com.fake.bilibili.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/16 18:18
 * @Description:
 */
@Mapper
public interface FollowingGroupDao {

    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);

    List<FollowingGroup> getByUserId(@Param("userId") Long userId);
    Integer addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userId);
}
