package com.ah.service;

import com.ah.common.BaseResp;
import com.ah.pojo.GameOrder;

import javax.servlet.http.HttpServletRequest;

public interface GameOrderService {

    BaseResp toOrder(Integer id, HttpServletRequest request);

    Integer addOrder(GameOrder gameOrder);

    BaseResp findAll(HttpServletRequest request);
}
