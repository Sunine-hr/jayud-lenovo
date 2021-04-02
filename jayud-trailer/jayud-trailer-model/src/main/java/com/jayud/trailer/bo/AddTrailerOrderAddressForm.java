package com.jayud.trailer.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单地址表
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@Data
public class AddTrailerOrderAddressForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务主键(根据类型选择对应表的主键) 前端不用管")
    private Long businessId;

    @ApiModelProperty(value = "类型(0:发货,1:收货,2:通知)")
    private Integer type;

    @ApiModelProperty(value = "绑定商品id")
    private Long bindGoodsId;

    @ApiModelProperty(value = "业务类型(0:空运)")
    private Integer businessType;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "公司")
    private String company;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    private String mailbox;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "附件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "附件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "附件")
    private List<FileView> takeFiles = new ArrayList<>();

    @ApiModelProperty(value = "交货日期(提货日期/送货日期)")
    private String deliveryDate;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


//    public void checkCreateTrailerOrder() {
//        if (StringUtils.isEmpty(this.contacts)){
//            throw new JayudBizException(400,"联系人不能为空");
//        }
//        if (StringUtils.isEmpty(this.phone)){
//            throw new JayudBizException(400,"联系电话不能为空");
//        }
//        if (StringUtils.isEmpty(this.address)){
//            throw new JayudBizException(400,"地址不能空");
//        }
//        if (StringUtils.isEmpty(this.deliveryDate)){
//            throw new JayudBizException(400,"提货日期不能为空");
//        }
//
////        if (this.type == null || StringUtils.isEmpty(this.address)
////                || StringUtils.isEmpty(this.company)) {
////            return false;
////        }
////        return true;
//    }

    public void checkPickUpInfo() {
        if (StringUtils.isEmpty(this.contacts)) {
            throw new JayudBizException(400, "请输入联系人");
        }
        if (StringUtils.isEmpty(this.phone)) {
            throw new JayudBizException(400, "请输入联系电话");
        }
        if (StringUtils.isEmpty(this.address)) {
            throw new JayudBizException(400, "请输入地址");
        }
        if (StringUtils.isEmpty(this.deliveryDate)) {
            throw new JayudBizException(400, "请输入提货日期");
        }
        if (StringUtils.isEmpty(this.name)) {
            throw new JayudBizException(400, "请输入货物描述");
        }
        if (this.bulkCargoAmount == null) {
            throw new JayudBizException(400, "请输入件数");
        }
        if (StringUtils.isEmpty(this.bulkCargoUnit)) {
            throw new JayudBizException(400, "请输入单位");
        }
        if (this.totalWeight == null) {
            throw new JayudBizException(400, "请输入重量");
        }
    }


    @ApiModelProperty(value = "货品名称")
    private String name;

    @ApiModelProperty(value = "散货件数")
    private Integer bulkCargoAmount;

    @ApiModelProperty(value = "散货单位")
    private String bulkCargoUnit;

    @ApiModelProperty(value = "尺寸(长宽高)")
    private String size;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "体积")
    private Double volume;


    public boolean checkCreateGood() {
        if (StringUtils.isEmpty(this.name)
                || this.bulkCargoAmount == null || StringUtils.isEmpty(this.bulkCargoUnit)
                || this.totalWeight == null) {
            return false;
        }
        return true;
    }

}
