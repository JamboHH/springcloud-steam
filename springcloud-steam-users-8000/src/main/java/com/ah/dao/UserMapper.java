package com.ah.dao;

import com.ah.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    TbUser findByEmail(@Param("email") String email);

    TbUser findByUserName(@Param("username") String name);

    int registry(TbUser tbUser);

    TbUser findByUserId(@Param("userId") Integer userId);

    Integer updateByUser(TbUser tbUser);
}
