package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.CounterCaseInfoMapper;
import com.jayud.mall.mapper.CounterListInfoMapper;
import com.jayud.mall.mapper.CounterOrderInfoMapper;
import com.jayud.mall.model.bo.OrderInfoQueryForm;
import com.jayud.mall.model.po.CounterListInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.ICounterListInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜子清单信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Service
public class CounterListInfoServiceImpl extends ServiceImpl<CounterListInfoMapper, CounterListInfo> implements ICounterListInfoService {

    @Autowired
    CounterListInfoMapper counterListInfoMapper;
    @Autowired
    CounterOrderInfoMapper counterOrderInfoMapper;
    @Autowired
    CounterCaseInfoMapper counterCaseInfoMapper;

    @Override
    public CounterListInfoVO findCounterListInfoById(Long id) {
        return counterListInfoMapper.findCounterListInfoById(id);
    }

    @Override
    public List<CounterCaseInfoVO> findCounterCaseInfo(Long b_id) {
        return counterListInfoMapper.findCounterCaseInfo(b_id);
    }

    @Override
    public List<CounterCaseInfoExcelVO> findCounterCaseInfoBybid(Long b_id) {
        List<CounterCaseInfoExcelVO> counterCaseInfoExcelVOS = counterListInfoMapper.findCounterCaseInfoBybid(b_id);
        return counterCaseInfoExcelVOS;
    }

    @Override
    public List<OrderInfoVO> findUnselectedOrderInfo(OrderInfoQueryForm form) {
        //1.查询过滤的订单id
        Long bId = form.getCounterListInfoId();
        List<Long> filterOrderIds = new ArrayList<>();
        List<CounterOrderInfoVO> counterOrderInfoList = counterOrderInfoMapper.findCounterOrderInfoBybId(bId);
        if(CollUtil.isNotEmpty(counterOrderInfoList)){
            counterOrderInfoList.forEach(counterOrderInfoVO -> {
                Long orderId = counterOrderInfoVO.getOrderId();
                filterOrderIds.add(orderId);
            });
        }
        if(CollUtil.isNotEmpty(filterOrderIds)){
            form.setFilterOrderIds(filterOrderIds);
        }

        //2.查询未选择的订单list
        List<OrderInfoVO> orderInfoList = counterListInfoMapper.findUnselectedOrderInfo(form);

        return orderInfoList;
    }

    @Override
    public List<OrderInfoVO> findSelectedOrderInfo(OrderInfoQueryForm form) {
        //1.查询-已选择的订单(柜子清单-绑定订单)
        List<OrderInfoVO> orderInfoList = counterListInfoMapper.findSelectedOrderInfo(form);
        return orderInfoList;
    }
}
