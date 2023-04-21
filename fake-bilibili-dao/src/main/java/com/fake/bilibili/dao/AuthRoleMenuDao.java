package com.fake.bilibili.dao;

import com.fake.bilibili.domain.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/19 19:46
 * @Description:
 */
@Mapper
public interface AuthRoleMenuDao {

    List<AuthRoleMenu> getAuthRoleMenuListByRoleIds(Set<Long> roleIdSet);
}
