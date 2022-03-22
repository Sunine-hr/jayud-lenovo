package com.jayud.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jayud.common.HttpContextUtils;
import com.jayud.common.dto.LogDTO;
import com.jayud.common.enums.SysLogOperateTypeEnum;
import com.jayud.common.enums.SysLogTypeEnum;
import com.jayud.common.mapper.BaseCommonMapper;
import com.jayud.common.service.BaseCommonService;
import com.jayud.common.utils.HttpUtils;
import com.xxl.job.core.util.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author ciro
 * @date 2022/2/24 14:23
 * @description:    common接口实现类
 */
@Service
public class BaseCommonServiceImpl implements BaseCommonService {

    @Autowired
    private BaseCommonMapper baseCommonMapper;


    @Override
    public void addLog(LogDTO logDTO) {
        baseCommonMapper.saveLog(logDTO);
    }

    @Override
    public void addLog(String logContent, SysLogTypeEnum sysLogTypeEnum, String method, String requestParam, Long costTime, String username,String trueName) {
        LogDTO sysLog = new LogDTO();
        sysLog.setLogType(sysLogTypeEnum.getType());
        sysLog.setLogContent(logContent);
        sysLog.setOperateType(SysLogOperateTypeEnum.getOperateType(sysLogTypeEnum, method));
        try {
            //获取request
            HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
            //设置IP地址
            sysLog.setIp(HttpUtils.getIpAddr(request));
            sysLog.setMethod(method);
            sysLog.setRequestUrl(request.getRequestURI());
            sysLog.setRequestParam(requestParam);
            sysLog.setCostTime(costTime);
            sysLog.setRequestType(request.getMethod());
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        sysLog.setUserId(username);
        sysLog.setUsername(username);
        sysLog.setTrueName(trueName);
        sysLog.setCreateBy(username);
        sysLog.setCreateTime(new Date());
        try {
            baseCommonMapper.saveLog(sysLog);
        } catch (Exception e) {

        }
    }
}
