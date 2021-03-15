package com.ah.controller;

import com.ah.common.BaseResp;
import com.ah.service.GaSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SearchController {
    @Autowired
    private GaSearchService gaSearchService;

    @PostMapping("/selectKey")
    public BaseResp selectKey(@RequestBody Map map) {
        return gaSearchService.selectKey(map);
    }
}
