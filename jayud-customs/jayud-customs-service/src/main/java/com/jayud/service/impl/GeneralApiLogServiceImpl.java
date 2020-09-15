package com.jayud.service.impl;

import com.jayud.model.po.GeneralApiLog;
import com.jayud.mapper.GeneralApiLogMapper;
import com.jayud.service.IGeneralApiLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报关请求接口历史数据表 服务实现类
 * </p>
 *
 * @author william.chen
 * @since 2020-09-10
 */
@Service
public class GeneralApiLogServiceImpl extends ServiceImpl<GeneralApiLogMapper, GeneralApiLog> implements IGeneralApiLogService {

}
