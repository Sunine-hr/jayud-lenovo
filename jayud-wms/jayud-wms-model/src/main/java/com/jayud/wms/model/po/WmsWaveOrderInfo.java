package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.wms.model.vo.WmsOutboundOrderInfoVO;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * WmsWaveOrderInfo 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="波次单对象", description="波次单")
public class WmsWaveOrderInfo extends SysBaseEntity {


    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主编码")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "状态(1 新建,2 处理中,3 已分配,4 缺货中,5 已撤销,6已出库)")
    private Integer status;

    @ApiModelProperty(value = "订单数")
    private Integer allOrderAccount;

    @ApiModelProperty(value = "完成订单数")
    private Integer finishOrderAccount;

    @ApiModelProperty(value = "分配量")
    private BigDecimal allocationAccount;

    @ApiModelProperty(value = "拣货量")
    private BigDecimal packingAccount;

    @ApiModelProperty(value = "订单物料总量")
    private BigDecimal allMaterialAccount;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "波次编码集合")
    private List<String> waveOrderNumberList;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态文本")
    private String status_text;

    @TableField(exist = false)
    @ApiModelProperty(value = "出库单数据")
    private List<WmsOutboundOrderInfoVO> orderInfoList;

    @TableField(exist = false)
    private List<String> orgIds;

    @TableField(exist = false)
    private Boolean isCharge;

    @TableField(exist = false)
    private List<String> owerIdList;

    @TableField(exist = false)
    private List<String> warehouseIdList;





}
