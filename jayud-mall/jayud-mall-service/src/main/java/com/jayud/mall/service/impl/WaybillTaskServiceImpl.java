package com.jayud.mall.service.impl;

import com.jayud.mall.model.po.WaybillTask;
import com.jayud.mall.mapper.WaybillTaskMapper;
import com.jayud.mall.model.vo.WaybillTaskVO;
import com.jayud.mall.service.IWaybillTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<WaybillTaskVO> findWaybillTaskByOfferInfoId(Integer OfferInfoId) {
        List<WaybillTaskVO> list = waybillTaskMapper.findWaybillTaskByOfferInfoId(OfferInfoId);
        return list;
    }
}
