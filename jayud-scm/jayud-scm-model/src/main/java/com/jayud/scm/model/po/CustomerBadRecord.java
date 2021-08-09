package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 客户不良记录表
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CustomerBadRecord对象", description="客户不良记录表")
public class CustomerBadRecord extends Model<CustomerBadRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动id")
      private Integer id;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "0:无不良记录，1有")
    private Integer isBad;

    @ApiModelProperty(value = "不良记录类别")
    private String isBadType;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Integer crtBy;

    private String crtByName;

    private LocalDateTime crtDtm;

    private Integer mdyBy;

    private String mdyByName;

    private LocalDateTime mdyDtm;

    private Integer voidedBy;

    private String voidedByName;

    private LocalDateTime voidedDtm;

    private Integer voided;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
