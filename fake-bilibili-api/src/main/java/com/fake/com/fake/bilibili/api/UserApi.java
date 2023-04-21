package com.fake.com.fake.bilibili.api;


import com.alibaba.fastjson.JSONObject;
import com.fake.bilibili.domain.JsonResponse;
import com.fake.bilibili.domain.PageResult;
import com.fake.bilibili.domain.User;
import com.fake.bilibili.domain.UserInfo;
import com.fake.bilibili.domain.annotation.DataLimited;
import com.fake.bilibili.service.UserFollowingService;
import com.fake.bilibili.service.UserService;
import com.fake.bilibili.service.utils.RSAUtil;
import com.fake.com.fake.bilibili.api.support.UserSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedInputStream;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.com.fake.bilibili.api
 * @Author: 潘星星
 * @Create: 2023/4/15 15:37
 * @Description:
 */

@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserFollowingService userFollowingService;

    @GetMapping("/users")
    public JsonResponse<User> getUserInfo(){
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey(){
        // get RSA coding public key
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/users")
    public  JsonResponse<String> addUser(@RequestBody User user){
        userService.addUser(user);
        // we clarified all these exceptions in the service layer, so we can simply return success
        return JsonResponse.success();
    }

    @PostMapping("/user-tokens")
    public  JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @PutMapping("/users")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return  JsonResponse.success();
    }

    //  4-17
    @PutMapping("/user-infos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return  JsonResponse.success();
    }

    // 让主用户在进行用户查询时，可以得到几个用户作为思路启发,(分页查询)
    //方法名向我们提示这是一共分页查询接口  @RequestParam表示我们一定要这个参数
    @GetMapping("/user-infos")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no,@RequestParam Integer size, String nick){
        Long userId = userSupport.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if(result.getTotal() > 0){
            //查看我们搜索的用户对象，我们早已经关注过哪些
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }

    @PostMapping("/user-dts")
    public  JsonResponse<Map<String, Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    @DeleteMapping("/refresh-tokens")
    public JsonResponse<String> logout(HttpServletRequest request){
        //用request获得refresh-token
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logout(refreshToken, userId);
        return JsonResponse.success();
    }
    //利用刷新令牌获取登录令牌
    @PostMapping("/access-tokens")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }
}
