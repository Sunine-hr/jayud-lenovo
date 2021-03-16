package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对账单管理出的对账单详情预览
 * 根据账单编号查询，此时已出对账单
 */
@Data
public class ViewBillDetailForm {

    @ApiModelProperty(value = "对账单编号")
    private String billNo;

//    @ApiModelProperty(value = "指令cmd=bg,ky,zgys,nl ..和费用类型一样")
//    private String cmd;
}
