package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class InputStorageInputOrderVO extends Model<InputStorageInputOrderVO> {

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

    @ApiModelProperty(value = "流程状态名")
    private String processStatusDesc;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

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

    @ApiModelProperty(value = "操作部门")
    private String department;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "入仓号")
    private String warehouseNumber;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "入库商品对象集合")
    private List<WarehouseGoodsVO> goodsFormList;

    @ApiModelProperty(value = "附件集合")
    private List<FileView> allPics;

    @ApiModelProperty(value = "接单人")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receivingOrdersDate;

    @ApiModelProperty(value = "总件数")
    private String totalNumber;

    @ApiModelProperty(value = "总重量")
    private String totalWeight;

    @ApiModelProperty(value = "入库实际商品")
    private List<InGoodsOperationRecordVO> inGoodsOperationRecords;

    @ApiModelProperty(value = "状态()")
    private String statusName;

    @ApiModelProperty(value = "费用状态", required = true)
    private String costDesc;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;

    /**
     * 校验创建出库子订单参数
     */
    public String checkCreateOrder() {
        //入库
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

    public void setUnitCodeName(String unitCodeName) {
        this.unitCodeName=unitCodeName;
    }

    public void copyOperationInfo() {
        this.id = null;
        this.allPics = new ArrayList<>();
        this.orderNo = null;
        this.mainOrderNo = null;
        this.status = null;
        this.processStatus = null;
        this.orderTaker = null;
        this.createTime = null;
        this.receivingOrdersDate = null;
        this.createUser = null;
        this.inGoodsOperationRecords = new ArrayList<>();
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(goodsFormList)) {
            goodsFormList.forEach(e -> {
                e.setId(null);
                e.setOrderId(null);
                e.setOrderNo(null);
                e.setTakeFiles(null);
                e.setTakeFiles(null);
            });
        }
    }
}
