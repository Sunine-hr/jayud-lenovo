package com.jayud.common.service;

import com.jayud.common.dto.LogDTO;
import com.jayud.common.enums.SysLogTypeEnum;
import org.springframework.stereotype.Component;

/**
 * @author ciro
 * @date 2022/2/24 14:23
 * @description:    common接口实现类
 */
@Component
public interface BaseCommonService {

    /**
     * 保存日志
     *
     * @param logDTO
     */
    void addLog(LogDTO logDTO);

    /**
     * @description 保存日志
     * @author  ciro
     * @date   2022/2/24 15:02
     * @param: logContent
     * @param: sysLogTypeEnum
     * @param: method
     * @param: requestParam
     * @param: costTime
     * @param: username
     * @param: trueName 真实名称
     * @return: void
     **/
    void addLog(String logContent, SysLogTypeEnum sysLogTypeEnum, String method, String requestParam, Long costTime, String username,String trueName);

}
