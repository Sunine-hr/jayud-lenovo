package com.jayud.auth.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SysLog 实体类
 *
 * @author jayud
 * @since 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统日志表对象", description="系统日志表")
public class SysLogForm extends SysBaseEntity {


    @ApiModelProperty(value = "日志类型（1登录日志，2操作日志）")
    private Integer logType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "操作类型(0:系统,1:客户)")
    private Integer operateType;

    @ApiModelProperty(value = "操作用户账号")
    private String userid;

    @ApiModelProperty(value = "操作用户名称")
    private String username;

    @ApiModelProperty(value = "用户真实姓名")
    private String trueName;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "请求java方法")
    private String method;

    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    @ApiModelProperty(value = "请求类型")
    private String requestType;

    @ApiModelProperty(value = "耗时")
    private Long costTime;

    @ApiModelProperty(value = "创建时间")
    private List<String> creationTime;

    @ApiModelProperty(value = "创建时间前")
    private String creationTimeOne;

    @ApiModelProperty(value = "创建时间后")
    private String creationTimeTwo;


    public void setCreationTime(List<String> creationTime) {
        /**
         *  客户管理-时间配置
         */
        String STARTING_TIME_TYPE = "2000-01-01 00:00:00";

        this.creationTime = creationTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String format = sdf.format(date);
        // 只传第一个 计算设置时间到 当前时间
        if(creationTime.get(0)!=null&&creationTime.get(1)==null){
            this.creationTimeOne = creationTime.get(0);
//            if(creationTime.get(1)!=null){
//                this.creationTimeTwo = creationTime.get(1);
//            }else {
            this.creationTimeTwo = format ;
//            }
        }
        // 只传第二个 计算 到第二个时间之前的所有的
        if(creationTime.get(1)!=null&&creationTime.get(0)==null){
            this.creationTimeOne = STARTING_TIME_TYPE ;
            this.creationTimeTwo = creationTime.get(1);
        }
        if(creationTime.get(0)!=null&&creationTime.get(1)!=null){
            this.creationTimeOne = creationTime.get(0);
            this.creationTimeTwo = creationTime.get(1);
        }
    }


}
