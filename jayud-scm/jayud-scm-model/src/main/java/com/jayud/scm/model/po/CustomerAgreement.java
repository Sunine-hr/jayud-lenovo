package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户协议表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerAgreement对象", description="客户协议表")
public class CustomerAgreement extends Model<CustomerAgreement> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
      private Integer id;

    @ApiModelProperty(value = "客户签约主体")
    private String signCustomerName;

    @ApiModelProperty(value = "我司签约主体")
    private String signCompanyName;

    @ApiModelProperty(value = "是否顺延，0顺延，1不顺延")
    private Integer isExtended;

    @ApiModelProperty(value = "我司原件份数")
    private Integer isNum;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "协议类型")
    private String treatyType;

    @ApiModelProperty(value = "协议编号")
    private String treatyNo;

    @ApiModelProperty(value = "协议名称")
    private String treatyName;

    @ApiModelProperty(value = "协议开始时间")
    private LocalDateTime treatyBeginDtm;

    @ApiModelProperty(value = "协议结束时间")
    private LocalDateTime treatyEndDtm;

    @ApiModelProperty(value = "协议有效期限(月)")
    private String treatyDate;

    @ApiModelProperty(value = "协议版本(我司版本、客户版本)")
    private String treatyCopyRight;

    @ApiModelProperty(value = "协议说明")
    private String treatyNotes;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否默认，1默认，0不默认")
    private Integer isDefaultValue;

    @ApiModelProperty(value = "是否归档")
    private Integer isFiles;

    @ApiModelProperty(value = "归档人")
    private String filesUser;

    @ApiModelProperty(value = "归档时间")
    private LocalDateTime filesDtm;

    @ApiModelProperty(value = "归档号")
    private String filesNum;

    @ApiModelProperty(value = "是否原件回来")
    private Integer isYuanJian;

    @ApiModelProperty(value = "原件归档时间")
    private LocalDateTime yuanJianDtm;

    @ApiModelProperty(value = "审核级别")
    private String fLevel;

    @ApiModelProperty(value = "审核步长")
    private String fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    private Integer crtBy;

    private String crtByName;

    private LocalDateTime crtDtm;

    private Integer mdyBy;

    private String mdyByName;

    private LocalDateTime mdyDtm;

    private Integer voidedBy;

    private String voidedByName;

    private LocalDateTime voidedDtm;

    private Integer voided;

    @ApiModelProperty(value = "父协议ID")
    private Integer parentId;

    @ApiModelProperty(value = "争议解决方式和地点")
    private String dispute;

    @ApiModelProperty(value = "是否垫资")
    private Integer isPayIn;

    @ApiModelProperty(value = "收货人姓名")
    private String receivername;

    @ApiModelProperty(value = "收货人联系方式")
    private String receivertel;

    @ApiModelProperty(value = "收货人有效证件号码")
    private String receivercode;

    @ApiModelProperty(value = "签约主体通信地址")
    private String mailingAddress;

    @ApiModelProperty(value = "erp是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "业务类型")
    private Integer modelType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
