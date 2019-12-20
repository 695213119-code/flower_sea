package com.flower.sea.entityservice.user;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flower_sea_user")
@ApiModel(value = "用户表")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "用户手机号/账号")
    private String phone;

    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户角色(角色表主键id)")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty(value = "用户账号状态 1-正常 2-禁用")
    private Integer status;

    @ApiModelProperty(value = "创建者")
    @TableField("create_by")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    @TableField("update_by")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "删除状态 1-已删除 2-未删除")
    @TableField("is_deleted")
    private Integer isDeleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    /**
     * 用户的业务枚举类
     */
    public enum UserEnum {

        /**
         * 用户被禁用
         */
        USER_DISABLE(2, "该用户已经被禁用"),

        /**
         * 用户未绑定手机号/未在数据中进行关联
         */
        USER_DOES_NOT_BIND_THE_PHONE(1000, "用户未绑定手机号!");

        private Integer code;

        private String message;

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        UserEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }


    /**
     * 用户登录策略key的枚举类
     */
    public enum userLoginStrategyKeyEnum {

        /**
         * 登录平台-pc
         */
        LOGIN_PLATFORM_PC("login_platform_pc", "登录平台-pc"),

        /**
         * 登录平台-app
         */
        LOGIN_PLATFORM_APP("login_platform_app", "登录平台-app"),

        /**
         * 登录平台-小程序
         */
        LOGIN_PLATFORM_SMALL_PROGRAM("login_platform_small_program", "登录平台-小程序"),

        /**
         * 登录方式-账号加密码
         */
        LOGIN_TYPE_ACCOUNT_AND_PASSWORD("login_type_account_and_password", "登录方式-账号加密码"),

        /**
         * 登录方式-微信的openId
         */
        LOGIN_TYPE_WE_CHAT_OPEN_ID("login_type_we_chat_open_id", "登录方式-微信的openId"),

        /**
         * 登录方式-手机短信验证码
         */
        LOGIN_TYPE_PHONE_SHORT_CODE("login_type_phone_short_code", "登录方式-手机短信验证码");

        private String key;

        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        userLoginStrategyKeyEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }


}