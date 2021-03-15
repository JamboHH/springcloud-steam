package com.ah.dao;

import com.ah.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 添加评论
    int addComment(Comment comment);

    // 查找一个游戏的全部评论
    List<Comment> findCommentByGameId(@Param("gameId") Integer gameId);

    // 删除用户自己的评论
    int delComment(@Param("userId") Integer userId, @Param("cId") Integer cId);

    //  评论点赞数增加
    int CommentlikeCount(@Param("cId") Integer cId, @Param("likeCount") Integer likeCount);

    //  查找一个游戏的点赞数最高的评论
    List<Comment> findHotComment(@Param("gameId") Integer gameId);
}
