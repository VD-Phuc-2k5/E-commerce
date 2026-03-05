package com.dontwait.server.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.dontwait.server.entity.UserAddress;
import java.util.UUID;
@Mapper
public interface UserAddressMapper {
    @Insert("INSERT INTO user_addresses (user_id, address, type, is_default, possition_map) " +
            "VALUES (#{userId}, #{address}, #{type}, #{isDefault}, #{possitionMap})")
    int insertUserAddress(UserAddress userAddress);

    @Select("SELECT * FROM user_addresses WHERE user_id = #{userId}")
    UserAddress findByUserId(UUID userId);
}
