package com.jayud.oms.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jayud.oms.model.vo.CostTypeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 费用名描述
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Data
public class AddCostInfoForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "费用code", required = true)
    @NotEmpty(message = "idCode is required")
    private String idCode;

    @ApiModelProperty(value = "费用名", required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "费用类型id集合", required = true)
    private List<Long> cids;

//    @ApiModelProperty(value = "费用类别")
//    @NotNull(message = "cids is required")
//    private List<CostTypeVO> costTypeVOs;


}
