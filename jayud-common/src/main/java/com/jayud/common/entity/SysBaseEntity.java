package com.jayud.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ciro
 * @date 2021/12/2 11:29
 * @description: Base基类
 */
@Data
@Accessors(chain = true)
public class SysBaseEntity implements Serializable {
    private static final long serialVersionUID = 2978210586254056099L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    protected Long id;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    protected String createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新人")
    protected String updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "当前页数")
    Integer currentPage = 1;
    @TableField(exist = false)
    @ApiModelProperty(value = "每页记录数")
    Integer pageSize = 10;
    @TableField(exist = false)
    @ApiModelProperty(value = "排序")
    String sorter;

    public void clearUpdate(){
        this.updateBy = null;
        this.updateTime = null;
    }

    public void clearCreate(){
        this.createBy = null;
        this.createTime = null;
    }

}
