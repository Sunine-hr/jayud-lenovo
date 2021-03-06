package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 添加移库信息对象
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Data
public class RelocationRecordForm {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "移库订单号")
    private String orderNo;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "货架名称")
    private String shelvesName;

    @ApiModelProperty(value = "旧库位")
    private String oldLocationCode;

    @ApiModelProperty(value = "新库位")
    private String newLocationCode;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "移库商品件数")
    private Integer number;

    @ApiModelProperty(value = "0为无效，1为有效")
    private Integer status;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "入库批次号")
    private String warehousingBatchNo;


}
