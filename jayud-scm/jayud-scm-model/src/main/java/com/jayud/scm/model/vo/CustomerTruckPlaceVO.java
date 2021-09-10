package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运输公司车牌信息
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerTruckPlaceVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "运输公司ID")
    private Integer customerId;

    @ApiModelProperty(value = "运输公司名称")
    private String customerName;

    @ApiModelProperty(value = "车次类型")
    private String truckType;

    @ApiModelProperty(value = "大陆车牌")
    private String cnTruckNo;

    @ApiModelProperty(value = "香港车牌")
    private String hkTruckNo;

    @ApiModelProperty(value = "香港备案号")
    private String hkNo;

    @ApiModelProperty(value = "海关备案号")
    private String regNo;

    @ApiModelProperty(value = "司机")
    private String driverName;

    @ApiModelProperty(value = "司机电话")
    private String driverTel;

    @ApiModelProperty(value = "证件号码")
    private String idCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "司机集合")
    private List<CustomerTruckDriverVO> customerTruckDriverVOS;

}
