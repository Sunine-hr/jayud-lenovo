package com.jayud.airfreight.service.impl;

import com.jayud.airfreight.model.po.GeneralApiLog;
import com.jayud.airfreight.mapper.GeneralApiLogMapper;
import com.jayud.airfreight.service.IGeneralApiLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 经过api模块进行操作的接口请求历史数据表 服务实现类
 * </p>
 *
 * @author william.chen
 * @since 2020-09-17
 */
@Service
public class GeneralApiLogServiceImpl extends ServiceImpl<GeneralApiLogMapper, GeneralApiLog> implements IGeneralApiLogService {

}
