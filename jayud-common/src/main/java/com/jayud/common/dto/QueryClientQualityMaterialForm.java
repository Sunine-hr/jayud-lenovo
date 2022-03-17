package com.jayud.common.dto;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 *  外部调用货单物料信息对象
 *
 * @author
 * @since
 */
@Data
public class QueryClientQualityMaterialForm {

    /**
     * 收货单id
     */
    @ApiModelProperty(value = "收货单id")
    protected Long receiptId;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "预计数量")
    private Double num;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "文件预留one")
    private List<FileView> fileObject;

    @ApiModelProperty(value = "文件字段预留")
    private List<String> fileList;

    @ApiModelProperty(value = "物料序列号(SN)")
    private String serialNum;


}
