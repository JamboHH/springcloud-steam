package com.ah.service;


import com.ah.common.BaseResp;
import com.ah.pojo.Comment;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
    // 添加评论
    BaseResp addComment(Comment comment, HttpServletRequest request);

    // 查找一个游戏的全部评论
    BaseResp findCommentByGameId(Integer gameId);

    // 用户删除用户自己的评论
    BaseResp delComment(Integer userId, Integer cId, HttpServletRequest request);

    // 点赞
    BaseResp CommentlikeCount(Integer cId, Integer likeCount, HttpServletRequest request);

    // 查找一个游戏的最热评论
    BaseResp findHotComment(Integer gameId);
}
