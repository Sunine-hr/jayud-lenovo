package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * larry 20210427
 * 清关信息参数信息
 */
@Data
public class BillClearanceInfoForm extends BasePageForm{
    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提单id(ocean_bill id)")
    private Integer billId;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "模版文件地址(附件)")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describe;

    @ApiModelProperty(value = "总箱数")
    private Integer cartons;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
