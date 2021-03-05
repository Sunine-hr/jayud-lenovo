package com.jayud.trailer.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@Slf4j
public class AddTrailerDispatchFrom {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "拖车订单id")
    private Long orderId;

    @ApiModelProperty(value = "拖车订单号")
    private String trailerOrderNo;

    @ApiModelProperty(value = "派车单号")
    private String orderNo;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "司机电话")
    private String phone;

    @ApiModelProperty(value = "司机姓名")
    private String name;

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名字(supplier_info name)")
    private String supplierName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "过磅重量")
    private Double weighing;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    public boolean checkDispatchOptParam() {
        String title = "派车";
        if (this.orderNo == null) {
            log.warn(title + " 派车单号必填");
            return false;
        }
        if (this.plateNumber == null) {
            log.warn(title + " 大陆车牌必填");
            return false;
        }
        if (this.supplierId == null) {
            log.warn(title + " 车辆供应商必填");
            return false;
        }
        if (this.phone == null) {
            log.warn(title + " 司机号码必填");
            return false;
        }
        if (this.name == null) {
            log.warn(title + " 司机姓名必填");
            return false;
        }
        return true;
    }

    public boolean checkWeightOptParam() {
        String title = "拖车过磅";
        if (this.weighing == null) {
            log.warn(title + " 过磅重量必填");
            return false;
        }
        return true;
    }
}
