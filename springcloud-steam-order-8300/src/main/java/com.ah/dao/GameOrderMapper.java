package com.ah.dao;


import com.ah.pojo.GameOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameOrderMapper {

    Integer addOrder(GameOrder gameOrder);

    List<GameOrder> findAll(@Param("uid") Integer uid);

}
