package com.dontwait.server.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dontwait.server.entity.UserInfo;

@Mapper
public interface UserInfoMapper {

    @Insert("INSERT INTO user_info (user_id, phone, shop_name, shop_email, shop_description, pickup_address) " +
            "VALUES (#{userId}, #{phone}, #{shopName}, #{shopEmail}, #{shopDescription}, #{pickupAddress})")
    int insertUserInfo(UserInfo userInfo);

    @Select("SELECT * FROM user_info WHERE user_id = #{userId}")
    UserInfo findByUserId(@Param("userId") UUID userId);
}
