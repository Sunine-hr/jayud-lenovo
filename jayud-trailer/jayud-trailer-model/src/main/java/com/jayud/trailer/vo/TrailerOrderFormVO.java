package com.jayud.trailer.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.enums.IsEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 海运订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@Slf4j
public class TrailerOrderFormVO extends Model<TrailerOrderFormVO> {

    private static final long serialVersionUID=1L;

//    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long orderId;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    //进出口类型(1：进口，2：出口)
    //@ApiModelProperty(value = "进出口类型")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型")
    private String impAndExpTypeDesc;

    //@ApiModelProperty(value = "起运港/目的港代码")
    private String portCode;

    @ApiModelProperty(value = "起运港/目的港")
    private String portCodeName;

    //@ApiModelProperty(value = "车型尺寸id")
    private Long cabinetSize;

    @ApiModelProperty(value = "车型尺寸")
    private String cabinetSizeName;

    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    //@ApiModelProperty(value = "状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3派车审核,TT_4拖车提柜,TT_5拖车到仓,TT_6拖车离仓,TT_7拖车过磅,TT_8确认还柜)")
    private String status;

    //流程状态(0:进行中,1:完成,2:草稿,3.关闭)
    //@ApiModelProperty(value = "流程状态")
    private Integer processStatus;

    @ApiModelProperty(value = "状态")
    private String processStatusDesc;

    //@ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单法人名称")
    private String subLegalName;

    //@ApiModelProperty(value = "结算单位code")
    //@ApiModelProperty(value = "结算单位")
    private String unitCode;

    @ApiModelProperty(value = "结算单位")
    private String unitCodeName;

    @ApiModelProperty(value = "提运单")
    private String billOfLading;

    @ApiModelProperty(value = "提运单上传附件地址数组集合")
    private List<FileView> billPics = new ArrayList<>();

    //@ApiModelProperty(value = "提运单附件路径，前台忽略")
    private String bolFilePath;

    //@ApiModelProperty(value = "提运单附件名称，前台忽略")
    private String bolFileName;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    //@ApiModelProperty(value = "封条附件路径")
    private String pssFilePath;

    //@ApiModelProperty(value = "封条上传附件地址数组集合")
    private List<FileView> pssPics = new ArrayList<>();

    //@ApiModelProperty(value = "封条附件名称")
    private String pssFileName;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    //@ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cnPics = new ArrayList<>();

    //@ApiModelProperty(value = "柜号附件路径")
    private String cnFilePath;

    //@ApiModelProperty(value = "柜号附件名称")
    private String cnFileName;

    @ApiModelProperty(value = "SO")
    private String so;

    //@ApiModelProperty(value = "SO上传附件地址数组集合")
    private List<FileView> soPics = new ArrayList<>();

    //@ApiModelProperty(value = "SO附件路径")
    private String soFilePath;

    //@ApiModelProperty(value = "SO附件名称")
    private String soFileName;

    @ApiModelProperty(value = "到港时间")
    private String arrivalTime;

    @ApiModelProperty(value = "截仓期时间")
    private String closingWarehouseTime;

    @ApiModelProperty(value = "截柜租时间")
    private String timeCounterRent;

    @ApiModelProperty(value = "开仓时间")
    private String openTime;

    @ApiModelProperty(value = "截补料时间")
    private String cuttingReplenishingTime;

    @ApiModelProperty(value = "截关时间")
    private String closingTime;

    @ApiModelProperty(value = "放行时间")
    private String releaseTime;

    //@ApiModelProperty(value = "是否过磅(1代表true,0代表false)")
    private Boolean isWeighed;

    @ApiModelProperty(value = "是否过磅")
    private String isWeighedName;

    //@ApiModelProperty(value = "是否做补料(1代表true,0代表false)")
    private Boolean isMakeUp;

    @ApiModelProperty(value = "是否做补料")
    private String isMakeUpName;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimeStr;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    //@ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    //@ApiModelProperty(value = "拖车订单地址信息")
    private List<TrailerOrderAddressVO> orderAddressForms;

//    @ApiModelProperty(value = "货品信息")
//    private List<GoodsVO> goodsForms;

    //@ApiModelProperty(value = "派车信息")
    private TrailerDispatchVO trailerDispatchVO = new TrailerDispatchVO();

   // @ApiModelProperty(value = "附件信息集合")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "订单流程状态")
    private String statusDesc;

    //@ApiModelProperty(value = "客户代码")
    private String customerCode;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    //@ApiModelProperty(value = "是否录用费用")
    private Boolean cost;

    //@ApiModelProperty(value = "供应商id")
    //private Long supplierId;

    //@ApiModelProperty(value = "供应商代码")
    private String defaultSupplierCode;

    //@ApiModelProperty(value = "费用录用默认结算单位")
    private String defaultUnitCode;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    //@ApiModelProperty(value = "对应业务类型")
    private String bizCode;

    //@ApiModelProperty(value = "订单类别")
    private String classCode;

    //@ApiModelProperty(value = "主订单id")
    private String mainOrderId;

    @ApiModelProperty(value = "货物重量")
    private Double totalWeight = 0.0;

    //@ApiModelProperty(value = "流程描述")
    private String processDescription;

    /**
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {

            sb.append(goods.getName())
                    .append(" ").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append("板")
                    .append(",").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                    .append(",").append("重量:").append(goods.getTotalWeight()).append("KG")
                    .append(";");
            if (goods.getTotalWeight() != null) {
                this.totalWeight = this.totalWeight + goods.getTotalWeight();
            }
        }

        this.goodsInfo = sb.toString();
    }

    /**
     * @param mainOrderObjs 远程客户对象集合
     */
    public void assemblyMainOrderData(Object mainOrderObjs) {
        if (mainOrderObjs == null) {
            return;
        }
        JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderObjs));
        for (int i = 0; i < mainOrders.size(); i++) {
            JSONObject json = mainOrders.getJSONObject(i);
            if (this.mainOrderNo.equals(json.getStr("orderNo"))) { //主订单配对
                this.customerName = json.getStr("customerName");
                this.customerCode = json.getStr("customerCode");
                this.mainOrderId = json.getStr("id");
                this.bizUname = json.getStr("bizUname");
                this.bizCode = json.getStr("bizCode");
                this.classCode = json.getStr("classCode");
                break;
            }
        }

    }


    /**
     * 组装供应商数据
     */
//    public void assemblySupplierInfo(JSONArray supplierInfo) {
//        if (supplierInfo == null) {
//            return;
//        }
//        for (int i = 0; i < supplierInfo.size(); i++) {
//            JSONObject json = supplierInfo.getJSONObject(i);
//            if (this.trailerDispatchVO.getSupplierId() != null && this.trailerDispatchVO.getSupplierId().equals(json.getLong("id"))) { //供应商配对
//                this.defaultSupplierCode = json.getStr("supplierCode");
//                break;
//            }
//        }
//    }

    /**
     * 组装结算单位数据
     */
    public void assemblyUnitCodeInfo(ApiResult unitCodeInfo) {
        if (unitCodeInfo == null) {
            return;
        }
        if (unitCodeInfo.getCode() != HttpStatus.SC_OK) {
            log.warn("请求结算单位信息失败");
            return;
        }
        JSONArray unitCodes = new JSONArray(unitCodeInfo.getData());
        for (int i = 0; i < unitCodes.size(); i++) {
            JSONObject json = unitCodes.getJSONObject(i);
            if (this.unitCode.equals(json.getStr("idCode"))) { //结算单位配对
                this.unitCodeName = json.getStr("name");
                break;
            }
        }
    }

    public void assemblyCabinetSize(ApiResult cabinetSizeInfo) {
        if (cabinetSizeInfo == null) {
            return;
        }
        if (cabinetSizeInfo.getCode() != HttpStatus.SC_OK) {
            log.warn("请求结算单位信息失败");
            return;
        }
        JSONArray cabinetSizeInfos = new JSONArray(cabinetSizeInfo.getData());
        for (int i = 0; i < cabinetSizeInfos.size(); i++) {
            JSONObject json = cabinetSizeInfos.getJSONObject(i);
            if (this.cabinetSize.equals(json.getLong("id"))) { //结算单位配对
                this.cabinetSizeName = json.getStr("name");
                break;
            }
        }
    }


    /**
     * 组装法人主体
     *
     * @param legalEntityResult
     */
    public void assemblyLegalEntity(ApiResult legalEntityResult) {
        if (legalEntityResult == null) {
            return;
        }
        if (legalEntityResult.getCode() != HttpStatus.SC_OK) {
            log.warn("请求法人主体信息失败");
            return;
        }
        JSONArray legalEntitys = new JSONArray(legalEntityResult.getData());
        for (int i = 0; i < legalEntitys.size(); i++) {
            JSONObject json = legalEntitys.getJSONObject(i);
            if (this.legalEntityId.equals(json.getLong("id"))) { //法人主体配对
                this.subLegalName = json.getStr("legalName");
                break;
            }
        }
    }


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
        this.defaultUnitCode = unitCode;
    }

    public void setIsWeighed(Boolean isWeighed) {
        this.isWeighed = isWeighed;
        this.isWeighedName = IsEnum.getDesc(isWeighed);
    }

    public void setIsMakeUp(Boolean isMakeUp) {
        this.isMakeUp = isMakeUp;
        this.isMakeUpName = IsEnum.getDesc(isMakeUp);
    }

    public void getFile(String path){
        this.soPics = StringUtils.getFileViews(this.getSoFilePath(),this.getSoFileName(),path);
        this.billPics = StringUtils.getFileViews(this.getBolFilePath(),this.getBolFileName(),path);
        this.cnPics = StringUtils.getFileViews(this.getCnFilePath(),this.getCnFileName(),path);
        this.pssPics = StringUtils.getFileViews(this.getPssFilePath(),this.getPssFileName(),path);
    }
}
