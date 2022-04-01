package com.jayud.oms.order.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 创建订单，快进快出表单
 * </p>
 *
 * @author LLJ
 * @since 2021-06-10
 */
@Data
public class InputStorageOrderForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工作单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "操作类型(1.入库，2.出库)")
    private Integer operationType;

    @ApiModelProperty(value = "单据类型(1.常规，2.快进快出)")
    private Integer documentType;

    @ApiModelProperty(value = "流程状态(1:暂存,2:正常,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    @NotNull(message = "结算单位不为空")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    @NotNull(message = "操作主体不为空")
    private Long legalId;

    @ApiModelProperty(value = "操作部门id")
    @NotNull(message = "操作部门id不为空")
    private Long departId;

    @ApiModelProperty(value = "操作部门")
    @NotNull(message = "操作部门不为空")
    private String departName;

    @ApiModelProperty(value = "外部单号")
    private String externalOrderNo;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "车型")
    private String model;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "联系方式")
    private String contactInformation;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "预计到货时间")
    private String estimatedArrivalTime;

    @ApiModelProperty(value = "商品对象集合")
    private List<AddWarehouseGoodsForm> fastGoodsFormList;
//
//    @ApiModelProperty(value = "入库商品对象集合")
//    private List<AddWarehouseGoodsForm> inGoodsFormList;
//
//    @ApiModelProperty(value = "出库商品对象集合")
//    private List<AddWarehouseGoodsForm> outGoodsFormList;


}
