package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * <p>
 * 线路管理
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@Data
public class AddLineForm {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Long id;

    @ApiModelProperty(value = "线路编号", required = true)
    @NotEmpty(message = "线路编号不能为空")
    @Length(max = 14, message = "线路编号最大长度14位")
    @Length(min = 14, message = "线路编号长度为14位")
    private String lineCode;

    @ApiModelProperty(value = "线路类型(城配/干线等-数据字典配置)", required = true)
    @NotEmpty(message = "线路类型不能为空")
    private String lineType;

    @ApiModelProperty(value = "线路名称", required = true)
    @NotEmpty(message = "线路名称不能为空")
    private String lineName;

    @ApiModelProperty(value = "出发地-省")
    private Long fromCountry;

    @ApiModelProperty(value = "出发地-省")
    private Long fromProvince;

    @ApiModelProperty(value = "出发地-城市")
    private Long fromCity;

    @ApiModelProperty(value = "出发地-区县")
    private Long fromRegion;

    @ApiModelProperty(value = "目的地-省")
    private Long toProvince;

    @ApiModelProperty(value = "目的地-城市")
    private Long toCity;

    @ApiModelProperty(value = "目的地-区县")
    private Long toRegion;

    @ApiModelProperty(value = "路由属性(自营/加盟-数据字典配置)")
    private String routeAttribute;

    @ApiModelProperty(value = "里程(km)")
    private Double mileage;

    @ApiModelProperty(value = "时效(h)")
    private Double prescription;

    @ApiModelProperty(value = "日均里程(km)")
    private Double averageMileage;

    @ApiModelProperty(value = "审核状态(1-待审核 2-审核通过 3-终止 0-拒绝)")
    private Integer auditStatus;

    @ApiModelProperty(value = "途经点")
    private String passing;

    @ApiModelProperty(value = "路线路由")
    private String lineRoute;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
