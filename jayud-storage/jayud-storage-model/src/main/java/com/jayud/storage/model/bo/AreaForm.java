package com.jayud.storage.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 仓库区域表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-27
 */
@Data
public class AreaForm extends Model<AreaForm> {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "区域代码")
    private String areaCode;

}
