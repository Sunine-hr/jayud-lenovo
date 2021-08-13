package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 担保合同
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerGuaranteeVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "提保类型")
    private String guaranteeType;

    @ApiModelProperty(value = "提保类型")
    private String guaranteeTypeName;

    @ApiModelProperty(value = "担保协议名称")
    private String guaranteeName;

    @ApiModelProperty(value = "提保协议编号")
    private String guaranteeNo;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "提保主体（人/公司)")
    private String contractName;

    @ApiModelProperty(value = "证件号码")
    private Integer cardNo;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginDate;

    @ApiModelProperty(value = "结束日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "是否有房产复制件")
    private Integer isHourse;

    @ApiModelProperty(value = "担保金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "担保财产市值")
    private BigDecimal amt;

    @ApiModelProperty(value = "是否归档")
    private String isFile;

    @ApiModelProperty(value = "归档日期")
    private LocalDateTime fileDate;

    @ApiModelProperty(value = "归档人")
    private String fileUserName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

}
