package com.jayud.oceanship.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 船港口地址表
 * </p>
 *
 * @author LLJ
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SeaPort对象", description="船港口地址表")
public class AddSeaPortForm extends Model<AddSeaPortForm> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ExcelProperty("港口编码")
    @ApiModelProperty(value = "船港口代码")
    private String code;

    @ExcelProperty("港口英文")
    @ApiModelProperty(value = "船港口名称")
    private String name;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private Integer status;

    @ExcelProperty("国家")
    @ApiModelProperty(value = "国家")
    private String country;

    @ExcelProperty("航线")
    @ApiModelProperty(value = "航线")
    private String route;

    @ExcelProperty("港口中文")
    @ApiModelProperty(value = "港口中文名")
    private String chineseName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
