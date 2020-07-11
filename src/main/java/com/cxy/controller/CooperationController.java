package com.cxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.cxy.common.resultbean.ResultMsg;
import com.cxy.model.entity.News;
import com.cxy.model.entity.User;
import com.cxy.service.CooperationService;
import com.cxy.service.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/27 19:17
 * Modified By:
 */
@Controller
public class CooperationController {
    @Autowired
    UserService userService;

    @Autowired
    CooperationService cooperationService;

    //@RequiresRoles("管理员")
    @RequestMapping("/feedback")
    @ResponseBody
    public ResultMsg<List<News>> feedback(@RequestBody News news, HttpServletResponse response){
        ResultMsg<List<News>> result = ResultMsg.build();
        result.setData(cooperationService.findListNews(news));
        return result;
    }
    @PostMapping("/createfeedback")
    @ResponseBody
    public ResultMsg<String> createFeedback (@RequestBody JSONObject jsonObject, User user, HttpServletResponse response){
        ResultMsg<String> result = ResultMsg.build();
        if (jsonObject == null) {
            return (ResultMsg<String>)result.withError("表单不存在");
        }
        cooperationService.createNews(jsonObject,user);
        return result;
    }

    //@RequiresRoles("管理员")
    @DeleteMapping("/feedback/{nid}")
    @ResponseBody
    public ResultMsg<String> deleteFeedback (@PathVariable("nid")Integer nid, User user, HttpServletResponse response){
        ResultMsg<String> result = ResultMsg.build();
        if (nid == null) {
            return (ResultMsg<String>)result.withError("请选中删除项");
        }
        cooperationService.deleteNew(nid);
        return result;
    }
}
