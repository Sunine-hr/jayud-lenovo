package com.jayud.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.sql.Blob;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报关请求接口历史数据表
 * </p>
 *
 * @author william.chen
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CustomsApiLog对象", description="报关请求接口历史数据表")
public class CustomsApiLog extends Model<CustomsApiLog> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "访问方法")
    private String method;

    @ApiModelProperty(value = "请求json")
    private String requestJson;

    @ApiModelProperty(value = "返回json")
    private String resultJson;

    @ApiModelProperty(value = "访问方IP")
    private String ipAddress;

    @ApiModelProperty(value = "访问用户id")
    private Integer userId;

    @ApiModelProperty(value = "处理时间")
    private Integer timeSpan;

    @ApiModelProperty(value = "访问发起时间")
    private LocalDateTime requestTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
