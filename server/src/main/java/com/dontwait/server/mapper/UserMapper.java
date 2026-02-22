package com.dontwait.server.mapper;

import java.util.UUID;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dontwait.server.entity.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE user_phone = #{phone}")
    User findByPhone(@Param("phone") String phone);

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User findById(@Param("userId") UUID userId);

    @Insert("INSERT INTO users (user_phone, user_password, name, phone_verified) " +
            "VALUES (#{userPhone}, #{userPassword}, #{name}, #{phoneVerified})")
    int insertUser(User user);

    @Update("UPDATE users SET user_password = #{password}, updated_at = CURRENT_TIMESTAMP " +
            "WHERE user_id = #{userId}")
    int updatePassword(@Param("userId") UUID userId, @Param("password") String password);

    @Update("UPDATE users SET phone_verified = true, updated_at = CURRENT_TIMESTAMP " +
            "WHERE user_phone = #{phone}")
    int setPhoneVerified(@Param("phone") String phone);
}
