package com.fake.com.fake.bilibili.api;

import com.fake.bilibili.domain.JsonResponse;
import com.fake.bilibili.domain.auth.UserAuthorities;
import com.fake.bilibili.service.UserAuthService;
import com.fake.com.fake.bilibili.api.support.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.com.fake.bilibili.api
 * @Author: 潘星星
 * @Create: 2023/4/19 16:23
 * @Description:
 */
@RestController
public class UserAuthApi {

    @Autowired
    private UserSupport  userSupport;

    @Autowired
    private UserAuthService userAuthService;

    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
