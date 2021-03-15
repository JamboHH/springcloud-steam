package com.ah.service.Impl;

import com.ah.common.BaseResp;
import com.ah.dao.GameOrderMapper;
import com.ah.pojo.Game;
import com.ah.pojo.GameOrder;
import com.ah.pojo.TbUser;
import com.ah.service.GameOrderService;
import com.ah.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class GameOrderServiceImpl implements GameOrderService {

    @Autowired
    RedisTemplate redisTemplate;

/*    @Autowired
    GameService gameService;*/

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    GameOrderMapper gameOrderMapper;


    @Override
    public BaseResp toOrder(Integer id, HttpServletRequest request) {
        String token = getToken(request);
        Object o = redisUtils.get(token);
        //将Object转换为user对象
        Object o1 = JSONObject.toJSON(o);
        TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);

        Object o3 = redisTemplate.opsForValue().get("order_" + tbUser.getId());

        BaseResp resultResp = new BaseResp();
        if (o3 != null) {
            String o4 = (String) JSONObject.toJSON(o3);
            Object o2 = redisTemplate.opsForValue().get(o4);
            if (o2 != null) {
                Object o5 = JSONObject.toJSON(o2);
                GameOrder gameOrder = JSONObject.parseObject(o5.toString(), GameOrder.class);
                resultResp.setCode(201);
                resultResp.setData(gameOrder);
                resultResp.setMessage("你有未完成的订单，请完成支付！！");
                return resultResp;
            }
        }

        Object o2 = redisTemplate.opsForHash().get("cart_" + tbUser.getId(), id.toString());
        Game game = null;
        if (o2 != null) {
            Object o5 = JSONObject.toJSON(o2);
            game = JSONObject.parseObject(o5.toString(), Game.class);
        }

        if (game != null) {
            GameOrder gameOrder = new GameOrder();
            UUID uuid = UUID.randomUUID();
            String replace = uuid.toString().replace("-", "");
            gameOrder.setOrderid(replace);
            gameOrder.setGamename(game.getGName());
            gameOrder.setPic(game.getGPic());
            gameOrder.setPrice(game.getGPrice());
            gameOrder.setNum(game.getNum());
            gameOrder.setStatus("UNPAID");
            gameOrder.setUid(tbUser.getId());
            gameOrder.setGid(game.getId());
            // gameOrder存储到redis中，设置定时，超时，订单失效，从redis 中删除,
            // gameOrder订单完成支付后改变订单状态，存入数据库中
            redisTemplate.opsForValue().set(gameOrder.getOrderid(), gameOrder, 60 * 5, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set("order_" + tbUser.getId(), gameOrder.getOrderid(), 60 * 5, TimeUnit.SECONDS);
            resultResp.setCode(200);
            resultResp.setData(gameOrder);
            resultResp.setMessage("订单生成成功！！");
            return resultResp;
        }
        resultResp.setCode(202);
        resultResp.setMessage("生成订单失败！");
        return resultResp;
    }

    @Override
    public Integer addOrder(GameOrder gameOrder) {
        return gameOrderMapper.addOrder(gameOrder);
    }

    @Override
    public BaseResp findAll(HttpServletRequest request) {
        String token = getToken(request);
        Object o = redisUtils.get(token);
        //将Object转换为user对象
        Object o1 = JSONObject.toJSON(o);
        TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);
        BaseResp resultResp = new BaseResp();
        List<GameOrder> all = gameOrderMapper.findAll(tbUser.getId());
        if (all != null) {
            resultResp.setCode(200);
            resultResp.setData(all);
            resultResp.setMessage("查询成功！！");
            return resultResp;
        } else {
            resultResp.setCode(201);
            resultResp.setMessage("还没有购买过游戏");
            return resultResp;
        }
    }

    public String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
