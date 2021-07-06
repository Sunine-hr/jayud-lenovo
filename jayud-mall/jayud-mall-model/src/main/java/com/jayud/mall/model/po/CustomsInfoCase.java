package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报关文件箱号
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomsInfoCase对象", description="报关文件箱号")
public class CustomsInfoCase extends Model<CustomsInfoCase> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提单对应报关信息id(bill_customs_info id)")
    private Long bId;

    @ApiModelProperty(value = "提单对应报关信息id(bill_customs_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "箱号id(order_case id)")
    private Long caseId;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private String cartonNo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
