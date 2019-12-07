package com.flower.sea.entityservice.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户第三方信息表
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flower_sea_user_thirdparty")
@ApiModel(value = "用户第三方信息表")
public class UserThirdparty extends Model<UserThirdparty> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "用户id(用户表主键id)")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "平台类型(1-qq 2-微信)")
    private Integer platform;

    @ApiModelProperty(value = "第三方平台的用户标识(唯一)")
    @TableField("union_id")
    private String unionId;

    @ApiModelProperty(value = "第三方用户性别(0-未知 1-男 2女)")
    private Integer gender;

    @ApiModelProperty(value = "第三方用户昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "第三方用户年龄")
    private Integer age;

    @ApiModelProperty(value = "第三方用户头像")
    private String avatar;

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

}