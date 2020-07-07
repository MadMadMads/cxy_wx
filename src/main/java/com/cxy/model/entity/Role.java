package com.cxy.model.entity;


import com.cxy.model.entity.Permission;
import lombok.Data;

import java.util.List;
@Data
public class Role{
	private Integer rid;
	private String roleName;
	private String memo;
	private List<Permission> permissions;
}
