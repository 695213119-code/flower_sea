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
     * 用户的枚举类
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

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        UserEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }
    }


}