package com.ah.service.Impl;

import com.ah.common.BaseResp;
import com.ah.dao.UserMapper;
import com.ah.pojo.TbUser;
import com.ah.pojo.req.TbUserReq;
import com.ah.service.UserService;
import com.ah.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RedisUtils redisUtils;

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public BaseResp sendMail(String email) {
        //定义返回类型
        BaseResp resultResp = new BaseResp();
        //去数据库查询，保证邮箱的唯一性
        if (email != null) {
            TbUser byEmail = userMapper.findByEmail(email);
            if (byEmail != null) {
                resultResp.setCode(201);
                resultResp.setMessage("当前邮箱已被占用");
                return resultResp;
            }
            //进行邮箱的发送
            //定义发送的验证码
            Random random = new Random();
            StringBuffer code = new StringBuffer();
            for (int i = 0; i < 4; i++) {
                int i1 = random.nextInt(10);
                code.append(i1);
            }
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(from);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("steam注册验证码：");
            simpleMailMessage.setText(code.toString());
            javaMailSender.send(simpleMailMessage);
            //发送成功后，存入redis
            redisUtils.set(email, code.toString());

            //设置一个过期时间
            redisUtils.expire(email, 6000);
            //设置返回值
            resultResp.setCode(200);
            resultResp.setMessage("邮件发送成功");
            return resultResp;

        }
        resultResp.setCode(202);
        resultResp.setMessage("邮箱不能为空");
        return resultResp;

    }

    @Override
    public BaseResp registry(TbUserReq tbUserReq) {
        BaseResp resultResp = new BaseResp();
        //判断用户名
        String userName = tbUserReq.getUserName();
        TbUser byUserName = userMapper.findByUserName(userName);
        if (byUserName != null) {
            resultResp.setCode(203);
            resultResp.setMessage("该用户已被注册");
            return resultResp;
        }
        //判断验证码
        String code = tbUserReq.getCode();
        String email = tbUserReq.getEmail();
        //从redis中获取code
        String o = (String) redisUtils.get(email);
        if (o != null && code.equals(o)) {
            //验证码通过，将数据添加到数据库中
            TbUser tbUser = new TbUser();
            BeanUtils.copyProperties(tbUserReq, tbUser);
            //添加到数据库
            userMapper.registry(tbUser);
            resultResp.setCode(200);
            resultResp.setMessage("注册成功");
            return resultResp;

        }
        resultResp.setCode(205);
        resultResp.setMessage("注册失败");
        return resultResp;
    }

    @Override
    public BaseResp relogin(TbUserReq tbUserReq) {
        //获取用户查询的密码
        BaseResp resultResp = new BaseResp();
        String userName = tbUserReq.getUserName();
        TbUser byUserName = userMapper.findByUserName(userName);
        //进行密码的比对
        if (byUserName == null) {
            resultResp.setCode(204);
            resultResp.setMessage("账号不存在！");
            return resultResp;
        } else if (tbUserReq.getPassword() != null && tbUserReq.getPassword().equals(byUserName.getPassword())) {
            //密码比对通过，放行，京用户信息存入到redis，设置token值
            //生成用户唯一标识
            UUID uuid = UUID.randomUUID();
            redisUtils.set(uuid.toString(), byUserName);
            resultResp.setData(uuid.toString());
            resultResp.setMessage("登录成功");
            resultResp.setCode(200);
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("密码错误");
        return resultResp;
    }

    @Override
    public BaseResp findByuuid(HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "token":
                        token = cookie.getValue();
                        break;
                    default:
                        break;
                }
            }
        }
        Object o = redisUtils.get(token);
        if (o != null) {
            resultResp.setCode(200);
            resultResp.setData(o);
            resultResp.setMessage("这是一个用户信息");
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("没有找到用户信息");
        return resultResp;
    }

    @Override
    public BaseResp findByUserId(Integer userId) {
        BaseResp resultResp = new BaseResp();
        TbUser byUserId = userMapper.findByUserId(userId);
        if (byUserId != null) {
            resultResp.setCode(200);
            resultResp.setData(byUserId);
            resultResp.setMessage("这是一个用户的全部信息");
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("查无此人");
        return resultResp;
    }

    @Override
    public BaseResp logout(HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String name = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    name = cookie.getValue();
                }
            }
        }
        if (name != null) {
            redisUtils.del(name);
            resultResp.setCode(200);
            resultResp.setMessage("注销成功！");
            return resultResp;
        }
        resultResp.setCode(201);
        resultResp.setMessage("注销失败！！");
        return resultResp;
    }

    @Override
    public BaseResp uploadAvatar(HttpServletRequest request, String pic) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String name = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                name = cookie.getValue();
            }
        }
        if (name != null) {
            Object o = redisUtils.get(name);
            if (o != null) {
                Object o1 = JSONObject.toJSON(o);
                TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);
                tbUser.setPic(pic);
                Integer integer = userMapper.updateByUser(tbUser);
                if (integer == 1) {
                    redisUtils.set(name, tbUser);
                    resultResp.setCode(200);
                    resultResp.setMessage("用户头像修改成功！！");
                    return resultResp;
                }
            }
        }
        resultResp.setCode(201);
        resultResp.setMessage("redis没有该用户名！");
        return resultResp;
    }

    @Override
    public BaseResp findUserByCookie(HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String name = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    name = cookie.getValue();
                }
            }
        }
        if (name != null) {
            Object o = redisUtils.get(name);
            if (o != null) {
                Object o1 = JSONObject.toJSON(o);
                TbUser tbUser = JSONObject.parseObject(o1.toString(), TbUser.class);
                resultResp.setCode(200);
                resultResp.setData(tbUser);
                return resultResp;
            }
        }
        resultResp.setCode(201);
        resultResp.setMessage("没有该用户信息");
        return resultResp;
    }

    @Override
    public BaseResp updateUser(TbUser tbUser, HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String name = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                name = cookie.getValue();
            }
        }
        if (name != null) {
            redisUtils.set(name, tbUser);
            Integer integer = userMapper.updateByUser(tbUser);
            if (integer == 1) {
                resultResp.setCode(200);
                resultResp.setMessage("修改成功！");
                return resultResp;
            }
        }
        resultResp.setCode(201);
        resultResp.setMessage("没有该用户信息");
        return resultResp;
    }
}
