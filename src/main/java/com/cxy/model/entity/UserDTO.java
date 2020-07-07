package com.cxy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Luo
 * @description:
 * @time: 2020/7/7 19:52
 * Modified By:
 */
@Data
@AllArgsConstructor
public class UserDTO {
    private Integer uid;
    private String nickName;
    private String url;
}
