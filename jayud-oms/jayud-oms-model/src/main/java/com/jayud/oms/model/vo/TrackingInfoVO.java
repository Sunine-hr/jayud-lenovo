package com.jayud.oms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 油卡跟踪信息
 * </p>
 *
 * @author LDR
 * @since 2021-10-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TrackingInfo对象", description = "油卡跟踪信息")
public class TrackingInfoVO extends Model<TrackingInfoVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务类型(1:合同报价)")
    private Integer businessType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "操作类型(1:修改,2:删除,3:新增)")
    private Integer optType;

    @ApiModelProperty(value = "操作类型(1:修改,2:删除,3:新增)")
    private String optTypeDesc;


    public void setOptType(Integer optType) {
        this.optType = optType;
        switch (optType){
            case 1:
                this.optTypeDesc="修改";
                break;
            case 2:
                this.optTypeDesc="删除";
                break;
            case 3:
                this.optTypeDesc="新增";
                break;
        }
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
