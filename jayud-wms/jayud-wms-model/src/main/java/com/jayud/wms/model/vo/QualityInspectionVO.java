package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * QualityInspection 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "质检检测对象", description = "质检检测")
public class QualityInspectionVO extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    private String ower;

    @ApiModelProperty(value = "质检单号")
    @JsonProperty(value = "qcNo")
    private String qcNo;

    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "客户id-供应商id(基础客户表)")
    private Long supplierId;

    @ApiModelProperty(value = "客户名称-供应商(基础客户表)")
    private String supplier;

    @ApiModelProperty(value = "质检部门id")
    private Long qualityInspectionDeptId;

    @ApiModelProperty(value = "质检部门")
    private String qualityInspectionDept;

    @ApiModelProperty(value = "质检人")
    private String qualityInspector;

    @ApiModelProperty(value = "质检时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date qualityInspectionTime;

    @ApiModelProperty(value = "质检状态(1:未质检,2:已质检)")
    private Integer status;

    @ApiModelProperty(value = "质检状态详情")
    private String statusDetails;

    @ApiModelProperty(value = "质检状态文本")
    private String statusDesc;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "是否跳过质检")
    private Boolean isSkip;

    @ApiModelProperty(value = "流程标志")
    private String processFlag;


    @ApiModelProperty(value = "主订单号")
    private String mainOrderNumber;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单据类型")
    private String documentType;

    @ApiModelProperty(value = "订单来源")
    private String orderSource;

    @ApiModelProperty(value = "总数量")
    private Double totalNum;

    @ApiModelProperty(value = "质检人id集合")
    private List<Long> qualityInspectorIds;

    @ApiModelProperty(value = "物料信息")
    List<QualityInspectionMaterialVO> materialForms;

    public void setStatus(Integer status) {
        this.status = status;
        if (this.status == 1){
            this.statusDesc = "未质检";
        }else if (this.status == 2){
            this.statusDesc = "已质检";
        }else if (this.status == 3){
            this.statusDesc = "质检中";
        }
    }

    public List<QualityInspectionMaterialVO> getMaterialForms() {
        return materialForms;
    }

    public void setMaterialForms(List<QualityInspectionMaterialVO> materialForms) {
        this.materialForms = materialForms;
    }
}
