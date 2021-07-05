package com.jayud.oms.model.bo;

import cn.hutool.core.io.unit.DataUnit;
import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryOrderInfoForm extends BasePageForm {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸")
    private String portCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结束创建时间")
    private String endCreateTimeStr;

    @ApiModelProperty(value = "开始创建时间")
    private String beginCreateTimeStr;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "操作指令,cmd = noSubmit草稿 or submit全部 or costAudit费用审核 or outCustomsRelease外部报关放行" +
            "or goCustomsAudit通关前审核 or dataNotAll待补全 or 待取消处理cancelled or 待驳回处理rejected ")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户,前台传", required = true)
    @NotEmpty(message = "loginUserName is required")
    private String loginUserName;


    @ApiModelProperty(value = "菜单入口 我的订单myOrder")
    private String entrance;

    @ApiModelProperty(value = "创建用户")
    private String createdUser;

    @ApiModelProperty(value = "主订单id集合")
    private List<Long> mainOrderIds;

    @ApiModelProperty(value = "操作时间")
    private List<String> operationTime;

    public void setOperationTime(List<String> operationTime) {
        if (CollectionUtils.isNotEmpty(operationTime) && operationTime.size() > 1) {
            operationTime.set(1, DateUtils.strMaximumTime(operationTime.get(1)));
        }
        this.operationTime = operationTime;
    }
}
