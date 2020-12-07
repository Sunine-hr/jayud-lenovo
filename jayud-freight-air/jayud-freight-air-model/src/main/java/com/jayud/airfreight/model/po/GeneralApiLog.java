package com.jayud.airfreight.model.po;

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
 * 经过api模块进行操作的接口请求历史数据表
 * </p>
 *
 * @author william.chen
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GeneralApiLog对象", description="经过api模块进行操作的接口请求历史数据表")
public class GeneralApiLog extends Model<GeneralApiLog> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "模块名")
    private String moduleName;

    @ApiModelProperty(value = "访问方法")
    private String method;

    @ApiModelProperty(value = "请求json")
    private String requestJson;

    @ApiModelProperty(value = "返回json")
    private String resultJson;

    @ApiModelProperty(value = "访问方IP")
    private String ipAddress;

    @ApiModelProperty(value = "登录用户名称")
    private String loginUser;

    @ApiModelProperty(value = "处理时间")
    private Integer timeSpan;

    @ApiModelProperty(value = "访问发起时间")
    private LocalDateTime requestTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
