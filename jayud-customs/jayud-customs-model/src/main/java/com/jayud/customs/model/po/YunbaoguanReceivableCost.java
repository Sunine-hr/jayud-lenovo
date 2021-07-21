package com.jayud.customs.model.po;

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
 * 云报关应收费用信息
 * </p>
 *
 * @author Daniel
 * @since 2021-05-19
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="YunbaoguanReceivableCost对象", description="云报关应收费用信息")
public class YunbaoguanReceivableCost extends Model<YunbaoguanReceivableCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报关单号(18位)")
    private String applyNo;

    @ApiModelProperty(value = "委托单号")
    private String uid;

    @ApiModelProperty(value = "应收费用数据")
    private String receivableCostData;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "是否完成费用同步")
    private Boolean isComplete;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
