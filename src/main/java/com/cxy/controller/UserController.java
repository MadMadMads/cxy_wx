package com.cxy.controller;

import com.cxy.common.enums.ResultStatus;
import com.cxy.common.resultbean.ResultMsg;
import com.cxy.model.entity.User;
import com.cxy.service.UserService;
import com.cxy.util.JWTUtil;
import com.cxy.util.RedisUtils;
import com.cxy.util.SignUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
@AllArgsConstructor
@CrossOrigin()
public class UserController {

    @Autowired
    DefaultKaptcha producer;
    @Autowired
    private UserService userService;
    private final WxMpService wxMpService;


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

    @GetMapping("/welcome/{page}")
    public String welcome(@PathVariable("page") String page,HttpServletRequest request,HttpServletResponse response) throws IOException {
       String url = "http://i8gs8h.natappfree.cc/wxUserLogin";
       String redirectURL = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, page);
       log.info("【微信网页授权】获取code,redirectURL={}", redirectURL);
       return "redirect:" + redirectURL;
    }

    /**
     * 微信匿名登录
     *
     * @param
     * @return
     */
    @GetMapping(value = "/wxUserLogin")
    public String userLogin(@RequestParam("code") String code,
                            @RequestParam("state") String returnUrl,HttpServletResponse response) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = (WxMpOAuth2AccessToken)RedisUtils.get("code");
        if (wxMpOAuth2AccessToken == null) {
            try {
                //  拿token
                wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
                RedisUtils.set("code",wxMpOAuth2AccessToken,6);
                // 3.进一步获取用户信息
                String openId = wxMpOAuth2AccessToken.getOpenId();
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        // 拿到用户的基本信息
        WxMpUser wxMpUser=null;
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        User user = userService.getUserByOpenId(wxMpUser.getOpenId());
        if (user == null) {
            return "redirect:" + returnUrl + ".html";
        } else {
            String tokenStr = JWTUtil.sign(user.getOpenid(), user.getOpenid());
            userService.addTokenToRedis(user.getOpenid(), tokenStr);
            response.setHeader("Authorization", tokenStr);
            return "redirect:index.html";
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
