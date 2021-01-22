package com.jayud.oms.model.bo;

import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InputSubOrderCustomsForm {

    @ApiModelProperty(value = "报关子订单", required = true)
    private String orderNo;

    @ApiModelProperty(value = "报关抬头", required = true)
    private String title;

    @ApiModelProperty(value = "结算单位code", required = true)
    private String unitCode;

    @ApiModelProperty(value = "附件,多个逗号分隔,前台忽略")
    private String description;

    @ApiModelProperty(value = "附件名称,多个逗号分隔,前台忽略")
    private String descName;

    @ApiModelProperty(value = "单双抬头")
    private String isTitle;

    @ApiModelProperty(value = "附件", required = true)
    private List<FileView> fileViews = new ArrayList<>();

    public void checkSubOrderCustoms() {
        if (StringUtil.isNullOrEmpty(this.getOrderNo())
                || StringUtil.isNullOrEmpty(this.getTitle())
                || StringUtil.isNullOrEmpty(this.getUnitCode())
                || StringUtil.isNullOrEmpty(this.getIsTitle())) {
            throw new JayudBizException(ResultEnum.PARAM_ERROR);
        }
    }

}
