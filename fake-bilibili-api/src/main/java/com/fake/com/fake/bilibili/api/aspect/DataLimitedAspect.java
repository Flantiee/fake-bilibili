package com.fake.com.fake.bilibili.api.aspect;

import com.fake.bilibili.domain.UserMoment;
import com.fake.bilibili.domain.annotation.ApiLimitedRole;
import com.fake.bilibili.domain.auth.UserRole;
import com.fake.bilibili.domain.constant.AuthRoleConstant;
import com.fake.bilibili.domain.exception.ConditionException;
import com.fake.bilibili.service.UserRoleService;
import com.fake.com.fake.bilibili.api.support.UserSupport;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DataLimitedAspect {

    //查看用户传入的参数有没有问题
    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.fake.bilibili.domain.annotation.DataLimited)")
    public void check(){

    }

    @Before("check() ")
    public void doBefore(JoinPoint joinPoint){
        //(专门用在UserMomentAPI)
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set<String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //获取请求参数
        Object [] args = joinPoint.getArgs();
        for(Object arg : args){
            if(arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)){
                    throw new ConditionException("参数异常");
                }
            }
        }

    }
}
