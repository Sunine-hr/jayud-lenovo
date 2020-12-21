package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.tms.model.po.OrderSendCars;
import com.jayud.tms.mapper.OrderSendCarsMapper;
import com.jayud.tms.model.vo.*;
import com.jayud.tms.service.IOrderSendCarsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单派车信息 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderSendCarsServiceImpl extends ServiceImpl<OrderSendCarsMapper, OrderSendCars> implements IOrderSendCarsService {

    @Override
    public OrderSendCarsVO getOrderSendInfo(String orderNo) {
        return baseMapper.getOrderSendInfo(orderNo);
    }

    @Override
    public int getDriverPendingOrderNum(Long driverId, List<String> orderNos) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getDriverInfoId, driverId);
        if (CollectionUtils.isNotEmpty(orderNos)) {
            condition.lambda().notIn(OrderSendCars::getOrderNo, orderNos);
        }
        return this.count(condition);
    }

    /**
     * 根据订单编号获取
     */
    @Override
    public OrderSendCars getOrderSendCarsByOrderNo(String orderNo) {
        QueryWrapper<OrderSendCars> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderSendCars::getOrderNo, orderNo);
        return this.baseMapper.selectOne(condition);
    }

    @Override
    public DriverInfoPdfVO initDriverInfo(String orderNo) {
        return baseMapper.initDriverInfo(orderNo);
    }

    @Override
    public SendCarListPdfVO initSendCarList(String orderNo) {
        List<SendCarListTempVO> tempList = baseMapper.initSendCarList(orderNo);
        SendCarListPdfVO sendCarListPdfVO = new SendCarListPdfVO();
        if(tempList != null && tempList.size() > 0){
            sendCarListPdfVO.setLegalName(tempList.get(0).getLegalName());
            sendCarListPdfVO.setLegalEnName(tempList.get(0).getLegalEnName());
            sendCarListPdfVO.setJobNumber(tempList.get(0).getJobNumber());
            sendCarListPdfVO.setCreateTimeStr(tempList.get(0).getCreateTimeStr());
            List<SendCarListVO> sendCarListVOList = ConvertUtil.convertList(tempList,SendCarListVO.class);
            sendCarListPdfVO.setSendCarListVOList(sendCarListVOList);
        }
        return sendCarListPdfVO;
    }


}
