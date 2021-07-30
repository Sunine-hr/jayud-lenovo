package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 培训管理
 * </p>
 *
 * @author LDR
 * @since 2021-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "TrainingManagement对象", description = "培训管理")
public class QueryTrainingManagementFrom extends BasePageForm{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "培训主题")
    private String subject;

    @ApiModelProperty(value = "培训开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime trainingStartTime;

    @ApiModelProperty(value = "培训结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime trainingEndTime;


    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;





}
