package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品区域货架表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class WarehouseAreaShelvesForm extends Model<WarehouseAreaShelvesForm> {

    @ApiModelProperty(value = "区域名称")
    @NotNull(message = "区域名称不为空")
    private String areaName;

    @ApiModelProperty(value = "区域代码")
    @NotNull(message = "区域名称不为空")
    private String areaCode;

    @ApiModelProperty(value = "货架名称")
    @NotNull(message = "区域名称不为空")
    private List<ShelvesForm> shelvesName;

    @ApiModelProperty(value = "0为无效，1为有效")
    private Integer status;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "区域id")
    @NotNull(message = "区域名称不为空")
    private Long areaId;


}
