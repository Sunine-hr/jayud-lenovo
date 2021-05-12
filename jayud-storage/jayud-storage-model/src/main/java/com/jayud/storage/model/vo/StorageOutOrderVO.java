package com.jayud.storage.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
public class StorageOutOrderVO {


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
    @NotNull(message = "结算单位不为空")
    private String unitCode;

    @ApiModelProperty(value = "流程状态名")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "接单法人id")
    @NotNull(message = "操作主体不为空")
    private Long legalEntityId;

    @ApiModelProperty(value = "操作部门id")
    @NotNull(message = "操作部门不为空")
    private Long departmentId;

    @ApiModelProperty(value = "操作部门")
    private String departmentName;

    @ApiModelProperty(value = "车牌信息")
    private String plateInformation;

    @ApiModelProperty(value = "出仓号")
    @NotNull(message = "出仓号不为空")
    private String warehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "入库商品对象集合")
    private List<WarehouseGoodsVO> goodsFormList;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> allPics;

    @ApiModelProperty(value = "接单人")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "总件数")
    private String totalNumber;

    @ApiModelProperty(value = "总重量")
    private String totalWeight;

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName=unitCodeName;
    }
}
