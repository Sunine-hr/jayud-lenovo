package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.WaybillTaskMapper;
import com.jayud.mall.model.po.WaybillTask;
import com.jayud.mall.service.IWaybillTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运单(订单)任务列表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
@Service
public class WaybillTaskServiceImpl extends ServiceImpl<WaybillTaskMapper, WaybillTask> implements IWaybillTaskService {

    @Autowired
    WaybillTaskMapper waybillTaskMapper;

}
