package com.cxy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxy.dao.UserMapper;
import com.cxy.model.entity.News;
import com.cxy.model.entity.User;
import com.cxy.service.CooperationService;
import com.cxy.service.UserService;
import com.cxy.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShirostudyApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Autowired
    CooperationService cooperationService;
    @Test
    void t1() {
        System.out.println(cooperationService.findListNews(new News()));
    }
}
