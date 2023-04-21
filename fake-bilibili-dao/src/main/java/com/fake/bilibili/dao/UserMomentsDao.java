package com.fake.bilibili.dao;

import com.fake.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/18 10:56
 * @Description:
 */
@Mapper
public interface UserMomentsDao {
    Integer addUserMoments(@Param("userMoment") UserMoment userMoment);
}
