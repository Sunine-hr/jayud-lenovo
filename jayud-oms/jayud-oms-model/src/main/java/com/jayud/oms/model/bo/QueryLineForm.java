package com.jayud.oms.model.bo;

import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * <p>
 * 线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@Data
public class QueryLineForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "审核状态(1-待审核 2-审核通过 3-终止 0-拒绝)")
    @Pattern(regexp = "^$|^([0-3])$", message = "auditStatus requires '1' or '2' or '3' or '0' only")
    private String auditStatus;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;

    public void setCreateTime(List<String> createTime) {
        if (CollectionUtils.isNotEmpty(createTime) && createTime.size() > 1) {
            createTime.set(1, DateUtils.strMaximumTime(createTime.get(1)));
        }
        this.createTime = createTime;
    }
}
