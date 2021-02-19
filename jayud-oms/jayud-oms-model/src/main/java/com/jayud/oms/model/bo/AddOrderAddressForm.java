package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
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
public class AddOrderAddressForm {

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
    private LocalDateTime deliveryDate;


    public boolean checkCreateAirOrder() {
        if (this.type == null || StringUtils.isEmpty(this.address)
                || StringUtils.isEmpty(this.company)) {
            return false;
        }
        return true;
    }

}
