package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
public class InputTrailerOrderFrom {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拖车订单编号")
    private String orderNo;

    @ApiModelProperty(value = "旧拖车订单编号")
    private String oldOrderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "旧主订单编号")
    private String oldMainOrderNo;

    @ApiModelProperty(value = "状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3拖车提柜,TT_4拖车到仓,TT_5拖车离仓,TT_6拖车过磅,TT_7确认还柜)")
    private String status;

    @ApiModelProperty(value = "流程状态(0:进行中,1:完成,2:草稿,3.关闭)")
    private Integer processStatus;

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "接单法人名称")
    private String legalName;

    @ApiModelProperty(value = "进出口类型(1：进口，2：出口)")
    private Integer impAndExpType;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "结算单位姓名")
    private String unitCodeName;

    @ApiModelProperty(value = "起运港/目的港代码")
    private String portCode;

    @ApiModelProperty(value = "车型尺寸id")
    private Long cabinetSize;

    @ApiModelProperty(value = "提运单")
    private String billOfLading;

    @ApiModelProperty(value = "提运单上传附件地址数组集合")
    private List<FileView> billPics = new ArrayList<>();

    @ApiModelProperty(value = "提运单附件路径")
    private String bolFilePath;

    @ApiModelProperty(value = "提运单附件名称")
    private String bolFileName;

    @ApiModelProperty(value = "封条")
    private String paperStripSeal;

    @ApiModelProperty(value = "封条上传附件地址数组集合")
    private List<FileView> pssPics = new ArrayList<>();

    @ApiModelProperty(value = "封条附件路径")
    private String pssFilePath;

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
    private List<AddTrailerOrderAddressForm> orderAddressForms;

    @ApiModelProperty(value = "是否资料齐全")
    private Boolean isInfoComplete = true;

    @ApiModelProperty(value = "操作部门id")
    private Long departmentId;

    /**
     * 校验创建拖车子订单参数
     */
    public boolean checkCreateOrder() {
        //拖车
        if (this.legalEntityId == null || StringUtils.isEmpty(this.unitCode)
                || this.impAndExpType == null || this.cabinetSize == null
                || StringUtils.isEmpty(this.portCode)) {
            return false;
        }
        if (this.impAndExpType == 1) {
            //提运单
            if (StringUtils.isEmpty(this.billOfLading)) {
                log.warn("提运单不能为空");
                return false;
            }
            //柜号
            if (StringUtils.isEmpty(this.cabinetNumber)) {
                log.warn("柜号不能为空");
                return false;
            }
        } else {
            if (StringUtils.isEmpty(this.so)) {
                log.warn("SO不能为空");
                return false;
            }
        }

        // 发货/收货地址是必填项
        if (isInfoComplete) { //资料齐全,需要校验资料
            if (CollectionUtils.isEmpty(this.orderAddressForms)) {
                log.warn("提货地址信息不能为空");
                return false;
            }
            for (AddTrailerOrderAddressForm orderAddressForm : this.orderAddressForms) {
                if (!orderAddressForm.checkCreateTrailerOrder()) {
                    return false;
                }
                if (!orderAddressForm.checkCreateGood()) {
                    return false;
                }
            }
        }
        //货品信息
//        for (AddGoodsForm goodsForm : goodsForms) {
//            if (!goodsForm.checkCreateAirOrder()) {
//                return false;
//            }
//        }

        return true;
    }
}
