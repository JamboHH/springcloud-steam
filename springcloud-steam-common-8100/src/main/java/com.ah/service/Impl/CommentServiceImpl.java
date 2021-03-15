package com.ah.service.Impl;

import com.ah.client.UserClient;
import com.ah.common.BaseResp;
import com.ah.dao.CommentMapper;
import com.ah.pojo.Comment;
import com.ah.pojo.TbUser;
import com.ah.pojo.req.CommentResp;
import com.ah.service.CommentService;
import com.ah.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserClient userClient;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public BaseResp addComment(Comment comment, HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String token = "";
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "token":
                    token = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        Object o = redisUtils.get(token);
        if (o != null) {
            comment.setCreateTime(new Date());
            comment.setLikeCount(0);
            int i = commentMapper.addComment(comment);
            if (i == 1) {
                resultResp.setCode(200);
                resultResp.setMessage("评论成功");
                return resultResp;
            } else {
                resultResp.setCode(201);
                resultResp.setMessage("评论发生错误");
                return resultResp;
            }
        }
        resultResp.setCode(204);
        resultResp.setMessage("请先登录!");
        return resultResp;
    }

    @Override
    public BaseResp findCommentByGameId(Integer gameId) {
        BaseResp resultResp = new BaseResp();
        ArrayList<CommentResp> commentResps = new ArrayList<>();
        List<Comment> commentByGameId = commentMapper.findCommentByGameId(gameId);
        if (commentByGameId != null) {
            for (Comment comment : commentByGameId) {
                CommentResp commentResp = new CommentResp();
                BeanUtils.copyProperties(comment, commentResp);

                Integer userId = comment.getUserId();

                //调用远程User服务的方法
                Map map = new HashMap();
                map.put("userId", userId);
                BaseResp byUserId1 = userClient.findByUserId(map);
                TbUser byUserId = JSONObject.parseObject(JSONObject.toJSON(byUserId1.getData()).toString(), TbUser.class);


                String userName = byUserId.getUserName();
                String pic = byUserId.getPic();

                commentResp.setUserName(userName);
                commentResp.setPic(pic);
                commentResps.add(commentResp);
            }
            if (commentResps != null) {
                resultResp.setCode(200);
                //一个游戏的全部评论集合
                resultResp.setData(commentResps);
                resultResp.setMessage("这个游戏的全部评论");
                return resultResp;
            }
        }
        resultResp.setCode(201);
        resultResp.setMessage("这个游戏目前还没有评论");
        return resultResp;
    }

    @Override
    public BaseResp delComment(Integer userId, Integer cId, HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String token = "";
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "token":
                    token = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        Object o = redisUtils.get(token);
        if (o != null) {
            Object o1 = JSONObject.toJSON(o);
            TbUser user = JSONObject.parseObject(o1.toString(), TbUser.class);
            if (user.getId() == userId) {
                int i = commentMapper.delComment(userId, cId);
                if (i == 1) {
                    resultResp.setCode(200);
                    resultResp.setMessage("删除评论成功");
                    return resultResp;
                } else {
                    resultResp.setCode(201);
                    resultResp.setMessage("删除失败，服务器出错");
                    return resultResp;
                }
            } else {
                resultResp.setCode(203);
                resultResp.setMessage("不能删除别人的评论!");
                return resultResp;
            }
        }
        resultResp.setCode(204);
        resultResp.setMessage("请先登录!");
        return resultResp;
    }

    @Override
    public BaseResp CommentlikeCount(Integer cId, Integer likeCount, HttpServletRequest request) {
        BaseResp resultResp = new BaseResp();
        Cookie[] cookies = request.getCookies();
        String token = "";
        for (Cookie cookie : cookies) {
            switch (cookie.getName()) {
                case "token":
                    token = cookie.getValue();
                    break;
                default:
                    break;
            }
        }
        Object o = redisUtils.get(token);
        if (o != null) {
            Integer count = likeCount + 1;
            int i = commentMapper.CommentlikeCount(cId, count);
            if (i == 1) {
                resultResp.setCode(200);
                resultResp.setMessage("点赞成功");
                return resultResp;
            }
            resultResp.setCode(201);
            resultResp.setMessage("点赞失败");
            return resultResp;
        }
        resultResp.setCode(204);
        resultResp.setMessage("请先登录!");
        return resultResp;
    }

    @Override
    public BaseResp findHotComment(Integer gameId) {
        BaseResp resultResp = new BaseResp();
        List<Comment> hotComment = commentMapper.findHotComment(gameId);
        if (hotComment != null) {
            CommentResp commentResp = new CommentResp();

            Comment comment = hotComment.get(0);
            BeanUtils.copyProperties(comment, commentResp);

            Integer userId = comment.getUserId();
            //调用远程User服务的方法
            Map map = new HashMap();
            map.put("userId", userId);
            BaseResp byUserId1 = userClient.findByUserId(map);
            TbUser byUserId = JSONObject.parseObject(JSONObject.toJSON(byUserId1.getData()).toString(), TbUser.class);

            String pic = byUserId.getPic();
            String userName = byUserId.getUserName();

            commentResp.setPic(pic);
            commentResp.setUserName(userName);

            resultResp.setCode(200);
            resultResp.setMessage("这个游戏的最新的最热评论");
            resultResp.setData(commentResp);
            return resultResp;
        } else {
            resultResp.setCode(201);
            resultResp.setMessage("这个游戏还没有评论");
            return resultResp;
        }
    }
}
