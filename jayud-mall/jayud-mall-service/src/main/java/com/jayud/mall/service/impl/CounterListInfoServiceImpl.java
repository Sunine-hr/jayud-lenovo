package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OrderInfoQueryForm;
import com.jayud.mall.model.po.CounterListInfo;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.ICounterListInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderPickMapper orderPickMapper;



    @Override
    public CounterListInfoVO findCounterListInfoById(Long id) {
        return counterListInfoMapper.findCounterListInfoById(id);
    }

    @Override
    public List<CounterCaseInfoVO> findCounterCaseInfo(Long b_id) {
        return counterListInfoMapper.findCounterCaseInfo(b_id);
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

    @Override
    public CounterListExcelVO findCounterListExcelById(Long id) {
        CounterListExcelVO counterListExcelVO = counterListInfoMapper.findCounterListExcelById(id);
        if(ObjectUtil.isEmpty(counterListExcelVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在");
        }
        Long listInfoId = counterListExcelVO.getListInfoId();//柜子清单id 柜子清单信息表(counter_list_info id)
        List<CounterOrderInfoExcelVO> counterOrderInfoExcelList = counterOrderInfoMapper.findCounterOrderInfoExcelBybid(listInfoId);
        String warehouseNo = "";//进仓编号，多个
        String fabNo = "";//FBA号，多个
        String cartons = "";//箱数
        String weight = "";//重量
        String volume = "";//体积
        for (int i=0; i<counterOrderInfoExcelList.size(); i++){
            CounterOrderInfoExcelVO counterOrderInfoExcelVO = counterOrderInfoExcelList.get(i);
            Long orderId = counterOrderInfoExcelVO.getOrderId();
            OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
            Integer isPick = orderInfoVO.getIsPick();//是否上门提货(0否 1是,order_pick)
            if(ObjectUtil.isNotEmpty(isPick) && isPick.equals(1)){
                List<OrderPickVO> orderPickList = orderPickMapper.findOrderPickByOrderId(orderId);
                for (int j=0; j<orderPickList.size(); j++){
                    OrderPickVO orderPickVO = orderPickList.get(j);
                    String warehouseNo1 = orderPickVO.getWarehouseNo();
                    if(j==0){
                        warehouseNo = warehouseNo1;
                    }else{
                        warehouseNo += ","+warehouseNo1;
                    }
                }
            }else{
                warehouseNo = orderInfoVO.getWarehouseNo();
            }

            List<CounterCaseInfoVO> counterCaseInfoList = counterCaseInfoMapper.findCounterCaseInfoByBidAndOrderId(listInfoId, orderId);
            int size = counterCaseInfoList.size();
            cartons =  Integer.toString(size);

            BigDecimal w = new BigDecimal("0");
            BigDecimal v = new BigDecimal("0");
            for (int k=0; k<counterCaseInfoList.size(); k++){
                CounterCaseInfoVO counterCaseInfoVO = counterCaseInfoList.get(k);
                String fabNo1 = counterCaseInfoVO.getFabNo();
                BigDecimal weight1 = counterCaseInfoVO.getAsnWeight();
                BigDecimal volume1 = counterCaseInfoVO.getAsnVolume();
                if(k == 0){
                    fabNo = fabNo1;
                }else{
                    fabNo += ","+fabNo1;
                }
                w = w.add(weight1);//重量
                v = v.add(volume1);//体积
            }
            weight = w.toString();
            volume = v.toString();
            counterOrderInfoExcelVO.setWarehouseNo(warehouseNo);
            counterOrderInfoExcelVO.setFabNo(fabNo);
            counterOrderInfoExcelVO.setCartons(cartons);
            counterOrderInfoExcelVO.setWeight(weight);
            counterOrderInfoExcelVO.setVolume(volume);

            int serialNumber = i+1;
            counterOrderInfoExcelVO.setSerialNumber(Integer.toString(serialNumber));//序号
        }
        counterListExcelVO.setCounterOrderInfoExcelList(counterOrderInfoExcelList);

        BigDecimal totalCartons = new BigDecimal("0");
        BigDecimal totalWeight = new BigDecimal("0");
        BigDecimal totalVolume = new BigDecimal("0");
        for (int l=0; l<counterOrderInfoExcelList.size(); l++){
            CounterOrderInfoExcelVO counterOrderInfoExcelVO = counterOrderInfoExcelList.get(l);
            BigDecimal cartons1 = ObjectUtil.isNotEmpty(counterOrderInfoExcelVO.getCartons()) ? new BigDecimal(counterOrderInfoExcelVO.getCartons()) : new BigDecimal("0");
            BigDecimal weight1 = ObjectUtil.isNotEmpty(counterOrderInfoExcelVO.getWeight()) ? new BigDecimal(counterOrderInfoExcelVO.getWeight()) : new BigDecimal("0");
            BigDecimal volume1 = ObjectUtil.isNotEmpty(counterOrderInfoExcelVO.getVolume()) ? new BigDecimal(counterOrderInfoExcelVO.getVolume()) : new BigDecimal("0");
            totalCartons = totalCartons.add(cartons1);
            totalWeight = totalWeight.add(weight1);
            totalVolume = totalVolume.add(volume1);
        }
        counterListExcelVO.setTotalCartons(totalCartons.toString());
        counterListExcelVO.setTotalWeight(totalWeight.toString());
        counterListExcelVO.setTotalVolume(totalVolume.toString());
        return counterListExcelVO;
    }
}