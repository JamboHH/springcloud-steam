package com.ah.pojo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResp {
    // 评论id
    private Integer cId;
    // 游戏id
    private Integer gameId;
    // 用户id
    private Integer userId;
    // 评论内容
    private String content;
    // 点赞数
    private Integer likeCount;
    // 评论时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //根据用户id查询到的 图片和用户名
    private String pic;
    private String userName;
}
