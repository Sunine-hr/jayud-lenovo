package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.BusinessLog;
import com.jayud.mall.mapper.BusinessLogMapper;
import com.jayud.mall.service.IBusinessLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务日志表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-24
 */
@Service
public class BusinessLogServiceImpl extends ServiceImpl<BusinessLogMapper, BusinessLog> implements IBusinessLogService {

}
