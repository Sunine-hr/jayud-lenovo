package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ReceiptNotice 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ReceiptNotice对象", description = "收货通知单")
public class QueryReceiptNoticeForm extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    @NotNull(message = "请选择仓库")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库名称")
    @NotBlank(message = "请选择仓库")
    private String warehouse;

    @ApiModelProperty(value = "货主id")
    @NotNull(message = "请选择货主")
    private Long owerId;

    @ApiModelProperty(value = "货主名称")
    @NotBlank(message = "请选择货主")
    private String ower;

    @ApiModelProperty(value = "收货通知单号")
    @NotBlank(message = "请填写收货通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "单据类型(存中文值)")
    @NotBlank(message = "请选择单据类型")
    private String documentType;

    @ApiModelProperty(value = "单据类型编号(字典)")
    @NotBlank(message = "请选择单据类型")
    private String documentTypeCode;

    @ApiModelProperty(value = "订单来源值(1:手工创建,,2:MES下发,3:ERP下发 <字典>)")
    private Integer orderSourceCode;

    @ApiModelProperty(value = "订单来源(字典)")
    private String orderSource;


    @ApiModelProperty(value = "供应商id(基础客户表)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商(基础客户表)")
    private String supplier;


    @ApiModelProperty(value = "确认人")
    private String confirmedBy;

    @ApiModelProperty(value = "订单状态")
    private Integer status;



    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "预到货时间")
    private List<String> arrivalTime;

    public void setOperationTime(List<String> arrivalTime) {
        if (CollectionUtils.isNotEmpty(arrivalTime) && arrivalTime.size() > 1) {
            arrivalTime.set(1, DateUtils.strMaximumTime(arrivalTime.get(1)));
        }
        this.arrivalTime = arrivalTime;
    }
    //货主id
    private List<String> owerIdList;

    //仓库id
    private List<String> warehouseIdList;

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


//
//    public void setCreationTime(List<String> creationTime) {
//        this.creationTime = creationTime;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        String format = sdf.format(date);
//        // 只传第一个 计算设置时间到 当前时间
//        if(creationTime.get(0)!=null&&creationTime.get(1)==null){
//            this.creationTimeOne = creationTime.get(0);
////            if(creationTime.get(1)!=null){
////                this.creationTimeTwo = creationTime.get(1);
////            }else {
//            this.creationTimeTwo = format ;
////            }
//        }
//        // 只传第二个 计算 到第二个时间之前的所有的
//        if(creationTime.get(1)!=null&&creationTime.get(0)==null){
//            this.creationTimeOne = CrmDictCode.CRM_FILE_TYPE;
//            this.creationTimeTwo = creationTime.get(1);
//        }
//
//        if(creationTime.get(0)!=null&&creationTime.get(1)!=null){
//            this.creationTimeOne = creationTime.get(0);
//            this.creationTimeTwo = creationTime.get(1);
//        }
//    }
}
