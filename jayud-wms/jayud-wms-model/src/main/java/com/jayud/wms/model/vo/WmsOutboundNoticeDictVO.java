package com.jayud.wms.model.vo;

import com.jayud.auth.model.po.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ciro
 * @date 2022/4/6 10:11
 * @description:    出库通知单字典
 */
@Data
@ApiModel(value = "出库通知单字典", description = "出库通知单字典")
public class WmsOutboundNoticeDictVO {

    @ApiModelProperty(value = "单据类型")
    List<SysDictItem> orderTypeDict;

    @ApiModelProperty(value = "单位")
    List<SysDictItem> unitDict;

    @ApiModelProperty(value = "车型")
    List<SysDictItem> carTypeDict;

    @ApiModelProperty(value = "出库通知单状态")
    List<SysDictItem> statusTypeDict;

    @ApiModelProperty(value = "出库单状态")
    List<SysDictItem> orderStatusTypeDict;

    @ApiModelProperty(value = "服务类型")
    List<SysDictItem> serviceTypeDict;

    @ApiModelProperty(value = "发运复核状态")
    List<SysDictItem> reviewStatusTypeDict;


}
