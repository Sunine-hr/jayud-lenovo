package com.jayud.wms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.wms.model.po.WmsPackingOffshelfTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author ciro
 * @date 2022/1/6 10:01
 * @description: 拣货下架VO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="拣货下架VO", description="拣货下架VO")
public class WmsPackingOffshelfVO {

    @ApiModelProperty(value = "拣货下架单号")
    private String packingOffshelfNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "总数")
    private Integer allCount;

    @ApiModelProperty(value = "完成数")
    private Integer finishCount;

    @ApiModelProperty(value = "完成数")
    private Integer unfinishCount;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private List<WmsPackingOffshelfTask> taskList;

    @ApiModelProperty(value = "降序")
    private String descMsg;

    @ApiModelProperty(value = "升序")
    private String ascMsg;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;



}
