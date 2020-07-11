package com.cxy.service;

import com.cxy.model.entity.User;
import com.cxy.model.entity.UserDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/15 20:00
 * Modified By:
 */
@Service
public interface UserService {

    User getUserByUserName(String userName);

    User getUserByOpenId(String openId);

    Map<String, Object> getRolesAndPermissionsByUserName(String userName);

    String createRandomToken(String textStr);

    boolean checkRandomToken(String sToken, String textStr);

    void addTokenToRedis(String userName, String jwtTokenStr);

    boolean removeJWTToken(String userName);

    List<User> listOnLineUser();

    User getUser(HttpServletRequest request);
}
