package com.jayud.oms.model.bo;

import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Data
@Slf4j
public class InputOrderCustomsForm {

    @ApiModelProperty(value = "主订单号,前台忽略")
    private String mainOrderNo;

    @ApiModelProperty(value = "旧主订单号,前台忽略")
    private String oldMainOrderNo;

    @ApiModelProperty(value = "通关口岸code", required = true)
    @NotEmpty(message = "portCode is required")
    private String portCode;

    @ApiModelProperty(value = "通关口岸", required = true)
    @NotEmpty(message = "portName is required")
    private String portName;

    @ApiModelProperty(value = "货物流向", required = true)
    @NotEmpty(message = "goodsType is required")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号上传附件地址名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "六联单号", required = true)
    private String encode;

    @ApiModelProperty(value = "六联单号附件,前台忽略")
    private String encodePic;

    @ApiModelProperty(value = "六联单号附件名称,前台忽略")
    private String encodePicName;

    @ApiModelProperty(value = "六联单号附件数组集合")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递 5-内陆运输)", required = true)
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件,前台忽略")
    private String airTransportPic;

    @ApiModelProperty(value = "提运单附件名称,前台忽略")
    private String airTransPicName;

    @ApiModelProperty(value = "提运单附件数组集合")
    private List<FileView> airTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件,前台忽略")
    private String seaTransportPic;

    @ApiModelProperty(value = "提运单号附件名称,前台忽略")
    private String seaTransPicName;

    @ApiModelProperty(value = "提运单号附件数组集合")
    private List<FileView> seaTransportPics = new ArrayList<>();

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "接单法人", required = true)
    private String legalName;

    @ApiModelProperty(value = "接单法人ID", required = true)
    private Long legalEntityId;

    @ApiModelProperty(value = "报关类型 CBG-纯报关 CKBG-出口报关,前台忽略")
    private String classCode;

    @ApiModelProperty(value = "子订单", required = true)
    @NotEmpty(message = "subOrders is required")
    private List<InputSubOrderCustomsForm> subOrders = new ArrayList<>();

    @ApiModelProperty(value = "当前登录用户,FeignClient必传,要么就传token,否则跨系统拿不到用户")
    private String loginUser;

    @ApiModelProperty(value = "审核状态")
    private String subCustomsStatus;

    @ApiModelProperty(value = "监管方式")
    private String supervisionMode;

    @ApiModelProperty(value = "订单备注")
    private String orderRemarks;

    @ApiModelProperty(value = "子订单状态")
    private Integer mainOrderStatus;


    public void checkCustomsInfoParam() {
        String title = "报关:";
        StringBuffer sb = new StringBuffer();
        boolean isSuccess = true;
        if (StringUtil.isNullOrEmpty(this.getPortCode())) {
            sb.append("通关口岸code").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getPortName())) {
            sb.append("通关口岸").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (this.getGoodsType() == null) {
            sb.append("货物流向").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getBizModel())) {
            sb.append("业务模式").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (StringUtil.isNullOrEmpty(this.getLegalName())) {
            sb.append("接单法人").append("参数不能为空").append(",");
            isSuccess = false;
        }
        if (this.getLegalEntityId() == null) {
            sb.append("接单法人").append("参数不能为空").append(",");
            isSuccess = false;
        }
        //只用中港才有六联单号
        if ("1".equals(this.bizModel)) {
//            if (StringUtil.isNullOrEmpty(this.getEncode())) {//六联单号
//                sb.append("六联单号").append("参数不能为空").append(",");
//                isSuccess = false;
//            }
            if (!StringUtils.isEmpty(this.getEncode())) {
                //六联单号必须为13位的纯数字
                String encode = this.getEncode();
                if (!(encode.matches("[0-9]{1,}") && encode.length() == 13)) {
                    sb.append("六联单号必须为13位的纯数字").append(",");
                    isSuccess = false;
                }
            }
        }

        if (this.getSubOrders() == null) {
            sb.append("子订单").append("参数不能为空").append(",");
            isSuccess = false;
        } else {
            //校验子订单数据
            if (this.getSubOrders().size() == 0) {
                sb.append("子订单数据为0条").append(",");
                isSuccess = false;
            }
        }
        if (!isSuccess) {
            String msg = title + sb.substring(0, sb.length() - 1);
            log.warn(msg);
            throw new JayudBizException(msg);
        }
        //校验报关子订单参数
        for (InputSubOrderCustomsForm subOrderCustomsForm : subOrders) {
            subOrderCustomsForm.checkSubOrderCustoms();
        }
    }

    public void handleAttachmentInfo() {
        //附件处理
        this.setCntrPic(StringUtils.getFileStr(this.getCntrPics()));
        this.setCntrPicName(StringUtils.getFileNameStr(this.getCntrPics()));
        this.setEncodePic(StringUtils.getFileStr(this.getEncodePics()));
        this.setEncodePicName(StringUtils.getFileNameStr(this.getEncodePics()));
        this.setAirTransportPic(StringUtils.getFileStr(this.getAirTransportPics()));
        this.setAirTransPicName(StringUtils.getFileNameStr(this.getAirTransportPics()));
        this.setSeaTransportPic(StringUtils.getFileStr(this.getAirTransportPics()));
        this.setSeaTransPicName(StringUtils.getFileNameStr(this.getAirTransportPics()));
    }
}
