package com.fake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake
 * @Author: 潘星星
 * @Create: 2023/4/14 22:44
 * @Description:
 */
@SpringBootApplication
public class fakeBilibiliApp {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(fakeBilibiliApp.class,args);
    }
}
