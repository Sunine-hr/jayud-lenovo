package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 产品服务对应业务类型
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class QueryProductBizForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "业务类型")
    private String name;

    @ApiModelProperty(value = "状态 0禁用 1启用")
    private String status;

}
