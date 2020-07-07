package com.cxy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cxy.model.entity.User;

/**
 * @author: Luo
 * @description:
 * @time: 2020/6/28 14:38
 * Modified By:
 */
public interface UserMapper extends BaseMapper<User> {
    public User listRolesAndPermissions(String userName);
}
