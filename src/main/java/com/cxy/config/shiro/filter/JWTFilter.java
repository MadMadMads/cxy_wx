package com.cxy.config.shiro.filter;

import com.cxy.common.enums.Constant;
import com.cxy.common.enums.ResultStatus;
import com.cxy.config.shiro.JWTToken;
import com.cxy.model.entity.User;
import com.cxy.service.UserService;

import com.cxy.util.JWTUtil;
import com.cxy.util.RedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {

    @Value("${jwt.anonymous.urls}")
    private String anonymousStr;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;


    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String contextPath = WebUtils.getPathWithinApplication(WebUtils.toHttp(servletRequest));
        if (!StringUtils.isEmpty(anonymousStr)) {
            String[] anonUrls = anonymousStr.split(",");
            //匿名可访问的url
            for (int i = 0; i < anonUrls.length; i++) {
                if (contextPath.contains(anonUrls[i])) {
                    return true;
                }
            }
        }
        //获取请求头token
        AuthenticationToken token = this.createToken(servletRequest, servletResponse);
        if (token.getPrincipal() == null) {
            handler401(servletResponse, ResultStatus.USER_NOT_Authorization.getCode(), ResultStatus.USER_NOT_Authorization.getMessage());
            return false;
        } else {
            try {
                this.getSubject(servletRequest, servletResponse).login(token);
                return true;
            } catch (Exception e) {
                String msg = e.getMessage();
                //token错误
                if (msg.contains("incorrect")) {
                    handler401(servletResponse, ResultStatus.USER_NOT_Authorization.getCode(), msg);
                    return false;
                    //token过期
                } else if (msg.contains("expired")) {
                    //尝试刷新token
                    if (this.refreshToken(servletRequest, servletResponse)) {
                        return true;
                    } else {
                        handler401(servletResponse, ResultStatus.USER_NOT_Authorization.getCode(), "token已过期,请重新登录");
                        return false;
                    }
                }
                handler401(servletResponse, ResultStatus.USER_NOT_Authorization.getCode(), msg);
                return false;
            }
        }
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    private boolean refreshToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取header，tokenStr
        String oldToken = request.getHeader("Authorization");
        String userName = JWTUtil.getUsername(oldToken);
        String key = Constant.JWT_TOKEN + userName;
        //获取redis tokenStr
        String redisUserInfo = (String) RedisUtils.get(key);
        if (redisUserInfo != null) {
            if (oldToken.equals(redisUserInfo)) {
                User vo = this.userService.getUserByUserName(userName);
                //重写生成token(刷新)
                String newTokenStr = JWTUtil.sign(vo.getUserName(), vo.getPassword());
                JWTToken jwtToken = new JWTToken(newTokenStr);
                userService.addTokenToRedis(userName, newTokenStr);
                SecurityUtils.getSubject().login(jwtToken);
                response.setHeader("Authorization", newTokenStr);
                return true;
            }
        }
        return false;
    }

    /**
     * token有问题
     *
     * @param response
     * @param code
     * @param msg
     */
    private void handler401(ServletResponse response, int code, String msg) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.OK.value());
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.getWriter().write("{\"code\":" + code + ", \"msg\":\"" + msg + "\"}");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 支持跨域
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization,Origin,X-Requested-With,Content-Type,Accept");
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(httpServletRequest, httpServletResponse);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        return new JWTToken(token);
    }
}
