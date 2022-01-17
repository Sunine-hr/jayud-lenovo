package com.jayud.scm.model.vo;


import com.jayud.scm.model.bo.CustomsAppendixForm;
import com.jayud.scm.model.bo.CustomsDetailForm;
import com.jayud.scm.model.bo.CustomsGoodsForm;
import com.jayud.scm.model.bo.CustomsHeadForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 委托单上传form（如果提供了uid，将更新数据，如果未提供uid，将新增）
 *
 * @author william
 * @description
 * @Date: 2020-09-07 18:38
 */
@Data
public class PushOrderVO {
    @ApiModelProperty(value = "报关单头")
    private CustomsHeadForm head;
    @ApiModelProperty(value = "委托单柜号列表")
    private List<CustomsDetailForm> dtl;
    @ApiModelProperty(value = "委托单商品列表")
    private List<CustomsGoodsForm> gdtl;
    @ApiModelProperty(value = "委托单随附单证列表")
    private List<CustomsAppendixForm> adtl;
    @ApiModelProperty(value = "报关回执返回地址")
    //如果需要接受报关回执，设置该地址，云报关采用POST方式向该地址发送回执。回执有两种，都是JSON格式
    private String callback;
}
