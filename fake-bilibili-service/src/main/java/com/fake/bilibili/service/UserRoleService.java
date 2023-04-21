package com.fake.bilibili.service;

import com.fake.bilibili.dao.UserRoleDao;
import com.fake.bilibili.domain.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service
 * @Author: 潘星星
 * @Create: 2023/4/19 16:36
 * @Description:
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userRoleDao.getUserRoleByUserId(userId);
    }

    public void addUserRole(UserRole userRole) {
        userRole.setCreateTime(new Date());
        userRoleDao.addUserRole(userRole);
    }
}
