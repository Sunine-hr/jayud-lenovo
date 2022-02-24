package com.jayud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ciro
 * @date 2021/12/13 16:56
 * @description: 系统日志对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO implements Serializable {
    private static final long serialVersionUID = 8482720462943906924L;
    /**
     * 内容
     */
    private String logContent;
    /**
     * 日志类型：1-日常，2-登录
     */
    private Integer logType;
    /**
     * 操作类型：1-操作：1-查询，2-添加，3-更新，4-删除，5-导入，6-导出；2-登录：1-登录，2-登出；3-定时：1-定时
     */
    private Integer operateType;

    /**
     * 主键ID
     */
    private String id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 消耗时间
     */
    private Long costTime;
    /**
     * 请求ip
     */
    private String ip;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 请求类型
     */
    private String requestType;
    /**
     * 请求路径
     */
    private String requestUrl;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 操作人用户名称
     */
    private String username;
    /**
     * 操作人用户账户
     */
    private String userId;


}
