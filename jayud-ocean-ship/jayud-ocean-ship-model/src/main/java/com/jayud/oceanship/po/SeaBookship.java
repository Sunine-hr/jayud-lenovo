package com.jayud.oceanship.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 海运订船表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SeaBookship对象", description="海运订船表")
public class SeaBookship extends Model<SeaBookship> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "状态(0:确认,1:待确认,2:删除)")
    private Integer status;

    @ApiModelProperty(value = "海运订单编号")
    private String seaOrderNo;

    @ApiModelProperty(value = "海运订单id")
    private Long seaOrderId;

    @ApiModelProperty(value = "代理供应商id")
    private Long agentSupplierId;

    @ApiModelProperty(value = "入仓号")
    private String warehousingNo;

    @ApiModelProperty(value = "主单号")
    private String mainNo;

    @ApiModelProperty(value = "分单号")
    private String subNo;

    @ApiModelProperty(value = "船公司")
    private String shipCompany;

    @ApiModelProperty(value = "船名字")
    private String shipName;

    @ApiModelProperty(value = "船次")
    private String shipNumber;

    @ApiModelProperty(value = "预计离港时间")
    private LocalDateTime etd;

    @ApiModelProperty(value = "实际离岗时间")
    private LocalDateTime atd;

    @ApiModelProperty(value = "预计到港时间")
    private LocalDateTime eta;

    @ApiModelProperty(value = "实际到港时间")
    private LocalDateTime ata;

    @ApiModelProperty(value = "交仓码头")
    private String deliveryWharf;

    @ApiModelProperty(value = "截关时间")
    private String closingTime;

    @ApiModelProperty(value = "提单文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "提单文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "提单重量")
    private Double billLadingWeight;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
