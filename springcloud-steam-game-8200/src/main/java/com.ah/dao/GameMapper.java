package com.ah.dao;


import com.ah.pojo.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameMapper {
    List<Game> findAll();

    Game findOne(@Param("id") Integer id);

    List<Game> findGameByTypeId(Integer typeId);
}
