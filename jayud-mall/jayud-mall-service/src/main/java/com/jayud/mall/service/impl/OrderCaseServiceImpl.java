package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.OfferInfoMapper;
import com.jayud.mall.mapper.OrderCaseMapper;
import com.jayud.mall.model.bo.CreateOrderCaseForm;
import com.jayud.mall.model.bo.QueryOrderCaseForm;
import com.jayud.mall.model.po.OrderCase;
import com.jayud.mall.model.vo.OfferInfoVO;
import com.jayud.mall.model.vo.OrderCaseVO;
import com.jayud.mall.service.IOrderCaseService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单对应箱号信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OrderCaseServiceImpl extends ServiceImpl<OrderCaseMapper, OrderCase> implements IOrderCaseService {

    @Autowired
    OrderCaseMapper orderCaseMapper;
    @Autowired
    OfferInfoMapper offerInfoMapper;

//    @Autowired
//    INumberGeneratedService numberGeneratedService;

    @Override
    public IPage<OrderCaseVO> findOrderCaseByPage(QueryOrderCaseForm form) {
        //定义分页参数
        Page<OrderCaseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<OrderCaseVO> pageInfo = orderCaseMapper.findOrderCaseByPage(page, form);
        return pageInfo;

    }

    @Override
    public List<OrderCaseVO> createOrderCaseList(CreateOrderCaseForm form) {
        Integer cartons = form.getCartons();// 总箱数
        BigDecimal weight = form.getWeight();// 每箱重量(KG) 实际重
        BigDecimal length = form.getLength();// 长(cm)
        BigDecimal width = form.getWidth();// 宽(cm)
        BigDecimal height = form.getHeight();// 高(cm)

        Integer offerInfoId = form.getOfferInfoId();
        if(ObjectUtil.isEmpty(offerInfoId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价id为空");
        }
        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));
        if(ObjectUtil.isEmpty(offerInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价不存在");
        }
        //计泡系数(默认6000)
        BigDecimal bubbleCoefficient = (offerInfoVO.getBubbleCoefficient() == null) ? new BigDecimal("6000") : offerInfoVO.getBubbleCoefficient();



        //体积(m3) = (长cm * 宽cm * 高cm) / 1000000
        BigDecimal volume = length.multiply(width).multiply(height).divide(new BigDecimal("1000000"),2, BigDecimal.ROUND_HALF_UP);
        //材积重(CBM) = (长cm * 宽cm * 高cm) / 计泡系数
        BigDecimal volumeWeight = length.multiply(width).multiply(height).divide(bubbleCoefficient,2, BigDecimal.ROUND_HALF_UP);
        //收费重 ，比较实际重和材积重的大小，谁大取谁 chargeWeight
        BigDecimal chargeWeight = new BigDecimal("0");
        if(weight.compareTo(volumeWeight) == 1){
            //weight > volumeWeight
            chargeWeight = weight;
        }else{
            chargeWeight = volumeWeight;
        }

        List<OrderCaseVO> list = new ArrayList<>();
        for(int i=0; i<cartons; i++) {
            OrderCaseVO orderCaseVO = new OrderCaseVO();
            String cartonNO = NumberGeneratedUtils.getOrderNoByCode2("case_number");
            orderCaseVO.setCartonNo(cartonNO);
            orderCaseVO.setAsnWeight(weight);//实际重
            orderCaseVO.setAsnLength(length);
            orderCaseVO.setAsnWidth(width);
            orderCaseVO.setAsnHeight(height);
            orderCaseVO.setAsnVolume(volume);
            orderCaseVO.setVolumeWeight(volumeWeight);//材积重
            orderCaseVO.setChargeWeight(chargeWeight);//收费重
            list.add(orderCaseVO);
        }
        return list;
    }
}
