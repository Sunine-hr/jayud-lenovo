package com.jayud.oauth.model.bo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@ApiModel(value = "TrainingManagement对象", description = "培训管理")
public class AddTrainingManagementForm extends Model<AddTrainingManagementForm> {

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

    @ApiModelProperty(value = "培训地点")
    private String trainingLocation;

    @ApiModelProperty(value = "培训内容")
    private String content;

    @ApiModelProperty(value = "培训对象(多个对象隔开)")
    private String trainees;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViewList;

    public void setTrainees(List<String> trainees) {
        StringBuilder sb = new StringBuilder();
        trainees.forEach(e -> sb.append(e).append(","));
        this.trainees = sb.toString();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void checkAdd() {
        if (StringUtils.isEmpty(subject)) {
            throw new JayudBizException(400, "培训主题不能为空");
        }
        if (trainingStartTime == null) {
            throw new JayudBizException(400, "培训开始时间不能为空");
        }
        if (trainingEndTime == null) {
            throw new JayudBizException(400, "培训结束时间不能为空");
        }
        if (StringUtils.isEmpty(trainingLocation)) {
            throw new JayudBizException(400, "培训地点不能为空");
        }
        if (StringUtils.isEmpty(content)) {
            throw new JayudBizException(400, "培训内容不能为空");
        }
        if (trainees == null) {
            throw new JayudBizException(400, "培训对象");
        }
//        if (fileViewList == null) {
//            throw new JayudBizException(400, "附件不能为空");
//        }

    }

    public static void main(String[] args) {
        Utilities.printCheckCode(AddTrainingManagementForm.class);
    }
}
