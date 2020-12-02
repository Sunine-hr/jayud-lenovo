package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @ApiModelProperty(value = "业务类型(0:空运)")
    private Integer businessType;

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


    public boolean checkCreateAirOrder() {
        if (this.type == null || StringUtils.isEmpty(this.address)
                || StringUtils.isEmpty(this.phone)) {
            return false;
        }
        return true;
    }

}
