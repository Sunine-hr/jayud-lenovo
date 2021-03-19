package com.jayud.trailer.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行)
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderStatus对象", description="业务流程节点，例如报关有(报关接单，报关打单，报关复核 报关申请 报关放行)")
public class OrderStatus extends Model<OrderStatus> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "编码")
    private String idCode;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "哪些状态就属于这个流程接点得")
    private String containState;

    @ApiModelProperty(value = "父流程节点")
    private Long fId;

    @ApiModelProperty(value = "对应业务code(product_biz code)")
    private String classCode;

    @ApiModelProperty(value = "对应业务名(product_biz name)")
    private String className;

    @ApiModelProperty(value = "所有子节点的排序,主节点可不配置")
    private Integer subSorts;

    @ApiModelProperty(value = "单个主流程下面的排序")
    private Integer sorts;

    @ApiModelProperty(value = "状态(0-无效 1有效)")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
