package com.flower.sea.entityservice.auth;

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
 * api表
 * </p>
 *
 * @author zhangLei
 * @since 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flower_sea_api")
@ApiModel(value = "api表")
public class Api extends Model<Api> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "菜单id(menu表主键id)")
    @TableField("menu_id")
    private Long menuId;

    @ApiModelProperty(value = "方法路径")
    private String url;

    @ApiModelProperty(value = "请求类型")
    private String method;

    @ApiModelProperty(value = "状态 1-正常 2-禁用")
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

}