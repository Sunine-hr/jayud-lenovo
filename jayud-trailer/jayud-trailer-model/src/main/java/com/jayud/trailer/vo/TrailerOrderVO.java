package com.jayud.trailer.vo;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.TradeTypeEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 拖车订单表
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@Slf4j
public class TrailerOrderVO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拖车订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3派车审核,TT_4拖车提柜,TT_5拖车到仓,TT_6拖车离仓,TT_7拖车过磅,TT_8确认还柜)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "流程状态名")
    private String processStatusDesc;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "起运港/目的港代码")
    private String portCode;

    @ApiModelProperty(value = "起运港/目的港")
    private String portCodeName;

    @ApiModelProperty(value = "车型尺寸id")
    private Long cabinetSize;

    @ApiModelProperty(value = "车型尺寸")
    private String cabinetSizeName;

    @ApiModelProperty(value = "提运单")
    private String billOfLading;

    @ApiModelProperty(value = "提运单上传附件地址数组集合")
    private List<FileView> billPics = new ArrayList<>();

    @ApiModelProperty(value = "提运单附件路径，前台忽略")
    private String bolFilePath;

    @ApiModelProperty(value = "提运单附件名称，前台忽略")
    private String bolFileName;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "封条附件路径")
    private String pssFilePath;

    @ApiModelProperty(value = "封条上传附件地址数组集合")
    private List<FileView> pssPics = new ArrayList<>();

    @ApiModelProperty(value = "封条附件名称")
    private String pssFileName;

    @ApiModelProperty(value = "柜号")
    private String cabinetNumber;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cnPics = new ArrayList<>();

    @ApiModelProperty(value = "柜号附件路径")
    private String cnFilePath;

    @ApiModelProperty(value = "柜号附件名称")
    private String cnFileName;

    @ApiModelProperty(value = "SO")
    private String so;

    @ApiModelProperty(value = "SO上传附件地址数组集合")
    private List<FileView> soPics = new ArrayList<>();

    @ApiModelProperty(value = "SO附件路径")
    private String soFilePath;

    @ApiModelProperty(value = "SO附件名称")
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

    @ApiModelProperty(value = "是否过磅(1代表true,0代表false)")
    private Boolean isWeighed;

    @ApiModelProperty(value = "是否做补料(1代表true,0代表false)")
    private Boolean isMakeUp;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "是否需要录入费用(0:false,1:true)")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人(登录用户名)")
    private String orderTaker;

    @ApiModelProperty(value = "接单日期")
    private String receivingOrdersDate;

    @ApiModelProperty(value = "拖车订单地址信息")
    private List<TrailerOrderAddressVO> orderAddressForms;

    @ApiModelProperty(value = "流程描述")
    private String processDescription;

    @ApiModelProperty(value = "货品信息")
    private String goodsInfo;

    @ApiModelProperty(value = "派车信息")
    private TrailerDispatchVO trailerDispatchVO;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight = 0.0;

    @ApiModelProperty(value = "总件数")
    private Integer totalAmount = 0;

    @ApiModelProperty(value = "总箱数")
    private Integer totalXAmount = 0;

    @ApiModelProperty(value = "总重量")
    private String totalWeightName = "";

    @ApiModelProperty(value = "总件数")
    private String totalAmountName = "";

    @ApiModelProperty(value = "总箱数")
    private String totalXAmountName = "";

    @ApiModelProperty(value = "附件信息集合")
    private List<FileView> allPics = new ArrayList<>();

    @ApiModelProperty(value = "是否待补全")
    private Boolean isInfoComplete;

    public void setImpAndExpType(Integer impAndExpType) {
        this.impAndExpType = impAndExpType;
        this.impAndExpTypeDesc = TradeTypeEnum.getDesc(impAndExpType);
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
        this.processStatusDesc = ProcessStatusEnum.getDesc(processStatus);
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
     * 组装商品信息
     */
    public void assemblyGoodsInfo(List<GoodsVO> goodsList) {
        StringBuilder sb = new StringBuilder();

        for (GoodsVO goods : goodsList) {
            if (this.id.equals(goods.getBusinessId())
                    && BusinessTypeEnum.TC.getCode().equals(goods.getBusinessType())) {
                sb.append(goods.getName())
                        .append(" ").append(goods.getPlateAmount() == null ? 0 : goods.getPlateAmount()).append(goods.getPlateUnit())
                        .append(",").append(goods.getBulkCargoAmount()).append(goods.getBulkCargoUnit())
                        .append(",").append("重量:").append(goods.getTotalWeight()).append("KG")
                        .append(";");
            }
            if(goods.getTotalWeight()!=null){
                this.totalWeight = this.totalWeight + goods.getTotalWeight();
                if(goods.getBulkCargoUnit().equals("件")){
                    this.totalAmount = this.totalAmount + goods.getBulkCargoAmount();
                }else{
                    this.totalXAmount = this.totalXAmount + goods.getBulkCargoAmount();
                }
            }
        }
        this.totalWeightName = this.totalWeight.toString() + "KG";
        this.totalAmountName = this.totalAmount.toString() + "件";
        this.totalXAmountName = this.totalXAmount.toString() + "箱";
        this.goodsInfo = sb.toString();
    }

    public void getFile(String path){
        this.soPics = StringUtils.getFileViews(this.getSoFilePath(),this.getSoFileName(),path);
        this.billPics = StringUtils.getFileViews(this.getBolFilePath(),this.getBolFileName(),path);
        this.cnPics = StringUtils.getFileViews(this.getCnFilePath(),this.getCnFileName(),path);
        this.pssPics = StringUtils.getFileViews(this.getPssFilePath(),this.getPssFileName(),path);
    }
}
