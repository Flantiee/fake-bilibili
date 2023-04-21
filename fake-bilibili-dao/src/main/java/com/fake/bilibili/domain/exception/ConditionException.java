package com.fake.bilibili.domain.exception;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.domain.exception
 * @Author: 潘星星
 * @Create: 2023/4/15 13:01
 * @Description:
 */
public class ConditionException extends RuntimeException {

    private  static final long serialVersionUID = 1L;

    private  String code;

    public ConditionException(String code, String name){
        super(name);
        this.code = code;
    }

    public ConditionException(String name){
        super(name);
        code = "500";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
