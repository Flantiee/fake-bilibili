package com.fake.bilibili.service;

import com.fake.bilibili.dao.AuthRoleMenuDao;
import com.fake.bilibili.domain.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service
 * @Author: 潘星星
 * @Create: 2023/4/19 18:13
 * @Description:
 */
@Service
public class AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;

    public List<AuthRoleMenu> getAuthRoleMenuListByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuDao.getAuthRoleMenuListByRoleIds(roleIdSet);
    }
}
