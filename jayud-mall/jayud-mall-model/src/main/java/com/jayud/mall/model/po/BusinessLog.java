package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 业务日志表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BusinessLog对象", description="业务日志表")
public class BusinessLog extends Model<BusinessLog> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
      private Long id;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "操作人id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "操作人name(system_user name)")
    private String userName;

    @ApiModelProperty(value = "业务表tb")
    private String businessTb;

    @ApiModelProperty(value = "业务表(中文)name")
    private String businessName;

    @ApiModelProperty(value = "业务操作(insert update delete)")
    private String businessOperation;

    @ApiModelProperty(value = "操作前(text)")
    private String operationFront;

    @ApiModelProperty(value = "操作后(text)")
    private String operationAfter;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
