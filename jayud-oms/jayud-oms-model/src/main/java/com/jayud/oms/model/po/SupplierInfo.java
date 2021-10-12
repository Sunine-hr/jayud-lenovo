package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 供应商信息
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplierInfo对象", description="供应商信息")
public class SupplierInfo extends Model<SupplierInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "产品分类id集合（多个用逗号隔开）")
    private String productClassifyIds;

    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    @ApiModelProperty(value = "供应商名称(英)")
    private String supplierEnName;

    @ApiModelProperty(value = "国家代码")
    private String stateCode;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系手机")
    private String contactPhone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "省id")
    private Integer pid;

    @ApiModelProperty(value = "省/州名")
    private String pname;

    @ApiModelProperty(value = "城市id")
    private Integer cid;

    @ApiModelProperty(value = "城市名")
    private String cname;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "供外使用的认证信息ID")
    private Long extSettingId;

    @ApiModelProperty(value = "结算类型")
    private String settlementType;

    @ApiModelProperty(value = "账期")
    private String paymentDay;

    @ApiModelProperty(value = "税票种类型")
    private String taxReceipt;

    @ApiModelProperty(value = "税率")
    private String rate;

    @ApiModelProperty(value = "采购员id")
    private Long buyerId;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "国家征信")
    private Integer nationalCredit;

    @ApiModelProperty(value = "海关征信")
    private Integer customsCredit;

    @ApiModelProperty(value = "海关信用等级(0:一般认证企业,1:一般信用企业,2:高级信用企业,3:失信企业)")
    private Integer customsCreditRating;

    @ApiModelProperty(value = "是否高级认证")
    private Boolean isAdvancedCertification;

    @ApiModelProperty(value = "文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "对接GPS所需要的key值")
    private String appKey;

    @ApiModelProperty(value = "对接GPS公用路径前缀")
    private String gpsAddress;

    @ApiModelProperty(value = "gps厂商(1:云港通,2:北斗)")
    private Integer gpsType;

    @ApiModelProperty(value = "gps请求参数(json格式)")
    private String gpsReqParam;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
