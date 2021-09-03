package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.common.exception.JayudBizException;
import com.jayud.oms.model.po.CostGenreTaxRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础数据费用类型
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@Data
public class AddCostGenreForm extends Model<AddCostGenreForm> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键,修改时必传")
    private Long id;

    @ApiModelProperty(value = "费用类型代码", required = true)
    @NotEmpty(message = "code is required")
    private String code;

    @ApiModelProperty(value = "费用类型名称", required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "税率")
    private List<CostGenreTaxRate> costGenreTaxRates;

    public void checkAdd() {
        if (CollectionUtils.isEmpty(costGenreTaxRates)) {
            return;
        }
        if (costGenreTaxRates.stream().anyMatch(e->e.getCostTypeId()==null||e.getTaxRate()==null)) {
            throw new JayudBizException(400,"作业环节/税率必填");
        }
        Map<Long, Long> map = costGenreTaxRates.stream().collect(Collectors.groupingBy(CostGenreTaxRate::getCostTypeId, Collectors.counting()));
        map.forEach((k,v)->{
            if (v>1){
                throw new JayudBizException(400,"不能选择相同的作业环节");
            }
        });
    }
}
