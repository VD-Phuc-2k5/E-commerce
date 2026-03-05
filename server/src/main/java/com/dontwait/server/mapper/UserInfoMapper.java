package com.dontwait.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.dontwait.server.entity.UserInfo;

@Mapper
public interface UserInfoMapper {

    @Insert("INSERT INTO user_info (user_id, phone, shop_name, shop_description) " +
            "VALUES (#{userId}, #{phone}, #{shopName}, #{shopDescription})")
    int insertUserInfo(UserInfo userInfo);

    @Select("SELECT * FROM user_info WHERE user_id = #{userId}")
    UserInfo findByUserId(String userId);
}
