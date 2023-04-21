package com.fake.com.fake.bilibili.api.aspect;

import com.fake.bilibili.domain.annotation.ApiLimitedRole;
import com.fake.bilibili.domain.auth.UserRole;
import com.fake.bilibili.domain.exception.ConditionException;
import com.fake.bilibili.service.UserRoleService;
import com.fake.com.fake.bilibili.api.support.UserSupport;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.com.fake.bilibili.api.aspect
 * @Author: 潘星星
 * @Create: 2023/4/19 20:52
 * @Description:
 */
@Aspect
@Order(1)
@Component
public class ApiLimitedRoleAspect {

    //查看用户有没有权限，访问一些后端资源
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.fake.bilibili.domain.annotation.ApiLimitedRole)")
    public void check(){

    }

    @Before("check() && @annotation(apiLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ApiLimitedRole apiLimitedRole){
        //看用户有没有权限进行操作
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        //获取我们要限制进入的用户权限 (记住是限制进入)
        String [] limitedRoleCodeList = apiLimitedRole.limitedRoleCodeList();
        //将字符串数组中的元素存到set中，并且去重
        Set<String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //查看交际
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if(roleCodeSet.size() > 0){
            throw new ConditionException("权限不足");
        }
    }
}
