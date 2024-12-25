package com.example.chatproject.Mapper;

import com.example.chatproject.entity.register;
import com.example.chatproject.entity.user;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface chatMapper {
    @Insert("INSERT into users values (#{account},#{passWord},#{username})")
    int InsertUser(register r1);

    @Select("select * from users where account = #{account} and password = #{passWord}")
    register find(String account , String passWord);

    @Update("update users set passWord = #{passWord},username = #{username} where account = #{account}")
    int UpdateUser(String passWord, String username, String account );
}
