package com.jayud.finance.bo;

import com.jayud.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 利润报表查询条件
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Data
public class QueryProfitStatementForm extends BasePageForm {

    @ApiModelProperty(value = "订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "操作部门ids")
    private List<Long> departmentIds;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "结算单位")
    private String unitName;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private List<String> createTime;

    @ApiModelProperty(value = "统计维度(1.所属部门,2.操作部门)")
    private Integer statisticalType;

    @ApiModelProperty(value = "是否打开内部往来费用")
    private Boolean isOpenInternal = false;

//    public void setCreateTime(List<String> createTime) {
//        this.createTime = createTime;
//        if (createTime != null && createTime.size() > 1) {
//            createTime.set(1, DateUtils.strMaximumTime(createTime.get(1)));
//        }
//    }
}
