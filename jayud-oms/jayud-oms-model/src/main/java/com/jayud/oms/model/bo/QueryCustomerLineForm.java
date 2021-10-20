package com.jayud.oms.model.bo;

import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 客户线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-19
 */
@Data
public class QueryCustomerLineForm extends BasePageForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户线路名称")
    private String customerLineName;

    @ApiModelProperty(value = "线路名称")
    private String lineName;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;

    public void setCreateTime(List<String> createTime) {
        if (CollectionUtils.isNotEmpty(createTime) && createTime.size() > 1) {
            createTime.set(1, DateUtils.strMaximumTime(createTime.get(1)));
        }
        this.createTime = createTime;
    }
}
