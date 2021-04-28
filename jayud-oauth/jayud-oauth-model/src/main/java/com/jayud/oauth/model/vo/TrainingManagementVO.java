package com.jayud.oauth.model.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@ApiModel(value = "TrainingManagement对象", description = "培训管理")
public class TrainingManagementVO extends Model<TrainingManagementVO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "培训主题")
    private String subject;

    @ApiModelProperty(value = "培训开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime trainingStartTime;

    @ApiModelProperty(value = "培训结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime trainingEndTime;

    @ApiModelProperty(value = "培训地点")
    private String trainingLocation;

    @ApiModelProperty(value = "培训内容")
    private String content;

    @ApiModelProperty(value = "培训对象(多个对象隔开)")
    private List<String> trainees;

//    @ApiModelProperty(value = "附件路径(多个逗号隔开)")
//    private String filePath;
//
//    @ApiModelProperty(value = "附件名字(多个逗号隔开)")
//    private String fileName;

    @ApiModelProperty(value = "附件")
    private List<FileView> fileViewList;

    @ApiModelProperty(value = "创建人(登录用户)")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    public void setTrainees(String trainees) {
        String[] split = trainees.split(",");
        this.trainees = Arrays.asList(split);
    }


}
