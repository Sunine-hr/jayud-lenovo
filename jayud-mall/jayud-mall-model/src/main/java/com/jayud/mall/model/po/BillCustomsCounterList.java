package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * （提单)报关、清关 关联 柜子清单 表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BillCustomsCounterList对象", description = "（提单)报关、清关 关联 柜子清单 表")
public class BillCustomsCounterList extends Model<BillCustomsCounterList> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
