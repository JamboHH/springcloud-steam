package com.ah.service;


import com.ah.common.BaseResp;

import javax.servlet.http.HttpServletRequest;

public interface GameService {

    BaseResp findAll(Integer page, Integer limit);


    BaseResp findOne(Integer id);

    BaseResp addShopCart(Integer id, HttpServletRequest request);

    BaseResp reduceShopCartGame(Integer id, HttpServletRequest request);

    BaseResp findShopCart(HttpServletRequest request);

    BaseResp findGameByTypeId(Integer typeId);
}
