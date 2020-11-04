package com.jayud.finance.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 开票
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MakeInvoice对象", description="")
public class MakeInvoice extends Model<MakeInvoice> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开票ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票时间")
    private LocalDateTime makeTime;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "附件")
    private String fileUrl;

    @ApiModelProperty(value = "附件名称")
    private String fileName;

    @ApiModelProperty(value = "是否作废 1-有效 0-无效")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
