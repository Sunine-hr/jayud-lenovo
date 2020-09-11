package com.jayud.service.impl;

import com.jayud.model.po.CustomsApiLog;
import com.jayud.mapper.CustomsApiLogMapper;
import com.jayud.service.ICustomsApiLogService;
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
public class CustomsApiLogServiceImpl extends ServiceImpl<CustomsApiLogMapper, CustomsApiLog> implements ICustomsApiLogService {

}
