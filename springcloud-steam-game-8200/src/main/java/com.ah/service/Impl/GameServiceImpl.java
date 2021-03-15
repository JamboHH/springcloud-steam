package com.ah.service.Impl;


import com.ah.common.BaseResp;
import com.ah.dao.GameMapper;
import com.ah.pojo.Game;
import com.ah.pojo.TbUser;
import com.ah.service.GameService;
import com.ah.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    GameMapper gameMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisUtils redisUtils;


    @Override
    public BaseResp findAll(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Game> all = gameMapper.findAll();
        PageInfo<Game> gamePageInfo = new PageInfo<>(all);
        BaseResp resultResp = new BaseResp();
        resultResp.setCode(200);
        resultResp.setData(all);
        resultResp.setTotal(gamePageInfo.getTotal());
        resultResp.setMessage("findAll查询全部成功！");
        return resultResp;
    }

    @Override
    public BaseResp findOne(Integer id) {
        BaseResp resultResp = new BaseResp();
        if (id != null && !(id.toString().equals(""))) {
            Game game = gameMapper.findOne(id);
            if (game != null) {
                resultResp.setCode(200);
                resultResp.setData(game);
                resultResp.setMessage("查询一个成功！！");
                return resultResp;
            }
            resultResp.setCode(202);
            resultResp.setMessage("没有找到id=" + id);
            return resultResp;
        }
        resultResp.setCode(201);
        return resultResp;
    }

    @Override
    public BaseResp addShopCart(Integer id, HttpServletRequest request) {
        String token = getToken(request);
        Object o = redisUtils.get(token);
        //将Object转换为user对象
        Object o1 = JSONObject.toJSON(o);
        TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);

        //创建该用户的购物车，
        // 用hash 类型存储，第一次key: cart_加上用户id
        // 第二层key：商品id  value: 商品详情

        //添加商品时，先判断redis中是否有这个商品，如果有 num++ 再把商品放到redis,覆盖原来的数据
        Object hget = redisUtils.hget("cart_" + tbUser.getId(), id.toString());
        boolean hset = false;
        if (hget != null) {//购物车有这个商品，num++
            Object cart1 = JSONObject.toJSON(hget);
            Game game = JSONObject.parseObject(cart1.toString(), Game.class);
            // TODO num++
            game.setNum(game.getNum() + 1);
            // 再存入 redis
            hset = redisUtils.hset("cart_" + tbUser.getId(), id.toString(), game);
        } else {
            Game one = gameMapper.findOne(id);
            one.setNum(1);
            hset = redisUtils.hset("cart_" + tbUser.getId(), id.toString(), one);
        }

        BaseResp resultResp = new BaseResp();
        if (hset) {
            resultResp.setCode(200);
            resultResp.setMessage("添加成功！！");
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("添加失败！ ");
        return resultResp;
    }

    @Override
    public BaseResp reduceShopCartGame(Integer id, HttpServletRequest request) {
        String token = getToken(request);
        Object o = redisUtils.get(token);
        //将Object转换为user对象
        Object o1 = JSONObject.toJSON(o);
        TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);
        BaseResp resultResp = new BaseResp();

        Object hget = redisUtils.hget("cart_" + tbUser.getId(), id.toString());
        boolean hset = false;
        if (hget != null) {//购物车有这个商品，num--
            Object cart1 = JSONObject.toJSON(hget);
            Game game = JSONObject.parseObject(cart1.toString(), Game.class);
            // TODO num--
            game.setNum(game.getNum() - 1);
            // 再存入 redis
            if (game.getNum() != 0) {
                hset = redisUtils.hset("cart_" + tbUser.getId(), id.toString(), game);
            } else {
                // 数量零，删除redis 中的商品
                redisUtils.hdel("cart_" + tbUser.getId(), id.toString());
                hset = true;
            }
        }

        if (hset) {
            resultResp.setCode(200);
            resultResp.setMessage("减少商品成功！！");
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("减少失败！ ");
        return resultResp;
    }

    @Override
    public BaseResp findShopCart(HttpServletRequest request) {
        String token = getToken(request);
        Object o = redisUtils.get(token);
        //将Object转换为user对象
        Object o1 = JSONObject.toJSON(o);
        TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);
        List values = redisTemplate.opsForHash().values("cart_" + tbUser.getId());
        BaseResp resultResp = new BaseResp();
        resultResp.setCode(200);
        resultResp.setData(values);
        resultResp.setMessage("查询购物车成功！");
        return resultResp;
    }

    @Override
    public BaseResp findGameByTypeId(Integer typeId) {
        BaseResp baseResp = new BaseResp();
        List<Game> gameList = gameMapper.findGameByTypeId(typeId);
        baseResp.setCode(200);
        baseResp.setMessage("成功");
        baseResp.setData(gameList);
        return baseResp;
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
