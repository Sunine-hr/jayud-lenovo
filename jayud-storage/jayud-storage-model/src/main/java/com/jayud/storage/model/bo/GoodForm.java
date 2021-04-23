package com.jayud.storage.model.bo;

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
 * 商品信息维护表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Good对象", description="商品信息维护表")
public class GoodForm extends Model<GoodForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "0为无效，1为有效")
    private Integer status;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "公司简称代码")
    private String customerCode;

    @ApiModelProperty(value = "长")
    private Double goodLength;

    @ApiModelProperty(value = "宽")
    private Double goodWidth;

    @ApiModelProperty(value = "高")
    private Double goodHeight;

    @ApiModelProperty(value = "商品价值")
    private Double commodityValue;

    @ApiModelProperty(value = "生产日期")
    private LocalDateTime dateOfManufacture;

    @ApiModelProperty(value = "截止有效期")
    private LocalDateTime expirationDate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
