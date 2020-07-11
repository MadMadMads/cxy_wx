package com.cxy.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxy.common.enums.Constant;
import com.cxy.config.webconfig.UserContext;
import com.cxy.dao.UserMapper;
import com.cxy.model.entity.Permission;
import com.cxy.model.entity.Role;
import com.cxy.model.entity.User;
import com.cxy.model.entity.UserDTO;
import com.cxy.service.UserService;
import com.cxy.util.JWTUtil;
import com.cxy.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/15 20:02
 * Modified By:
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${redis.identifyingTokenExpireTime}")
    private long identifyingTokenExpireTime;
    //redis中jwtToken过期时间
    @Value("${redis.refreshJwtTokenExpireTime}")
    private long refreshJwtTokenExpireTime;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByUserName(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByOpenId(String openId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("openid", openId);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) return null;
        else
        return user;
    }

    @Override
    public Map<String, Object> getRolesAndPermissionsByUserName(String userName) {
        Role role = null;
        Permission permission = null;
        Set<String> roles = new HashSet<String>();
        Set<String> permissions = new HashSet<String>();
        Map<String, Object> map = new HashMap<String, Object>();
        User vo = userMapper.listRolesAndPermissions(userName);
        for (int i = 0; i < vo.getRoles().size(); i++) {
            role = vo.getRoles().get(i);
            roles.add(role.getRoleName());
            for (int j = 0; j < role.getPermissions().size(); j++) {
                permission = role.getPermissions().get(i);
                permissions.add(permission.getPermissionName());
            }
        }
        map.put("allRoles", roles);
        map.put("allPermissions", permissions);
        return map;
    }

    @Override
    public String createRandomToken(String textStr) {
        //生成一个token
        String sToken = UUID.randomUUID().toString();
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        redisTemplate.opsForValue().set(Constant.NO_REPEAT_PRE + sToken, textStr, identifyingTokenExpireTime, TimeUnit.MINUTES);
        return sToken;
    }

    @Override
    public boolean checkRandomToken(String sToken, String textStr) {
        Object value = redisTemplate.opsForValue().get(Constant.NO_REPEAT_PRE + sToken);
        if (value != null && textStr.equals(value)) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    public void addTokenToRedis(String userName, String jwtTokenStr) {
        String key = Constant.JWT_TOKEN + userName;
        RedisUtils.set(key, jwtTokenStr, refreshJwtTokenExpireTime);
    }

    @Override
    public boolean removeJWTToken(String userName) {
        String key = Constant.JWT_TOKEN + userName;
        return redisTemplate.delete(key);
    }

    @Override
    public List<User> listOnLineUser() {
        //Set setNames = redisTemplate.keys(Constant.JWT_TOKEN + "*");
        //List list = new ArrayList<>(setNames.size());
        //Iterator<String> iter = setNames.iterator();
        //while (iter.hasNext()) {
        //    String temp = iter.next();
        //    list.add(temp.substring(temp.lastIndexOf("_") + 1));
        //}
        //return userMapper.listUserByNams(list.toArray());
        return null;
    }

    @Override
    public User getUser(HttpServletRequest request) {
        User user = new User();
        String token = JWTUtil.getToken(request);
        if (!StringUtils.isEmpty(token) && JWTUtil.verifyTime(token)) {
            user = getUserByUserName(JWTUtil.getUsername(token));
        }
        if (user.getUid() == null && user.getUserName() == null) {
            user.setUid(0);
            user.setUserName("游客");
        }
        UserContext.setUser(user);
        return user;
    }
}
