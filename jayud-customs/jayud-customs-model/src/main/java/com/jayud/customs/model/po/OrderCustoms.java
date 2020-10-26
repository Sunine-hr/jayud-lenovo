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

    @ApiModelProperty(value = "柜号上传附件地址名称")
    private String cntrPicName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关抬头")
    private String title;

    @ApiModelProperty(value = "单双抬头")
    private String isTitle;

    @ApiModelProperty(value = "结算代码(customer_info)")
    private String unitCode;

    @ApiModelProperty(value = "附件(json存储 文件名，url)")
    private String description;

    @ApiModelProperty(value = "附件名称,多个逗号分隔")
    private String descName;

    @ApiModelProperty(value = "委托单号")
    private String entrustNo;

    @ApiModelProperty(value = "六联单号附件")
    private String encodePic;

    @ApiModelProperty(value = "六联单号附件名称")
    private String encodePicName;

    @ApiModelProperty(value = "业务模式(1-陆路运输 2-空运 3-海运 4-快递)")
    private String bizModel;

    @ApiModelProperty(value = "提运单")
    private String airTransportNo;

    @ApiModelProperty(value = "提运单附件")
    private String airTransportPic;

    @ApiModelProperty(value = "提运单附件名称")
    private String airTransPicName;

    @ApiModelProperty(value = "提运单号")
    private String seaTransportNo;

    @ApiModelProperty(value = "提运单号附件")
    private String seaTransportPic;

    @ApiModelProperty(value = "提运单号附件名称")
    private String seaTransPicName;

    @ApiModelProperty(value = "是否代垫税金1-是 0-否")
    private String isAgencyTax;

    @ApiModelProperty(value = "状态(0-未接单 1-已接单 2-接单中 3-放行通过 4-放行驳回  5-已完成)")
    private String status;

    @ApiModelProperty(value = "通关时间")
    private LocalDateTime goCustomsTime;

    @ApiModelProperty(value = "预计通关时间")
    private LocalDateTime preGoCustomsTime;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "创建用户")
    private String createdUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "报关类型 CBG-纯报关 CKBG-出口报关")
    private String classCode;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单时间")
    private LocalDateTime jiedanTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
