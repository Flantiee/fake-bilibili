package com.fake.bilibili.domain.constant;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.domain.constant
 * @Author: 潘星星
 * @Create: 2023/4/15 16:48
 * @Description:
 */

// in the future if we want to modify the detail, we can do it here, instead of do it everwhere
public interface UserConstant {

    public static final String GENDER_MALE = "0";

    public static final String GENDER_FEMALE = "1";

    public static final String GENDER_UNKNOWN = "0";

    public static final String DEFAULT_BIRTH = "1999-10-01";

    public static final String DEFAULT_NICK = "萌新";

    public static final String USER_FOLLOWING_GROUP_TYPE_DEFAULT = "2";

    //用户自建分组
    public static final String USER_FOLLOWING_GROUP_TYPE_USER= "3";

    public static final String USER_FOLLOWING_GROUP_ALL_NAME = "全部关注";

}
