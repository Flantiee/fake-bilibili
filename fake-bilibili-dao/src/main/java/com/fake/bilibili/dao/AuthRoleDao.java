package com.fake.bilibili.dao;

import com.fake.bilibili.domain.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/19 22:54
 * @Description:
 */
@Mapper
public interface AuthRoleDao {
    AuthRole getRoleByCode(String code);
}
