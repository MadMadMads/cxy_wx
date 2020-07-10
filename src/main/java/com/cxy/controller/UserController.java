package com.cxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.cxy.common.enums.ResultStatus;
import com.cxy.common.resultbean.ResultMsg;
import com.cxy.model.entity.User;
import com.cxy.service.UserService;
import com.cxy.util.JWTUtil;
import com.cxy.util.SignUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/17 20:18
 * Modified By:
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    DefaultKaptcha producer;
    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public ResultMsg<String> ajaxLogin(@RequestBody User userInfo, HttpServletResponse response) {
        User vo = userService.getUserByUserName(userInfo.getUserName());
        if (null != vo && vo.getPassword().equals(userInfo.getPassword())) {
            String tokenStr = JWTUtil.sign(userInfo.getUserName(), userInfo.getPassword());
            userService.addTokenToRedis(userInfo.getUserName(), tokenStr);
            response.setHeader("Authorization", tokenStr);
        } else {
            return ResultMsg.error(ResultStatus.PASSWORD_ERROR);
        }
        return ResultMsg.build();
    }

    /**
     * 微信匿名登录
     *
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/wxUserLogin", method = RequestMethod.POST)
    public String userLogin(@RequestBody JSONObject userInfo, HttpServletResponse response) {
        User user = userService.getUserByOpenId(userInfo.getString("openid"));
        if (user == null) {
            return "redirect:" + userInfo.getString("page") + ".html";
        } else {
            String tokenStr = JWTUtil.sign(user.getOpenid(), "");
            userService.addTokenToRedis(user.getOpenid(), tokenStr);
            response.setHeader("Authorization", tokenStr);
            return "redirect:mulu.html";
        }
    }

    @GetMapping(value = "/wxValidate")
    public void wxValidate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.info("signature[{}], timestamp[{}], nonce[{}], echostr[{}]", signature, timestamp, nonce, echostr);
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            response.getOutputStream().println(echostr);
        }
    }

    /**
     * 退出
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/logout")
    public ResultMsg<String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jwtToken = request.getHeader("Authorization");
        userService.removeJWTToken(jwtToken);
        response.setHeader("Authorization", null);
        return ResultMsg.build();
    }

    /**
     * 生成验证码
     *
     * @return
     */
    @RequestMapping("/captcha")
    @ResponseBody
    public ResultMsg<Map<String, Object>> captcha() throws IOException {
        ResultMsg result = ResultMsg.build();
        try {
            // 生成文字验证码
            String text = producer.createText();
            // 生成图片验证码
            ByteArrayOutputStream outputStream = null;
            BufferedImage image = producer.createImage(text);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            Map<String, Object> map = new HashMap<>();
            //保存到redis
            String temp = userService.createRandomToken(text);
            map.put("token", temp);
            map.put("img", encoder.encode(outputStream.toByteArray()));
            result.setData(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/listOnLine")
    @ResponseBody
    public ResultMsg<List<User>> listOnLine() throws IOException {
        ResultMsg<List<User>> result = ResultMsg.build();
        try {
            List<User> vo = userService.listOnLineUser();
            result.setData(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
