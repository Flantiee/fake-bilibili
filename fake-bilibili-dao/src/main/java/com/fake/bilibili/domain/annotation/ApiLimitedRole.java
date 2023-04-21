package com.fake.bilibili.domain.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.domain.annotation
 * @Author: 潘星星
 * @Create: 2023/4/19 20:49
 * @Description:
 */
//配置注解运行的时间与作用范围
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ApiLimitedRole {

    String [] limitedRoleCodeList() default  {};
}
