package com.jayud.customs.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报关业务订单表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderCustoms对象", description="报关业务订单表")
public class OrderCustoms extends Model<OrderCustoms> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单编号(生成规则product_classify code+随时数)")
    private String orderNo;

    @ApiModelProperty(value = "口岸code(port_info code)")
    private String portCode;

    @ApiModelProperty(value = "口岸名称(port_info name)")
    private String portName;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片")
    private String cntrPic;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关抬头")
    private String title;

    @ApiModelProperty(value = "结算单位(customer_info)")
    private String unitAccount;

    @ApiModelProperty(value = "结算代码(customer_info)")
    private String unitCode;

    @ApiModelProperty(value = "附件(json存储 文件名，url)")
    private String description;

    @ApiModelProperty(value = "委托单号")
    private String entrustNo;

    @ApiModelProperty(value = "编辑委托单号备注")
    private String entrustNote;

    @ApiModelProperty(value = "状态(0-未接单 1-已接单 2-接单中 3-放行通过 4-放行驳回  5-已完成)")
    private Integer status;

    @ApiModelProperty(value = "审核意见")
    private String remarks;

    @ApiModelProperty(value = "操作人")
    private String optName;

    @ApiModelProperty(value = "接单人id")
    private Integer userId;

    @ApiModelProperty(value = "接单人姓名")
    private String userName;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime optTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建用户")
    private String createUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
