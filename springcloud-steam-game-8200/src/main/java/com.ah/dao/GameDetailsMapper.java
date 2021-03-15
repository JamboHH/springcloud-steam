package com.ah.dao;


import com.ah.pojo.GameDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GameDetailsMapper {

    GameDetails findOne(@Param("id") Integer id);
}
