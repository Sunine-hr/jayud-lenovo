package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用模板
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderCostTemplate对象", description = "费用模板")
public class OrderCostTemplateDTO extends Model<OrderCostTemplateDTO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private Integer status;

    @ApiModelProperty(value = "状态(0禁用 1启用)")
    private String statusDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "费用类型(0:应收,1:应付)")
    private Integer type;

    @ApiModelProperty(value = "费用项")
    private List<OrderCostTemplateInfoDTO> costTemplateInfo;


    public void checkAddOpt() {
        if (StringUtils.isEmpty(this.name)) {
            throw new JayudBizException(500, "请输入模块名称");
        }
        if (CollectionUtils.isEmpty(costTemplateInfo)){
            throw new JayudBizException(400,"请输入费用项");
        }
        for (OrderCostTemplateInfoDTO orderCostTemplateInfoDTO : costTemplateInfo) {
            orderCostTemplateInfoDTO.checkAdd();
        }

    }

    public void setStatus(Integer status) {
        this.status = status;
        this.statusDesc = StatusEnum.getDesc(String.valueOf(status));
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
