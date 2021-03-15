package com.ah.controller;


import com.ah.common.BaseResp;
import com.ah.service.GameService;
import com.ah.service.GameTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameService gameService;
    @Autowired
    GameTypeService gameTypeService;

    @RequestMapping(value = "/findAll/{page}/{limit}", method = RequestMethod.GET)
    public BaseResp findAll(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit) {

        return gameService.findAll(page, limit);
    }

    @RequestMapping(value = "/findOne", method = RequestMethod.POST)
    public BaseResp findOne(@RequestBody Map map) {
        return gameService.findOne((Integer) map.get("id"));
    }

    //返回游戏所有类别
    @RequestMapping(value = "/findAllType", method = RequestMethod.GET)
    public BaseResp findAllType() {
        return gameTypeService.findALL();
    }

    @RequestMapping(value = "/addShopCart", method = RequestMethod.POST)
    public BaseResp addShopCart(@RequestBody Map map, HttpServletRequest request) {
        return gameService.addShopCart((Integer) map.get("id"), request);
    }

    @RequestMapping(value = "/reduceShopCartGame", method = RequestMethod.POST)
    public BaseResp reduce(@RequestBody Map map, HttpServletRequest request) {
        return gameService.reduceShopCartGame((Integer) map.get("id"), request);
    }

    @RequestMapping(value = "/findShopCart", method = RequestMethod.GET)
    public BaseResp findShopCart(HttpServletRequest request) {
        return gameService.findShopCart(request);
    }

    @PostMapping("/findGameByTypeId")
    public BaseResp findGameByTypeId(@RequestBody Map map){
        return gameService.findGameByTypeId((Integer) map.get("typeId"));
    }

}
