package com.ah.controller;

import com.ah.common.BaseResp;
import com.ah.pojo.TbUser;
import com.ah.pojo.req.TbUserReq;
import com.ah.service.UserService;
import com.ah.utils.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UploadFile uploadFile;

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public BaseResp sendMail(@RequestBody Map map) {
        return userService.sendMail(map.get("email").toString());

    }

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public BaseResp registry(@RequestBody TbUserReq tbUserReq) {
        return userService.registry(tbUserReq);
    }

    @RequestMapping(value = "/relogin", method = RequestMethod.POST)
    public BaseResp relogin(@RequestBody TbUserReq tbUserReq) {
        System.out.println("tbUserReq = " + tbUserReq);
        return userService.relogin(tbUserReq);
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    public BaseResp findUser(HttpServletRequest request) {
        return userService.findByuuid(request);
    }

    //查找一个用户
    @RequestMapping(value = "/findByUserId", method = RequestMethod.POST)
    public BaseResp findByUserId(@RequestBody Map map) {
        return userService.findByUserId((Integer) map.get("userId"));
    }

    //注销
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public BaseResp logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST)
    public BaseResp uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        BaseResp resultResp = uploadFile.uploadFile(file);
        BaseResp resultResp1 = userService.uploadAvatar(request, resultResp.getData().toString());
        resultResp1.setData(resultResp.getData());
        return resultResp1;
    }
    @RequestMapping(value = "/findUserByCookie", method = RequestMethod.GET)
    public BaseResp findUserByCookie(HttpServletRequest request) {
        return userService.findUserByCookie(request);
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public BaseResp updateUser(@RequestBody TbUser tbUser, HttpServletRequest request) {
        return userService.updateUser(tbUser, request);
    }
}
