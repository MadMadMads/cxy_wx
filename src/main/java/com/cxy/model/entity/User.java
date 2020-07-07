package com.cxy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


/**
 * @author: Luo
 * @description:
 * @time: 2020/6/15 20:04
 * Modified By:
 */
@Data
@TableName(value = "sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String userName;
    private String password;
    private String realName;
    private Integer status;
    private Integer cid;
    private String cname;
    private String phone;
    private String email;
    private String qq;

    private String openid;
    private String regTime;
    private String headimgurl;
    private String nickName;
    private Long createTime;
    @TableField(exist = false)
    private List<Role> roles;
}
