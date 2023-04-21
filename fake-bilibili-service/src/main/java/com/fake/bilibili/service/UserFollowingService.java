package com.fake.bilibili.service;

import com.fake.bilibili.dao.UserFollowingDao;
import com.fake.bilibili.domain.FollowingGroup;
import com.fake.bilibili.domain.User;
import com.fake.bilibili.domain.UserFollowing;
import com.fake.bilibili.domain.UserInfo;
import com.fake.bilibili.domain.constant.UserConstant;
import com.fake.bilibili.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Project_Name: fake-bilibili
 * @Package_Name: com.fake.bilibili.service
 * @Author: 潘星星
 * @Create: 2023/4/16 18:26
 * @Description:
 */
@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    // 我们要先删除后增加，该注解可以，如果删除成功，但是增加失败，我们可以数据库回滚，保证数据完整
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing){
        Long groupId = userFollowing.getGroupId();
        if(groupId == null){
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        }else{
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if(followingGroup == null){
                throw new ConditionException("The group that you had chosen is not exist");
            }
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getUserById(followingId);
        if(user == null){
            throw new ConditionException("The user that you want to subscribe is not exist");
        }
        // First we delete the former relationship
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }

    // 第一步：获取关注的用户列表
    // 第二步：根据关注用户的id查询关注用户的基本信息
    // 第三步：将关注用户按关注分组进行分类
    public List<FollowingGroup> getUserFollowings(Long userId){

        List<UserFollowing> list = userFollowingDao.getUserFollowings(userId); //获取与主用户有关的following_user表的内容
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());//将list改为set去重
        List<UserInfo> userInfoList = new ArrayList<>();
        if(followingIdSet.size() > 0){
            //获取一堆该主用户所关注用户他们的信息
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }
        //将对应用户的信息填进主用户各关注目标的对象(升级查询到的user_following表内容)
        for(UserFollowing userFollowing : list){
            for(UserInfo userInfo : userInfoList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        //获取主用户所创建的关注分组(自己创的分组+三个默认分组)
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        //将主用户所有目标关注人的信息填入(不进行分组)
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for(FollowingGroup group:groupList){
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list ){
                if(group.getId().equals(userFollowing.getGroupId())){
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }

    // 第一步：获取当前用户的粉丝列表
    // 第二步：根据粉丝的用户id查询基本信息
    // 第三步：查询当前用户是否已经关注该粉丝
    public List<UserFollowing> getUserFans(Long userId){
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId); //查看following_group表中 followingId与主用户id对应的条目
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());//抓取主用户粉丝的id集合(去重)
        List<UserInfo> userInfoList = new ArrayList<>();
        if(fanIdSet.size() > 0){
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }
        //查看有无我们关注过的用户
        List<UserFollowing> followingList = userFollowingDao.getUserFollowings(userId); //获取主用户关注的列表
        for (UserFollowing fan : fanList){
            for(UserInfo userInfo : userInfoList){
                if(fan.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false); //这个可以不写在这
                    //如果找到该粉丝对应的个人信息，就赋值上去
                    fan.setUserInfo(userInfo);
                }
            }
            //查询与主用户互粉的用户，并且对该用户设置互粉状态
            for(UserFollowing following : followingList){
                if(following.getFollowingId().equals(fan.getUserId())){
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }

    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        // 在Dao中设置了自动返回id，并且赋值给该对象
        return followingGroup.getId();
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }

    //查看是否关注过此人
    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowings(userId);
        for(UserInfo userInfo : userInfoList){
            userInfo.setFollowed(false);
            for(UserFollowing userFollowing : userFollowingList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }
}
