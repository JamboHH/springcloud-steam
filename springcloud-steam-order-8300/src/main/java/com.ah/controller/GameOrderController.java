package com.ah.controller;

import com.ah.common.BaseResp;
import com.ah.service.GameOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class GameOrderController {

    @Autowired
    GameOrderService gameOrderService;

    @RequestMapping(value = "/toOrder", method = RequestMethod.POST)
    public BaseResp toOrder(@RequestBody Map map, HttpServletRequest request) {
        return gameOrderService.toOrder((Integer) map.get("id"), request);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public BaseResp findAll(HttpServletRequest request) {
        return gameOrderService.findAll(request);
    }
}
