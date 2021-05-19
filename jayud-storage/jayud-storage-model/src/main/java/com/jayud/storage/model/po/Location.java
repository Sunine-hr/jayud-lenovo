package com.jayud.storage.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 库位对应的库位编码
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Location对象", description="库位对应的库位编码")
public class Location extends Model<Location> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "库位id")
    private Long locationId;

    @ApiModelProperty(value = "库位编码")
    private String locationCode;

    @ApiModelProperty(value = "0为有效，1为无效")
    private Integer status;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
