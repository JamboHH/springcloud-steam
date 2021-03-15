package com.ah.controller;

import com.ah.common.BaseResp;
import com.ah.pojo.Comment;
import com.ah.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public BaseResp addComment(@RequestBody Comment comment, HttpServletRequest request) {
        return commentService.addComment(comment, request);
    }

    @RequestMapping(value = "/findAllComment", method = RequestMethod.GET)
    public BaseResp findAllComment(@RequestParam("gameId") Integer gameId) {
        return commentService.findCommentByGameId(gameId);
    }

    @RequestMapping(value = "/delComment", method = RequestMethod.POST)
    public BaseResp delComment(@RequestBody Map map, HttpServletRequest request) {
        return commentService.delComment((Integer) map.get("userId"), (Integer) map.get("cId"), request);
    }

    @RequestMapping(value = "/likeCount", method = RequestMethod.GET)
    private BaseResp CommentlikeCount(@RequestParam("cId") Integer cId, @RequestParam("likeCount") Integer likeCount, HttpServletRequest request) {
        System.out.println("cId = " + cId);
        System.out.println("likeCount = " + likeCount);
        return commentService.CommentlikeCount(cId, likeCount, request);
    }

    @RequestMapping(value = "/findHotComment", method = RequestMethod.GET)
    public BaseResp findHotComment(@RequestParam("gameId") Integer gameId) {
        return commentService.findHotComment(gameId);
    }
}
