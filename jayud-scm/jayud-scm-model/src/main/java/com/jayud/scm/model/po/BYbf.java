package com.jayud.scm.model.po;

import java.math.BigDecimal;
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
 * 运保费表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BYbf对象", description="运保费表")
public class BYbf extends Model<BYbf> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime beginDtm;

    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDtm;

    @ApiModelProperty(value = "CIF运保费比例")
    private BigDecimal cif;

    @ApiModelProperty(value = "FOB运费比例")
    private BigDecimal fob1;

    @ApiModelProperty(value = "FOB保费比例")
    private BigDecimal fob2;

    @ApiModelProperty(value = "FOB杂费比例")
    private BigDecimal fob3;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
