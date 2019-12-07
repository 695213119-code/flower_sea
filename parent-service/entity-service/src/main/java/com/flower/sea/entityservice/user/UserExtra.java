package com.flower.sea.entityservice.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
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
 * 用户额外信息表
 * </p>
 *
 * @author zhangLei
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flower_sea_user_extra")
@ApiModel(value = "用户额外信息表")
public class UserExtra extends Model<UserExtra> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "用户id(用户表表主键id)")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "用户昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @ApiModelProperty(value = "用户性别 1-男 2-女 0-未知")
    private Integer gender;

    @ApiModelProperty(value = "用户生日(农历)")
    private LocalDateTime birth;

    @ApiModelProperty(value = "用户下一次的生日时间(阳历)")
    @TableField("birth_another")
    private LocalDateTime birthAnother;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

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