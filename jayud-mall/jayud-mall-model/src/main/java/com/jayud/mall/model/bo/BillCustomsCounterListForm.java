package com.jayud.mall.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.mall.model.vo.CounterListInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BillCustomsCounterListForm {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "类型(1 报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "报关、清关id(报关bill_customs_info id 清关bill_clearance_info id)")
    private Long customsId;

    @ApiModelProperty(value = "柜子清单id(counter_list_info id)")
    private Long counterListId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "装柜清单列表list")
    private List<CounterListInfoVO> counterListInfoList;


}
