package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openId);

    @Insert("insert into user(openid,name,phone,sex,id_number,avatar) values (#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar})")
    void insert(User user);

}
