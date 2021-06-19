package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.BatchCounterOrderInfoForm;
import com.jayud.mall.model.po.BillOrderRelevance;
import com.jayud.mall.model.po.CounterOrderInfo;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.vo.CounterListInfoVO;
import com.jayud.mall.model.vo.CounterOrderInfoVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IBillOrderRelevanceService;
import com.jayud.mall.service.ICounterOrderInfoService;
import com.jayud.mall.service.IOceanBillService;
import com.jayud.mall.service.IOrderInfoService;
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
    @Autowired
    OceanBillMapper oceanBillMapper;
    @Autowired
    BillOrderRelevanceMapper billOrderRelevanceMapper;

    @Autowired
    IOceanBillService oceanBillService;
    @Autowired
    IBillOrderRelevanceService billOrderRelevanceService;
    @Autowired
    IOrderInfoService orderInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchIntoCounterOrderInfo(BatchCounterOrderInfoForm form) {
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
        Integer orderType = form.getOrderType();
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

            counterOrderInfo.setOrderType(orderType);//订单类型(1普通运单 2留仓运单)
            counterOrderInfoList.add(counterOrderInfo);
        }
        if(CollUtil.isNotEmpty(counterOrderInfoList)){
            //1.批量保存-柜子清单下的订单信息(1清单 -> N运单)
            this.saveOrUpdateBatch(counterOrderInfoList);
        }


        //2.保存-提单关联订单(任务通知表)
        Long billId = counterListInfoVO.getBillId();
        /**
         * 1).根据关系查询：提单关联运单信息  （这里是最新最全的数据）
         * 提单 -->  提单的柜子  -->  柜子清单  -->  柜子的箱子  --> 箱子的订单  --> 订单(运单)
         */
        List<BillOrderRelevance> billOrderList = oceanBillMapper.findBillOrderByBillId(billId);
        /**
         * 2).根据提单id，查询 提单关联订单(任务通知表)  （这里是数据库保存的历史数据）
         */
        List<BillOrderRelevance> billOrderRelevanceList = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
        /**
         * 3).billOrderList 和 billOrderRelevanceList 进行合并，找出删除的数据
         */
        List<Integer> delIds = new ArrayList<>();//要删除的ids
        List<Integer> notDelIds = new ArrayList<>();//不删除的ids
        List<BillOrderRelevance> billOrderRelevances = new ArrayList<>();//要保存的数据
        if(CollUtil.isNotEmpty(billOrderList)){
            for (int i=0; i<billOrderList.size(); i++) {
                BillOrderRelevance billOrder = billOrderList.get(i);
                Integer new_billId = billOrder.getBillId();
                Long new_orderId = billOrder.getOrderId();
                for(int j=0; j<billOrderRelevanceList.size(); j++){
                    BillOrderRelevance billOrderRelevance = billOrderRelevanceList.get(j);
                    Integer db_billId = billOrderRelevance.getBillId();
                    Long db_orderId = billOrderRelevance.getOrderId();
                    if(new_billId.equals(db_billId) && new_orderId.equals(db_orderId) ){
                        billOrder.setId(billOrderRelevance.getId());//主键id 更新数据id
                        billOrder.setIsInform(billOrderRelevance.getIsInform());
                        billOrder.setCreateTime(billOrderRelevance.getCreateTime());
                        //不删除的数据
                        notDelIds.add(billOrderRelevance.getId());
                    }
                }
                billOrderRelevances.add(billOrder);
            }
        }else{
            billOrderRelevanceList.forEach(billOrderRelevance -> {
                //要删除的数据
                delIds.add(billOrderRelevance.getId());
            });
        }
        if(CollUtil.isNotEmpty(delIds)){
            //要删除的ids
            billOrderRelevanceService.removeByIds(delIds);
        }
        if(CollUtil.isNotEmpty(notDelIds)){
            //删除其他的ids，过滤掉 不删除的ids
            QueryWrapper<BillOrderRelevance> qw = new QueryWrapper<>();
            qw.notIn("id", notDelIds);
            qw.eq("bill_id", billId);
            billOrderRelevanceService.remove(qw);
        }
        if(CollUtil.isNotEmpty(billOrderRelevances)){
            //保存的数据
            billOrderRelevanceService.saveOrUpdateBatch(billOrderRelevances);
        }

        //3.保存-修改提单关联运单的订单状态 改为 转运中
        if(CollUtil.isNotEmpty(orderIds)){
            QueryWrapper<OrderInfo> orderInfoQw = new QueryWrapper<>();
            orderInfoQw.in("id", orderIds);
            List<OrderInfo> orderInfoList = orderInfoService.list(orderInfoQw);
            if(CollUtil.isNotEmpty(orderInfoList)){
                orderInfoList.forEach(orderInfo -> {
                    // 将订单状态 从 订单确认 改为 转运中
                    orderInfo.setAfterStatusCode(OrderEnum.AFTER_TRANSIT.getCode());
                    orderInfo.setAfterStatusName(OrderEnum.AFTER_TRANSIT.getName());
                });
                orderInfoService.saveOrUpdateBatch(orderInfoList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveCounterOrderInfo(BatchCounterOrderInfoForm form) {
        Long bId = form.getCounterListInfoId();//柜子清单信息表(counter_list_info id)
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(bId);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在，不能操作");
        }
        List<Long> orderIds = form.getOrderIds();
        if (CollUtil.isEmpty(orderIds)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有选择订单，不能操作");
        }
        QueryWrapper<CounterOrderInfo> counterOrderInfoQueryWrapper = new QueryWrapper<>();
        counterOrderInfoQueryWrapper.eq("b_id", bId);
        counterOrderInfoQueryWrapper.in("order_id", orderIds);
        //1.批量移除-柜子清单下的订单信息(1清单 -> N运单)
        this.remove(counterOrderInfoQueryWrapper);

        //2.保存-提单关联订单(任务通知表)
        Long billId = counterListInfoVO.getBillId();
        /**
         * 1).根据关系查询：提单关联运单信息  （这里是最新最全的数据）
         * 提单 -->  提单的柜子  -->  柜子清单  -->  柜子的箱子  --> 箱子的订单  --> 订单(运单)
         */
        List<BillOrderRelevance> billOrderList = oceanBillMapper.findBillOrderByBillId(billId);
        /**
         * 2).根据提单id，查询 提单关联订单(任务通知表)  （这里是数据库保存的历史数据）
         */
        List<BillOrderRelevance> billOrderRelevanceList = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
        /**
         * 3).billOrderList 和 billOrderRelevanceList 进行合并，找出删除的数据
         */
        List<Integer> delIds = new ArrayList<>();//要删除的ids
        List<Integer> notDelIds = new ArrayList<>();//不删除的ids
        List<BillOrderRelevance> billOrderRelevances = new ArrayList<>();//要保存的数据
        if(CollUtil.isNotEmpty(billOrderList)){
            for (int i=0; i<billOrderList.size(); i++) {
                BillOrderRelevance billOrder = billOrderList.get(i);
                Integer new_billId = billOrder.getBillId();
                Long new_orderId = billOrder.getOrderId();
                for(int j=0; j<billOrderRelevanceList.size(); j++){
                    BillOrderRelevance billOrderRelevance = billOrderRelevanceList.get(j);
                    Integer db_billId = billOrderRelevance.getBillId();
                    Long db_orderId = billOrderRelevance.getOrderId();
                    if(new_billId.equals(db_billId) && new_orderId.equals(db_orderId) ){
                        billOrder.setId(billOrderRelevance.getId());//主键id 更新数据id
                        billOrder.setIsInform(billOrderRelevance.getIsInform());
                        billOrder.setCreateTime(billOrderRelevance.getCreateTime());
                        //不删除的数据
                        notDelIds.add(billOrderRelevance.getId());
                    }
                }
                billOrderRelevances.add(billOrder);
            }
        }else{
            billOrderRelevanceList.forEach(billOrderRelevance -> {
                //要删除的数据
                delIds.add(billOrderRelevance.getId());
            });
        }
        if(CollUtil.isNotEmpty(delIds)){
            //要删除的ids
            billOrderRelevanceService.removeByIds(delIds);
        }
        if(CollUtil.isNotEmpty(notDelIds)){
            //删除其他的ids，过滤掉 不删除的ids
            QueryWrapper<BillOrderRelevance> qw = new QueryWrapper<>();
            qw.notIn("id", notDelIds);
            qw.eq("bill_id", billId);
            billOrderRelevanceService.remove(qw);
        }
        if(CollUtil.isNotEmpty(billOrderRelevances)){
            //保存的数据
            billOrderRelevanceService.saveOrUpdateBatch(billOrderRelevances);
        }

    }

    @Override
    public List<CounterOrderInfoVO> findCounterOrderInfoByBid(Long bId) {
        List<CounterOrderInfoVO> counterOrderInfoList = counterOrderInfoMapper.findCounterOrderInfoBybId(bId);

        for (int i=0; i<counterOrderInfoList.size(); i++){
            CounterOrderInfoVO counterOrderInfoVO = counterOrderInfoList.get(i);
            Long orderId = counterOrderInfoVO.getOrderId();//订单id(order_info id)
            Long listInfoId = counterOrderInfoVO.getBId();//柜子清单信息表(counter_list_info id)
            //TODO



        }


        return counterOrderInfoList;
    }

}
