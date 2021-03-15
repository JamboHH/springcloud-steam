package com.ah.client;

import com.ah.pojo.GameOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "steam-game")

public interface GameOrderClient {
    @RequestMapping(value = "/game/addOrder", method = RequestMethod.POST)
    public int addOrder(@RequestBody GameOrder gameOrder);
}
