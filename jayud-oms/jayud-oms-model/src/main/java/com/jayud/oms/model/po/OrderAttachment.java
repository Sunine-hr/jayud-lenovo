package com.jayud.oms.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单附件
 * </p>
 *
 * @author LDR
 * @since 2021-02-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderAttachment对象", description="订单附件")
public class OrderAttachment extends Model<OrderAttachment> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单编号")
    private String orderNo;

    @ApiModelProperty(value = "附件文件路径(多个逗号隔开)")
    private String filePath;

    @ApiModelProperty(value = "附件文件名称(多个逗号隔开)")
    private String fileName;

    @ApiModelProperty(value = "描述(可以当key使用)")
    private String remarks;

    @ApiModelProperty(value = "上传时间")
    private LocalDateTime uploadTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
