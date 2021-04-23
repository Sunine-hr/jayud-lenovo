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
        OrderTypeNumber orderTypeNumber = baseMapper.getMaxNumberData(classCode, getDateData(DateUtils.getLocalToStr(LocalDateTime.now())));
        OrderTypeNumber typeNumber = new OrderTypeNumber();
        if (isFirstDayofMonth(LocalDateTime.now())) {
            if (orderTypeNumber != null) {
                orderNo = preOrderNO + orderTypeNumber.getDate() + String.format("%04d", orderTypeNumber.getNumber() + 1);
                typeNumber.setNumber(orderTypeNumber.getNumber() + 1);
                typeNumber.setDate(orderTypeNumber.getDate());
                typeNumber.setClassCode(classCode);
                baseMapper.insert(typeNumber);
            } else {
                typeNumber.setClassCode(classCode);
                typeNumber.setNumber(1);
                String dateData = getDateData(DateUtils.getLocalToStr(LocalDateTime.now()));
                typeNumber.setDate(dateData);
                orderNo = preOrderNO + dateData + String.format("%04d", typeNumber.getNumber());
                baseMapper.insert(typeNumber);
            }

        } else {

            if (orderTypeNumber != null) {
                orderNo = preOrderNO + orderTypeNumber.getDate() + String.format("%04d", orderTypeNumber.getNumber() + 1);
                typeNumber.setNumber(orderTypeNumber.getNumber() + 1);
                typeNumber.setDate(orderTypeNumber.getDate());
                typeNumber.setClassCode(classCode);
                baseMapper.insert(typeNumber);
            } else {
                typeNumber.setClassCode(classCode);
                typeNumber.setNumber(1);
                String dateData = getDateData(DateUtils.getLocalToStr(LocalDateTime.now()));
                typeNumber.setDate(dateData);
                orderNo = preOrderNO + dateData + String.format("%04d", typeNumber.getNumber());
                baseMapper.insert(typeNumber);
            }
        }
        return orderNo;
    }

    @Override
    public String getWarehouseNumber(String preOrder) {
        String orderNo = null;
        OrderTypeNumber orderTypeNumber = baseMapper.getMaxNumberData(preOrder, getDateData(DateUtils.getLocalToStr(LocalDateTime.now())));
        OrderTypeNumber typeNumber = new OrderTypeNumber();
        String dateData2 = getDateData2(DateUtils.getLocalToStr(LocalDateTime.now()), 2, 8);
        if (orderTypeNumber != null) {
            if (orderTypeNumber.getDate().equals(dateData2)) {
                typeNumber.setClassCode(preOrder);
                typeNumber.setDate(orderTypeNumber.getDate());
                typeNumber.setNumber(orderTypeNumber.getNumber() + 1);
                orderNo = preOrder + dateData2 + String.format("%04d", typeNumber.getNumber());
                baseMapper.insert(typeNumber);
            } else {
                typeNumber.setClassCode(preOrder);
                typeNumber.setDate(dateData2);
                typeNumber.setNumber(1);
                orderNo = preOrder + dateData2 + String.format("%04d", typeNumber.getNumber());
                baseMapper.insert(typeNumber);
            }
        }
        return orderNo;
    }

    //判断是否为本月第一天
    public boolean isFirstDayofMonth(LocalDateTime localDateTime) {
        int dayOfMonth = localDateTime.getDayOfMonth();
        return dayOfMonth == 1;
    }

    //处理时间信息
    public static String getDateData(String date) {
        String s = date.substring(0, 10).replaceAll("-", "");
        return s.substring(2, 6);
    }

    public static String getDateData2(String date, int start, int end) {
        String s = date.substring(0, 10).replaceAll("-", "");
        return s.substring(start, end);
    }

}
