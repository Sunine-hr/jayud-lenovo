package com.jayud.oms.order.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 入库订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StorageInputOrderVO extends Model<StorageInputOrderVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态()")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    @NotNull(message = "结算单位不为空")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    @NotNull(message = "操作主体不为空")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    @NotNull(message = "操作部门不为空")
    private Long departmentId;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "入仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "备注")
    private String remarks;

//    @ApiModelProperty(value = "入库商品对象集合")
//    private List<AddWarehouseGoodsForm> goodsFormList;

//    /**
//     * 校验创建出库子订单参数
//     */
//    public String checkCreateOrder() {
//        //拖车
//        if (this.legalEntityId == null ){
//            return "操作主体不为空";
//        }
//        if (StringUtils.isEmpty(this.unitCode)) {
//            return "结算单位不为空";
//        }
//        if(this.departmentId == null){
//            return "操作部门不为空";
//        }
//        if(StringUtils.isEmpty(this.warehouseNumber)){
//            return "入仓号不为空";
//        }
//
//        return "pass";
//    }

}
