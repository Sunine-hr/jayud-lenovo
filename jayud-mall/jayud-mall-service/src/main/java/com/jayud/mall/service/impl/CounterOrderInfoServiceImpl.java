package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.CounterListInfoMapper;
import com.jayud.mall.mapper.CounterOrderInfoMapper;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.model.bo.BtchCounterOrderInfoForm;
import com.jayud.mall.model.po.CounterOrderInfo;
import com.jayud.mall.model.vo.CounterListInfoVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.ICounterOrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜子订单信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
@Service
public class CounterOrderInfoServiceImpl extends ServiceImpl<CounterOrderInfoMapper, CounterOrderInfo> implements ICounterOrderInfoService {

    @Autowired
    CounterOrderInfoMapper counterOrderInfoMapper;
    @Autowired
    CounterListInfoMapper counterListInfoMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchIntoCounterOrderInfo(BtchCounterOrderInfoForm form) {
        //批量移入(柜子清单-绑定订单)
        Long bId = form.getCounterListInfoId();//柜子清单信息表(counter_list_info id)
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(bId);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在，不能操作");
        }
        List<Long> orderIds = form.getOrderIds();
        if (CollUtil.isEmpty(orderIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有选择订单，不能操作");
        }
        List<CounterOrderInfo> counterOrderInfoList = new ArrayList<>();
        for (int i=0; i<orderIds.size(); i++){
            Long orderId = orderIds.get(i);
            OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
            if(ObjectUtil.isEmpty(orderInfoVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在，不能操作");
            }
            CounterOrderInfo counterOrderInfo = new CounterOrderInfo();
            counterOrderInfo.setBId(counterListInfoVO.getId());
            counterOrderInfo.setBName(counterListInfoVO.getFileName());
            counterOrderInfo.setBillId(counterListInfoVO.getBillId().intValue());
            counterOrderInfo.setBillNo(counterListInfoVO.getBillNo());
            counterOrderInfo.setOrderId(orderInfoVO.getId());
            counterOrderInfo.setOrderNo(orderInfoVO.getOrderNo());
            counterOrderInfoList.add(counterOrderInfo);
        }
        if(CollUtil.isNotEmpty(counterOrderInfoList)){
            this.saveOrUpdateBatch(counterOrderInfoList);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveCounterOrderInfo(BtchCounterOrderInfoForm form) {
        //批量移除(柜子清单-绑定订单)
        Long bId = form.getCounterListInfoId();//柜子清单信息表(counter_list_info id)
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(bId);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在，不能操作");
        }
        List<Long> orderIds = form.getOrderIds();
        if (CollUtil.isEmpty(orderIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有选择订单，不能操作");
        }
        QueryWrapper<CounterOrderInfo> qw = new QueryWrapper<>();
        qw.eq("b_id", bId);
        qw.in("order_id", orderIds);
        this.remove(qw);
    }

}
