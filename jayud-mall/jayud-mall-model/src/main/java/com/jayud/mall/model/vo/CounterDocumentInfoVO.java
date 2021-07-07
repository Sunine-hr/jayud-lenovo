package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CounterDocumentInfoVO {

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "柜子id(ocean_counter id)")
    private Long counterId;

    @ApiModelProperty(value = "柜号(ocean_counter cntr_no)")
    private String cntrNo;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件地址(附件)")
    private String templateUrl;

    @ApiModelProperty(value = "说明")
    private String describes;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)")
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //extend 扩展的字段
    @ApiModelProperty(value = "文件地址(附件)文件上传")
    private List<TemplateUrlVO> templateUrls;


}