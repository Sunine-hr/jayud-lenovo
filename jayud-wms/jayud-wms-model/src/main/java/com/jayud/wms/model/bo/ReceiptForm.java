package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.exception.ServiceException;
import com.jayud.wms.model.enums.MaterialStatusEnum;
import com.jayud.wms.model.enums.ReceiptStatusEnum;
import com.jayud.wms.model.po.Container;
import com.jayud.wms.model.po.WmsReceiptAppend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Receipt 实体类
 *
 * @author jyd
 * @since 2021-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "收货单对象", description = "收货单")
public class ReceiptForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    private String ower;

    @ApiModelProperty(value = "收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "收货单号")
    private String receiptNum;

    @ApiModelProperty(value = "供应商id(基础客户表)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商(基础客户表)")
    private String supplier;

    @ApiModelProperty(value = "订单来源值(1:手工创建,,2:MES下发,3:ERP下发 <字典>)")
    private Integer orderSourceCode;

    @ApiModelProperty(value = "订单来源(字典)")
    private String orderSource;

    @ApiModelProperty(value = "计划收货时间（通知单带过来）")
    private LocalDate plannedReceivingTime;

    @ApiModelProperty(value = "预期数量")
    private Double totalNum;

    @ApiModelProperty(value = "预期重量")
    private Double totalWeight;

    @ApiModelProperty(value = "预期体积")
    private Double totalVolume;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "实收重量")
    private Double actualWeight;

    @ApiModelProperty(value = "实收体积")
    private Double actualVolume;

    @ApiModelProperty(value = "收货人")
    private String receiver;

    @ApiModelProperty(value = "订单状态（1：待收货：2：部分收货，3：完全收货，4：整单撤销）")
    private Integer status;

    @ApiModelProperty(value = "收货时间")
    private LocalDate receivingTime;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "是否直接上架")
    private Boolean isPutShelf;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "流程标志")
    private String processFlag;

    @ApiModelProperty(value = "质检单号")
    private String qcNo;

    @ApiModelProperty(value = "物料信息")
    private List<MaterialForm> materialForms;

    @ApiModelProperty(value = "物料sn信息")
    private List<MaterialSnForm> materialSnForms;

    @ApiModelProperty(value = "收货单附加服务表对象")
    private List<WmsReceiptAppendForm> wmsReceiptAppendForms;


    @ApiModelProperty(value = "主订单号")
    private String mainOrderNumber;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "车牌")
    private String carBarnd;

    @ApiModelProperty(value = "车型")
    private String carModel;

    @ApiModelProperty(value = "司机")
    private String carDriver;

    @ApiModelProperty(value = "联系方式")
    private String carRelation;

    @ApiModelProperty(value = "客户code")
    private String clientCode;

    @ApiModelProperty(value = "单据类型")
    private String orderType;


    public void checkParam() {

        this.materialForms.forEach(e -> {
            if (!MaterialStatusEnum.FOUR.getCode().equals(e.getStatus())) {
                e.checkParam();
            }
        });

        //校验同组物料
        Map<String, Long> materialMap = materialForms.stream().filter(e -> !MaterialStatusEnum.FOUR.getCode().equals(e.getStatus())).collect(Collectors.groupingBy(e -> this.receiptNoticeNum + e.getContainerNum(), Collectors.counting()));
        materialMap.forEach((k, v) -> {
            if (v > 1) {
                throw new ServiceException("相同物料不能放在同一个容器");
            }
        });
    }

    public void checkOverchargePolicy(Map<String, BigDecimal> overchargePolicyMap) {
        Map<String, List<MaterialForm>> map = this.materialForms.stream().filter(e -> !MaterialStatusEnum.FOUR.getCode().equals(e.getStatus())).collect(Collectors.groupingBy(e -> e.getMaterialCode()));
        map.forEach((k, v) -> {
            BigDecimal overchargeRatio = overchargePolicyMap.get(k);
            MaterialForm materialForm = v.get(0);
            BigDecimal num = materialForm.getNum() == null ? new BigDecimal(0) : BigDecimal.valueOf(materialForm.getNum());
            BigDecimal total = new BigDecimal(0);
            for (MaterialForm tmp : v) {
                if (tmp.getActualNum() != null) {
                    total = total.add(BigDecimal.valueOf(tmp.getActualNum()));
                }
            }
            BigDecimal tmpNum = num;
            if (overchargeRatio != null) {
                BigDecimal overchargedNum = overchargeRatio.divide(new BigDecimal(100)).add(new BigDecimal(1)).multiply(num);
                tmpNum = overchargedNum;
                if (total.compareTo(overchargedNum) > 0) {
                    throw new ServiceException(materialForm.getMaterialName() + "超过超收比例");
                }

            }
            for (MaterialForm form : v) {
                if (overchargeRatio == null && tmpNum.compareTo(new BigDecimal(0)) > 0 && total.compareTo(tmpNum) > 0) {
                    throw new ServiceException(materialForm.getMaterialName() + "实收超过预期数量");
                }
                form.setTmpNum(tmpNum).setTmpActualNum(total);
            }

        });
    }

    public void calculationStatus() {
        if (this.actualNum.compareTo(this.totalNum) == 0) {
            this.status = ReceiptStatusEnum.THREE.getCode();
        }
        if (this.actualNum.compareTo(this.totalNum) < 0) {
            this.status = ReceiptStatusEnum.TWO.getCode();
        }
        if (this.actualNum.compareTo(0.0) == 0) {
            this.status = ReceiptStatusEnum.ONE.getCode();
        }
    }

    /**
     * 计算合计数量
     */
    public void calculateTotalQuantity() {
        Double totalNum = 0.0;
        Double totalWeight = 0.0;
        Double totalVolume = 0.0;
        Double actualNum = 0.0;
        Double actualWeight = 0.0;
        Double actualVolume = 0.0;
        for (MaterialForm materialForm : this.materialForms) {
            if (MaterialStatusEnum.FOUR.getCode().equals(materialForm.getStatus())){ continue;}
            totalNum += materialForm.getNum();
            totalWeight += materialForm.getNum() * materialForm.getWeight().doubleValue();
            totalVolume += materialForm.getNum() * materialForm.getVolume().doubleValue();
            actualNum += materialForm.getActualNum();
            actualWeight += materialForm.getActualNum() * materialForm.getWeight().doubleValue();
            actualVolume += materialForm.getActualNum() * materialForm.getVolume().doubleValue();
        }
        this.totalNum = totalNum;
        this.totalWeight = totalWeight;
        this.totalVolume = totalVolume;
        this.actualNum = actualNum;
        this.actualWeight = actualWeight;
        this.actualVolume = actualVolume;
    }

    public void checkContainer(Map<String, Container> containerMap) {
        for (MaterialForm materialForm : materialForms) {
            if (!MaterialStatusEnum.FOUR.getCode().equals(materialForm.getStatus()) && containerMap.get(materialForm.getContainerNum()) == null) {
                throw new ServiceException("容器号:" + materialForm.getContainerNum() + "不存在");
            }
        }
    }
}
