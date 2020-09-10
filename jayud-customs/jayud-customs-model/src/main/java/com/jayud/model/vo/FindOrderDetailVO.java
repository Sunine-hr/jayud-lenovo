package com.jayud.model.vo;

import com.jayud.model.po.CustomsHead;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询委托单明细vo
 *
 * @author william
 * @description
 * @Date: 2020-09-09 09:24
 */
@Data
public class FindOrderDetailVO {
    @ApiModelProperty(value = "委托单头信息")
    private CustomsHeadVO head;
    @ApiModelProperty(value = "委托单柜号信息列表")
    private List<CustomsDetailVO> dtls;
    @ApiModelProperty(value = "委托单商品信息列表")
    private List<CustomsGoodsVO> gdtls;
    @ApiModelProperty(value = "委托单随附单证信息列表")
    private List<CustomsAppendixVO> adtls;
}
