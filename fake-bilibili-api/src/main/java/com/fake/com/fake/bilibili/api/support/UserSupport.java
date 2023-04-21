package com.fake.com.fake.bilibili.api.support;

import com.fake.bilibili.domain.exception.ConditionException;
import com.fake.bilibili.service.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.com.fake.bilibili.api.support
 * @Author: 潘星星
 * @Create: 2023/4/15 22:13
 * @Description:
 */
@Component
public class UserSupport {

    public Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);

        if(userId<0){
            throw new ConditionException("Illegal user !");
        }
        return userId;
    }
}
