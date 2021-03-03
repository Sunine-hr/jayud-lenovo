package com.jayud.oms.service.impl;


import com.jayud.common.utils.DateUtils;
import com.jayud.oms.model.po.OrderTypeNumber;
import com.jayud.oms.mapper.OrderTypeNumberMapper;
import com.jayud.oms.service.IOrderTypeNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * <p>
 * 订单单号记录表 服务实现类
 * </p>
 *
 * @author llj
 * @since 2021-02-26
 */
@Service
public class OrderTypeNumberServiceImpl extends ServiceImpl<OrderTypeNumberMapper, OrderTypeNumber> implements IOrderTypeNumberService {

    @Override
    public String getOrderNo(String preOrderNO, String classCode) {
        String orderNo = null;
        if(isFirstDayofMonth(LocalDateTime.now())){
            OrderTypeNumber typeNumber = new OrderTypeNumber();
            typeNumber.setClassCode(classCode);
            typeNumber.setNumber(1);
            String dateData = getDateData(DateUtils.getLocalToStr(LocalDateTime.now()));
            typeNumber.setDate(dateData);
            orderNo = preOrderNO + dateData + String.format("%04d",typeNumber.getNumber());
            baseMapper.insert(typeNumber);
        }else{
           OrderTypeNumber orderTypeNumber = baseMapper.getMaxNumberData(classCode);
           OrderTypeNumber typeNumber = new OrderTypeNumber();
           if(orderTypeNumber != null){
               orderNo = preOrderNO + orderTypeNumber.getDate() + String.format("%04d",orderTypeNumber.getNumber()+1);
               typeNumber.setNumber(orderTypeNumber.getNumber()+1);
               typeNumber.setDate(orderTypeNumber.getDate());
               typeNumber.setClassCode(classCode);
               baseMapper.insert(typeNumber);
           }else{
               typeNumber.setClassCode(classCode);
               typeNumber.setNumber(1);
               String dateData = getDateData(DateUtils.getLocalToStr(LocalDateTime.now()));
               typeNumber.setDate(dateData);
               orderNo = preOrderNO + dateData + String.format("%04d",typeNumber.getNumber());
               baseMapper.insert(typeNumber);
           }
        }
        return orderNo;
    }

    //判断是否为本月第一天
    public boolean isFirstDayofMonth(LocalDateTime localDateTime){
        int dayOfMonth = localDateTime.getDayOfMonth();
        return dayOfMonth == 1;
    }

    //处理时间信息
    public static String getDateData(String date){
        String s = date.substring(0, 10).replaceAll("-", "");
        return s.substring(2,6);
    }

}
