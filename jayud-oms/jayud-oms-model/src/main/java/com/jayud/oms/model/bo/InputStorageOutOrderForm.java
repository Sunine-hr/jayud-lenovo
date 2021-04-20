package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 出库订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InputStorageOutOrderForm {


    @ApiModelProperty(value = "主键id")
    private Long  id;

    @ApiModelProperty(value = "入库订单号")
    private String orderNo;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态()")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    @ApiModelProperty(value = "车牌信息")
    private String plateInformation;

    @ApiModelProperty(value = "出仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "入库商品对象集合")
    private List<AddWarehouseGoodsForm> goodsFormList;

    /**
     * 校验创建入库子订单参数
     */
    public String checkCreateOrder() {
        //拖车
        if (this.legalEntityId == null ){
            return "操作主体不为空";
        }
        if (StringUtils.isEmpty(this.unitCode)) {
            return "结算单位不为空";
        }
        if(this.departmentId == null){
            return "操作部门不为空";
        }
        if(StringUtils.isEmpty(this.warehouseNumber)){
            return "入仓号不为空";
        }

        return "pass";
    }

}
