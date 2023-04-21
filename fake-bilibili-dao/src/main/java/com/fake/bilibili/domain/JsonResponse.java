package com.fake.bilibili.domain;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.domain
 * @Author: 潘星星
 * @Create: 2023/4/15 12:23
 * @Description:
 */
public class JsonResponse<T> {

    private String code;

    private String msg;

    private T data;

    public JsonResponse(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public JsonResponse(T data){ //use the word key , can actually contain a lot of different types
        this.data = data;
        msg = "success";
        code = "0";
    }

    public static  JsonResponse<String> success(){
        return new JsonResponse<>(null);
    } //传入了一个null代表调用二号构造

    public static  JsonResponse<String> success(String data){
        return new JsonResponse<>(data);
    }

    public static  JsonResponse<String> fail(){
        return new JsonResponse<>("1", "fail");
    }

    public static  JsonResponse<String> fail(String code, String msg){
        return new JsonResponse<>(code, msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDate() {
        return data;
    }

    public void setDate(T data) {
        this.data = data;
    }
}
