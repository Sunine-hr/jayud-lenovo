package com.jayud.storage.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 商品区域货架表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class WarehouseAreaShelvesFormVO extends Model<WarehouseAreaShelvesFormVO> {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "仓库代码")
    private String code;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "区域代码")
    private String areaCode;

    @ApiModelProperty(value = "货架名称")
    private String shelvesName;

    @ApiModelProperty(value = "二维码")
    private String qrUrl;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;

}
