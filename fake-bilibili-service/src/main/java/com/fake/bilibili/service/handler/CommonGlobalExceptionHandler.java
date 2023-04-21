package com.fake.bilibili.service.handler;

import com.fake.bilibili.domain.JsonResponse;
import com.fake.bilibili.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service.handler
 * @Author: 潘星星
 * @Create: 2023/4/15 13:00
 * @Description:
 */

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    // 抛出的所有错误，都会在这里被处理，然后发给用户
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e){
        String errorMsg = e.getMessage();
        if(e instanceof ConditionException){
            String errorCode = ((ConditionException)e).getCode();
            return new JsonResponse<>(errorCode, errorMsg);
        }else {
            return new JsonResponse<>("500", errorMsg);
        }
    }
}
