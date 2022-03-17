package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * LockReLocation 实体类
 *
 * @author jyd
 * @since 2022-01-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "推荐库位锁定对象", description = "推荐库位锁定")
@TableName(value = "wms_lock_re_location")
public class LockReLocation {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    protected Long id;

    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库位编号")
    private String code;

    @ApiModelProperty(value = "库位id")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


}
