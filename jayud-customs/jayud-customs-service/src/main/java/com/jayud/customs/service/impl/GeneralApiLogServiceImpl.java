package com.jayud.customs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.customs.mapper.GeneralApiLogMapper;
import com.jayud.customs.service.IGeneralApiLogService;
import com.jayud.model.po.GeneralApiLog;
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
