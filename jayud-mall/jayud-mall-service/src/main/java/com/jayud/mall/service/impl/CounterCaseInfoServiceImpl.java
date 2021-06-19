package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.CounterCaseInfoMapper;
import com.jayud.mall.mapper.CounterListInfoMapper;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.model.bo.BatchCounterCaseInfoForm;
import com.jayud.mall.model.bo.OrderCaseQueryForm;
import com.jayud.mall.model.po.CounterCaseInfo;
import com.jayud.mall.model.vo.CounterCaseInfoVO;
import com.jayud.mall.model.vo.CounterListInfoVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.ICounterCaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 柜子箱号信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-28
 */
@Service
public class CounterCaseInfoServiceImpl extends ServiceImpl<CounterCaseInfoMapper, CounterCaseInfo> implements ICounterCaseInfoService {

    @Autowired
    CounterCaseInfoMapper counterCaseInfoMapper;
    @Autowired
    CounterListInfoMapper counterListInfoMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;

    @Override
    public List<OrderCaseVO> findUnselectedOrderCase(OrderCaseQueryForm form) {
        Long bId = form.getCounterListInfoId();//柜子清单信息表(counter_list_info id)
        Long orderId = form.getOrderId();
        List<Long> filterCaseIds = new ArrayList<>();
        //List<CounterCaseInfoVO> counterCaseInfoList = counterCaseInfoMapper.findCounterCaseInfoByBidAndOrderId(bId, orderId);
        List<CounterCaseInfoVO> counterCaseInfoList = counterCaseInfoMapper.findCounterCaseInfoByOrderId(orderId);
        if(CollUtil.isNotEmpty(counterCaseInfoList)){
            counterCaseInfoList.forEach(counterCaseInfoVO -> {
                Long caseId = counterCaseInfoVO.getCaseId();
                filterCaseIds.add(caseId);
            });
        }
        if(CollUtil.isNotEmpty(filterCaseIds)){
            form.setFilterCaseIds(filterCaseIds);
        }
        //查询-未选择的箱子(运单-绑定装柜箱子)
        List<OrderCaseVO> orderCaseList = counterCaseInfoMapper.findUnselectedOrderCase(form);
        return orderCaseList;
    }

    @Override
    public List<OrderCaseVO> findSelectedOrderCase(OrderCaseQueryForm form) {
        //查询-已选择的箱子(运单-绑定装柜箱子)
        List<OrderCaseVO> orderCaseList = counterCaseInfoMapper.findSelectedOrderCase(form);
        return orderCaseList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchIntoCounterCaseInfo(BatchCounterCaseInfoForm form) {
        //批量移入(运单-绑定装柜箱子)
        Long bId = form.getCounterListInfoId();//柜子清单信息表(counter_list_info id)
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(bId);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在，不能操作");
        }
        Long orderId = form.getOrderId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在，不能操作");
        }
        List<Long> caseIds = form.getCaseIds();
        if (CollUtil.isEmpty(caseIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有选择箱子，不能操作");
        }
        List<CounterCaseInfo> counterCaseInfoList = new ArrayList<>();
        for (int i=0; i<caseIds.size(); i++){
            Long caseId = caseIds.get(i);
            OrderCaseVO orderCaseVO = orderCaseMapper.findOrderCaseById(caseId);
            if(ObjectUtil.isEmpty(orderCaseVO)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "箱子不存在，不能操作");
            }
            CounterCaseInfo counterCaseInfo = new CounterCaseInfo();
            counterCaseInfo.setBId(counterListInfoVO.getId());
            counterCaseInfo.setBName(counterListInfoVO.getFileName());
            counterCaseInfo.setBillId(counterListInfoVO.getBillId().intValue());
            counterCaseInfo.setBillNo(counterListInfoVO.getBillNo());
            counterCaseInfo.setOrderId(orderInfoVO.getId());
            counterCaseInfo.setOrderNo(orderInfoVO.getOrderNo());
            counterCaseInfo.setCaseId(orderCaseVO.getId());
            counterCaseInfo.setCartonNo(orderCaseVO.getCartonNo());
            counterCaseInfoList.add(counterCaseInfo);
        }
        if(CollUtil.isNotEmpty(counterCaseInfoList)){
            this.saveOrUpdateBatch(counterCaseInfoList);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveCounterCaseInfo(BatchCounterCaseInfoForm form) {
        //批量移除(运单-绑定装柜箱子)
        Long bId = form.getCounterListInfoId();
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(bId);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在，不能操作");
        }
        Long orderId = form.getOrderId();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在，不能操作");
        }
        List<Long> caseIds = form.getCaseIds();
        if (CollUtil.isEmpty(caseIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有选择箱子，不能操作");
        }
        QueryWrapper<CounterCaseInfo> qw = new QueryWrapper<>();
        qw.eq("b_id", bId);
        qw.eq("order_id", orderId);
        qw.in("case_id", caseIds);
        this.remove(qw);

    }

}
