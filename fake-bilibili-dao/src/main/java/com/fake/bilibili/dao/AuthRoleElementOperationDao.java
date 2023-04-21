package com.fake.bilibili.dao;

import com.fake.bilibili.domain.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.dao
 * @Author: 潘星星
 * @Create: 2023/4/19 18:18
 * @Description:
 */
@Mapper
public interface AuthRoleElementOperationDao {
    List<AuthRoleElementOperation> getRoleElementOperationByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
