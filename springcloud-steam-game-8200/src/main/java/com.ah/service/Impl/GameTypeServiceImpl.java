package com.ah.service.Impl;

import com.ah.common.BaseResp;
import com.ah.dao.GameTypeRespository;
import com.ah.pojo.GameType;
import com.ah.service.GameTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameTypeServiceImpl implements GameTypeService {
    @Autowired
    GameTypeRespository gameTypeRespository;

    @Override
    public BaseResp findALL() {
        BaseResp resultResp = new BaseResp();
        List<GameType> all = gameTypeRespository.findAll();
        if (all != null) {
            resultResp.setCode(200);
            resultResp.setMessage("查询到类别集合");
            resultResp.setData(all);
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("查询错误");
        return resultResp;
    }
}
