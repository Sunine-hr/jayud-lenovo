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
import java.util.*;

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
    @Autowired
    OrderCaseMapper orderCaseMapper;



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
        //1.1 过滤 已选择的订单id
        Long bId = form.getCounterListInfoId();
        List<Long> filterOrderIds = new ArrayList<>();
        List<CounterOrderInfoVO> counterOrderInfoList = counterOrderInfoMapper.findCounterOrderInfoBybId(bId);
        if(CollUtil.isNotEmpty(counterOrderInfoList)){
            counterOrderInfoList.forEach(counterOrderInfoVO -> {
                Long orderId = counterOrderInfoVO.getOrderId();
                filterOrderIds.add(orderId);
            });
        }
        //1.2 过滤 订单里面已经没有箱子可用的 订单id
        if(CollUtil.isNotEmpty(filterOrderIds)){
            form.setFilterOrderIds(filterOrderIds);
        }
        List<OrderInfoVO> prepareOrderInfoList = counterListInfoMapper.findUnselectedOrderInfo(form);
        for (int i=0; i<prepareOrderInfoList.size(); i++){
            OrderInfoVO orderInfoVO = prepareOrderInfoList.get(i);
            Long orderId = orderInfoVO.getId();
            //判断 订单id 下面的箱子是否 全部 被 使用了
            List<OrderCaseVO> orderCaseList = orderCaseMapper.findOrderCaseByOrderId(orderId);//查询订单下有多少个箱子 数量
            int a = orderCaseList.size();
            List<CounterCaseInfoVO> counterCaseInfoList = counterCaseInfoMapper.findCounterCaseInfoByOrderId(orderId);//查询订单被用掉的箱子 数量
            int b = counterCaseInfoList.size();
            if(a == b){
                //说明订单的箱子被用完了 后面要过滤掉
                filterOrderIds.add(orderId);
            }
        }
        //2.查询未选择的订单list
        if(CollUtil.isNotEmpty(filterOrderIds)){
            form.setFilterOrderIds(filterOrderIds);
        }
        List<OrderInfoVO> orderInfoList = counterListInfoMapper.findUnselectedOrderInfo(form);
        for(int i=0; i<orderInfoList.size(); i++){
            OrderInfoVO orderInfoVO = orderInfoList.get(i);
            Long orderId = orderInfoVO.getId();
            List<OrderCaseVO> orderCaseList = orderCaseMapper.findOrderCaseByOrderId(orderId);
            int casesTotal = orderCaseList.size();
            BigDecimal casevolumeTotal = new BigDecimal("0");
            BigDecimal caseWeightTotal = new BigDecimal("0");
            for(int j=0; j<orderCaseList.size(); j++){
                OrderCaseVO orderCaseVO = orderCaseList.get(j);
                BigDecimal volume = orderCaseVO.getAsnVolume();
                BigDecimal weight = orderCaseVO.getAsnWeight();
                casevolumeTotal.add(volume);
                caseWeightTotal.add(weight);
            }
            orderInfoVO.setCasesTotal(casesTotal);
            orderInfoVO.setCasevolumeTotal(casevolumeTotal);
            orderInfoVO.setCaseWeightTotal(caseWeightTotal);
        }
        return orderInfoList;
    }

    @Override
    public List<OrderInfoVO> findSelectedOrderInfo(OrderInfoQueryForm form) {
        //1.查询-已选择的订单(柜子清单-绑定订单)
        List<OrderInfoVO> orderInfoList = counterListInfoMapper.findSelectedOrderInfo(form);
        for(int i=0; i<orderInfoList.size(); i++){
            OrderInfoVO orderInfoVO = orderInfoList.get(i);
            Long orderId = orderInfoVO.getId();
            List<OrderCaseVO> orderCaseList = orderCaseMapper.findOrderCaseByOrderId(orderId);
            int casesTotal = orderCaseList.size();
            BigDecimal casevolumeTotal = new BigDecimal("0");
            BigDecimal caseWeightTotal = new BigDecimal("0");
            for(int j=0; j<orderCaseList.size(); j++){
                OrderCaseVO orderCaseVO = orderCaseList.get(j);
                BigDecimal volume = orderCaseVO.getAsnVolume();
                BigDecimal weight = orderCaseVO.getAsnWeight();
                casevolumeTotal.add(volume);
                caseWeightTotal.add(weight);
            }
            orderInfoVO.setCasesTotal(casesTotal);
            orderInfoVO.setCasevolumeTotal(casevolumeTotal);
            orderInfoVO.setCaseWeightTotal(caseWeightTotal);
        }
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

        for (int i=0; i<counterOrderInfoExcelList.size(); i++){
            String warehouseNo = "";//进仓编号，多个
            String amazonReferenceId = "";//亚马逊引用ID
            String extensionNumber = "";//扩展单号,FBA号，多个
            String cartons = "";//箱数
            String weight = "";//重量
            String volume = "";//体积

            Set<String> amazonReferenceIdSet = new HashSet<>();//set去重
            Set<String> extensionNumberSet = new HashSet<>();//set去重

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
                //String fabNo1 = counterCaseInfoVO.getFabNo();
                String amazonReferenceId1 = counterCaseInfoVO.getAmazonReferenceId();
                String extensionNumber1 = counterCaseInfoVO.getExtensionNumber();

                amazonReferenceIdSet.add(amazonReferenceId1);
                extensionNumberSet.add(extensionNumber1);

                BigDecimal weight1 = counterCaseInfoVO.getAsnWeight();
                BigDecimal volume1 = counterCaseInfoVO.getAsnVolume();
                w = w.add(weight1);//重量
                v = v.add(volume1);//体积
            }
            weight = w.toString();
            volume = v.toString();

            Iterator<String> it1 = amazonReferenceIdSet.iterator();
            int aa = 0;
            while(it1.hasNext()){
                String next = it1.next();
                if(aa == 0){
                    amazonReferenceId = next;
                }else{
                    amazonReferenceId += ","+next;
                }
                aa++;
            }

            Iterator<String> it2 = extensionNumberSet.iterator();
            int bb = 0;
            while(it2.hasNext()){
                String next = it2.next();
                if(bb == 0){
                    extensionNumber = next;
                }else{
                    extensionNumber += ","+next;
                }
                bb++;
            }

            counterOrderInfoExcelVO.setWarehouseNo(warehouseNo);
            counterOrderInfoExcelVO.setAmazonReferenceId(amazonReferenceId);
            counterOrderInfoExcelVO.setExtensionNumber(extensionNumber);
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