package com.jayud.oms.model.vo.template.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.oms.model.vo.InputGoodsVO;
import com.jayud.oms.model.vo.TrailerDispatchVO;
import com.jayud.oms.model.vo.TrailerOrderAddressVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

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
@Accessors(chain = true)
@Slf4j
public class TrailerOrderTemplate extends BaseOrderTemplate {


    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "进出口类型", required = true)
    private String impAndExpTypeDesc;

    @ApiModelProperty(value = "车型尺寸", required = true)
    private String cabinetSizeName;

    @ApiModelProperty(value = "状态", required = true)
    private String statusDesc;

    @ApiModelProperty(value = "操作主体", required = true)
    private String legalName;

    @ApiModelProperty(value = "结算单位", required = true)
    private String unitCodeName;

    @ApiModelProperty(value = "起运港/目的港", required = true)
    private String portCodeName;

    @ApiModelProperty(value = "SO", required = true)
    private String so;

    @ApiModelProperty(value = "提运单", required = true)
    private String billOfLading;

    @ApiModelProperty(value = "柜号", required = true)
    private String cabinetNumber;

    @ApiModelProperty(value = "封条", required = true)
    private String paperStripSeal;

//    @ApiModelProperty(value = "费用状态", required = true)
//    private String costDesc;

    @ApiModelProperty(value = "是否待补全")
    private Boolean isInfoComplete;

    @ApiModelProperty(value = "费用状态")
    private Boolean cost;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3派车审核,TT_4拖车提柜,TT_5拖车到仓,TT_6拖车离仓,TT_7拖车过磅,TT_8确认还柜)")
    private String status;

//    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
//    private Integer processStatus;

//    @ApiModelProperty(value = "流程状态名")
//    private String processStatusDesc;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;


    @ApiModelProperty(value = "结算单位code")
    private String unitCode;


//    @ApiModelProperty(value = "起运港/目的港代码")
//    private String portCode;


//    @ApiModelProperty(value = "提运单上传附件地址数组集合")
//    private List<FileView> billPics = new ArrayList<>();


//    @ApiModelProperty(value = "封条附件路径")
//    private String pssFilePath;
//
//    @ApiModelProperty(value = "封条附件名称")
//    private String pssFileName;

//    @ApiModelProperty(value = "柜号附件路径")
//    private String cnFilePath;
//
//    @ApiModelProperty(value = "柜号附件名称")
//    private String cnFileName;


//    @ApiModelProperty(value = "SO附件路径")
//    private String soFilePath;

//    @ApiModelProperty(value = "SO附件名称")
//    private String soFileName;

//    @ApiModelProperty(value = "到港时间")
//    private String arrivalTime;

//    @ApiModelProperty(value = "截仓期时间")
//    private String closingWarehouseTime;
//
//    @ApiModelProperty(value = "截柜租时间")
//    private String timeCounterRent;
//
//    @ApiModelProperty(value = "开仓时间")
//    private String openTime;
//
//    @ApiModelProperty(value = "截补料时间")
//    private String cuttingReplenishingTime;

//    @ApiModelProperty(value = "截关时间")
//    private String closingTime;
//
//    @ApiModelProperty(value = "放行时间")
//    private String releaseTime;


//    @ApiModelProperty(value = "创建人(登录用户)")
//    private String createUser;
//
//    @ApiModelProperty(value = "创建时间")
//    private String createTime;


//    @ApiModelProperty(value = "接单人(登录用户名)")
//    private String orderTaker;
//
//    @ApiModelProperty(value = "接单日期")
//    private String receivingOrdersDate;

//    @ApiModelProperty(value = "海运订单地址信息")
//    private List<TrailerOrderAddressVO> orderAddressForms;
//
//    @ApiModelProperty(value = "货品信息")
//    private List<InputGoodsVO> goodsForms;
//
//    @ApiModelProperty(value = "派车信息")
//    private TrailerDispatchVO trailerDispatchVO;
//
//    @ApiModelProperty(value = "附件信息集合")
//    private List<FileView> allPics;
//
//    @ApiModelProperty(value = "总重量")
//    private String totalWeightName;
//
//    @ApiModelProperty(value = "总件数")
//    private String totalAmountName;
//
//    @ApiModelProperty(value = "总箱数")
//    private String totalXAmountName;

    @ApiModelProperty(value = "客户code")
    private String customerCode;


    public void setStatus(String status) {
        this.status = status;
        this.statusDesc = OrderStatusEnum.getDesc(status);
    }

//    public void setCost(Boolean cost) {
//        this.cost = cost;
//        if (cost){
//            this.costDesc="是";
//        }else {
//            this.costDesc="否";
//        }
//    }
}
