package com.ah.service;

import com.ah.common.BaseResp;
import com.ah.pojo.TbUser;
import com.ah.pojo.req.TbUserReq;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    BaseResp sendMail(String email);

    BaseResp registry(TbUserReq tbUserReq);

    BaseResp relogin(TbUserReq tbUserReq);

    BaseResp findByuuid(HttpServletRequest request);

    BaseResp findByUserId(Integer userId);

    BaseResp logout(HttpServletRequest request);

    BaseResp uploadAvatar(HttpServletRequest request, String toString);

    BaseResp findUserByCookie(HttpServletRequest request);

    BaseResp updateUser(TbUser tbUser, HttpServletRequest request);
}
