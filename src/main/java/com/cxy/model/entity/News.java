package com.cxy.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;

/**
 *
 *@author: Luo
 *@description: 
 *@time: 2020/7/2 22:10
 *Modified By:
 * 
 */
@Data
@TableName(value = "wx_news")
public class News {
    /**
     * 编号
     */
    @TableId(value = "nid", type = IdType.AUTO)
    private Integer nid;

    /**
     * 所属系统
     */
    @TableField(value = "sys_id")
    private String sysId;

    /**
     * 状态:0 未读 1 已读
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 类型 0 咨询建议表 5 需求反馈表
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 附标题
     */
    @TableField(value = "subtitle")
    private String subtitle;

    /**
     * 联系人
     */
    @TableField(value = "author")
    @NotEmpty(message = "联系人不能为空")
    private String author;

    /**
     * 联系人电话
     */
    @TableField(value = "tel")
    @NotEmpty(message = "电话不能为空")
    private String tel;

    /**
     * 公司编号
     */
    @TableField(value = "cid")
    private String cid;

    /**
     * 公司名称
     */
    @TableField(value = "cidname")
    private String cidname;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 公司地址
     */
    @TableField(value = "cidaddress")
    private String cidaddress;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 职务
     */
    @TableField(value = "job")
    private String job;

    /**
     * 单位简介
     */
    @TableField(value = "cidmemo")
    private String cidmemo;

    /**
     * 技术需求描述
     */
    @TableField(value = "need")
    private String need;


    @TableField(value = "needname")
    private String needname;
    /**
     * 技术需求描述领域
     */
    @TableField(value = "need_id")
    private String needId;


    /**
     * 拟投入资金额
     */
    @TableField(value = "money")
    private Double money;

    /**
     * 希望的合作
     */
    @TableField(value = "hope")
    private String hope;

    /**
     * 方式
     */
    @TableField(value = "mode")
    private String mode;

    /**
     * 创建者
     */
    @TableField(value = "_uid")
    private Integer uid;

    /**
     * 创建者名称
     */
    @TableField(value = "_uidname")
    private String uidname;

    /**
     * 创建时间
     */
    @TableField(value = "_time")
    private Long time;

    public static final String COL_NID = "nid";

    public static final String COL_SYS_ID = "sys_id";

    public static final String COL_STATUS = "status";

    public static final String COL_TYPE = "type";

    public static final String COL_TITLE = "title";

    public static final String COL_SUBTITLE = "subtitle";

    public static final String COL_AUTHOR = "author";

    public static final String COL_TEL = "tel";

    public static final String COL_CID = "cid";

    public static final String COL_CIDNAME = "cidname";

    public static final String COL_CONTENT = "content";

    public static final String COL_CIDADDRESS = "cidaddress";

    public static final String COL_EMAIL = "email";

    public static final String COL_JOB = "job";

    public static final String COL_CIDMEMO = "cidmemo";

    public static final String COL_NEED = "need";

    public static final String COL_NEED_ID = "need_id";

    public static final String COL_MONEY = "money";

    public static final String COL_HOPE = "hope";

    public static final String COL_MODE = "mode";

    public static final String COL__UID = "_uid";

    public static final String COL__UIDNAME = "_uidname";

    public static final String COL__TIME = "_time";
}