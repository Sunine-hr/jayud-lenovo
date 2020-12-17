package com.jayud.tms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 中港运输订单扩展字段表
 * </p>
 *
 * @author LDR
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TmsExtensionField对象", description="中港运输订单扩展字段表")
public class TmsExtensionField extends Model<TmsExtensionField> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "业务主键")
    private Long businessId;

    @ApiModelProperty(value = "第三方唯一标志")
    private String thirdPartyUniqueSign;

    @ApiModelProperty(value = "业务表(例如:order_transport)")
    private String businessTable;

    @ApiModelProperty(value = "数据(json格式)")
    private String value;

    @ApiModelProperty(value = "类型(0:vivo,待定)")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "描述(也可以当key值使用)")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
