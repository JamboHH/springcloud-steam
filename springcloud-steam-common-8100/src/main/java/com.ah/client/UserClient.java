package com.ah.client;

import com.ah.common.BaseResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "steam-user")
//@FeignClient(name = "steam-user", fallback = GoodsFallBack.class)
public interface UserClient {
    @RequestMapping(value = "/user/findByUserId", method = RequestMethod.POST)
    public BaseResp findByUserId(@RequestBody Map map);
}
