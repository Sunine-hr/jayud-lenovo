package com.jayud.oms.model.bo;

import com.jayud.oms.model.vo.LineVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 线路-批量操作
 */
@Data
public class LineBatchOprForm implements Serializable {

    @ApiModelProperty(value = "审核状态(1-待审核 2-审核通过 3-终止 0-拒绝)")
    @NotEmpty(message = "审核状态不能为空")
    @Pattern(regexp = "1|2|3|0",message = "auditStatus requires '1' or '2' or '3' or '0' only")
    private String auditStatus;

    @Valid
    @ApiModelProperty(value = "批量操作ID数组对象")
    @NotNull(message = "ID数组不能为空")
    @Size(min = 1, message = "ID数组不能为空")
    private List<Map<String, Object>> list;

}
