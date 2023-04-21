package com.fake.bilibili.service;

import com.fake.bilibili.domain.auth.*;
import com.fake.bilibili.domain.constant.AuthRoleConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service.handler
 * @Author: 潘星星
 * @Create: 2023/4/19 16:23
 * @Description:
 */
@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;
    public UserAuthorities getUserAuthorities(Long userId) {
        //查找该用户扮演了几种职位
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        //从列表中抓取每个UserRole的id，并且使用了 “方法引用”
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole :: getUserId).collect(Collectors.toSet());
        //接下来要查询两种不同的权限信息
        //可操作的元素
        List<AuthRoleElementOperation> roleElementOperationList = authRoleService.getRoleElementOperationByRoleIds(roleIdSet);
        //可进入的页面
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenuListByRoleIds(roleIdSet);
        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setRoleMenuList(authRoleMenuList);
        userAuthorities.setRoleElementOperationList(roleElementOperationList);
        return userAuthorities;
    }

    public void addUserDefaultRole(Long id) {
        UserRole userRole = new UserRole();
        AuthRole role = authRoleService.getRoleByCode(AuthRoleConstant.ROLE_LV0);
        userRole.setUserId(id);
        userRole.setRoleId(role.getId());
        userRoleService.addUserRole(userRole);
    }
}
