package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OperationTeamVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "小组代码")
    private String groupCode;

    @ApiModelProperty(value = "小组名称")
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    //人员
    @ApiModelProperty(value = "运营小组人员（前端：人员）")
    private String memberUserNames;

    @ApiModelProperty(value = "运营小组人员list")
    private List<OperationTeamMemberVO> operationTeamMemberVOS;

}
