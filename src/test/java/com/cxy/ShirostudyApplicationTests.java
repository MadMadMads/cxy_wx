package com.cxy;


import com.cxy.service.CooperationService;
import com.cxy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShirostudyApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    CooperationService cooperationService;
    @Test
    void t1() {
    }
}
