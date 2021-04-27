package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * larry 20210427
 * 清关对应箱号信息
 */
@Data
public class ClearanceInfoCaseForm extends BasePageForm{
    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提单对应清关信息id(bill_clearance_info id)")
    private Integer bid;

    @ApiModelProperty(value = "提单对应清关信息(bill_clearance_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private String billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "箱号id(order_case id)")
    private Integer caseId;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private Integer cartonNo;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
