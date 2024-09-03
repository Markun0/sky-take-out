package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where open_id = #{openId}")
    User getByOpenId(String openId);


    @Insert("insert into user(openid,name,phone,sex,id_number,avatar) values (#{user.openId},#{user.name},#{user.phone},#{user.sex},#{user.idNumber},#{user.avatar})")
    void insert(User user);
}
