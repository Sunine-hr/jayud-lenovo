package com.jayud.oceanship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.oceanship.bo.AddGoodsForm;
import com.jayud.oceanship.bo.AddOrderAddressForm;
import com.jayud.oceanship.bo.AddSeaOrderForm;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.OrderFlowSheet;
import com.jayud.oceanship.po.OrderStatus;
import com.jayud.oceanship.po.SeaOrder;
import com.jayud.oceanship.mapper.SeaOrderMapper;
import com.jayud.oceanship.service.IOrderFlowSheetService;
import com.jayud.oceanship.service.IOrderStatusService;
import com.jayud.oceanship.service.ISeaOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 海运订单表 服务实现类
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Service
public class SeaOrderServiceImpl extends ServiceImpl<SeaOrderMapper, SeaOrder> implements ISeaOrderService {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IOrderFlowSheetService orderFlowSheetService;

    @Autowired
    private IOrderStatusService orderStatusService;

    @Override
    @Transactional
    public void createOrder(AddSeaOrderForm addSeaOrderForm) {
        LocalDateTime now = LocalDateTime.now();
        SeaOrder seaOrder = ConvertUtil.convert(addSeaOrderForm, SeaOrder.class);
        //创建海运单
        if (addSeaOrderForm.getId() == null) {
            //生成订单号
            String orderNo = generationOrderNo();
            seaOrder.setOrderNo(orderNo);
            seaOrder.setCreateTime(now);
            seaOrder.setCreateUser(UserOperator.getToken());
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            this.save(seaOrder);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("class_code","HY");
            queryWrapper.isNotNull("sub_sorts");
            queryWrapper.orderByAsc("sub_sorts");
            List<OrderStatus> statuses = orderStatusService.list(queryWrapper);
            for (int i = 0; i < statuses.size(); i++) {
                OrderFlowSheet orderFlowSheet = new OrderFlowSheet();
                orderFlowSheet.setMainOrderNo(seaOrder.getMainOrderNo());
                orderFlowSheet.setOrderNo(seaOrder.getOrderNo());
                orderFlowSheet.setProductClassifyId(statuses.get(i).getClassCode());
                orderFlowSheet.setProductClassifyName(statuses.get(i).getClassName());
                orderFlowSheet.setStatus(statuses.get(i).getName());
                orderFlowSheet.setStatusName(statuses.get(i).getContainState());
                if(i==0){
                    orderFlowSheet.setComplete("1");
                    orderFlowSheet.setFStatus(null);
                }else{
                    orderFlowSheet.setComplete("0");
                    orderFlowSheet.setFStatus(statuses.get(i-1).getContainState());
                }

                orderFlowSheet.setIsPass("1");
                orderFlowSheet.setCreateTime(now);
                orderFlowSheet.setCreateUser(seaOrder.getCreateUser());
                orderFlowSheetService.saveOrUpdate(orderFlowSheet);
            }
        } else {
            //修改海运单
            seaOrder.setStatus(OrderStatusEnum.SEA_S_0.getCode());
            seaOrder.setUpdateTime(now);
            seaOrder.setUpdateUser(UserOperator.getToken());
            this.updateById(seaOrder);
        }
        //获取用户地址
        List<AddOrderAddressForm> orderAddressForms = addSeaOrderForm.getOrderAddressForms();
        for (AddOrderAddressForm orderAddressForm : orderAddressForms) {
            orderAddressForm.setOrderNo(seaOrder.getOrderNo());
            orderAddressForm.setBusinessType(BusinessTypeEnum.HY.getCode());
            orderAddressForm.setBusinessId(seaOrder.getId());
            orderAddressForm.setCreateTime(LocalDateTime.now());
        }
        //批量保存用户地址
        ApiResult result = this.omsClient.saveOrUpdateOrderAddressBatch(orderAddressForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改订单地址信息失败,订单地址信息={}");//, new JSONArray(orderAddressForms));
        }

        List<AddGoodsForm> goodsForms = addSeaOrderForm.getGoodsForms();
        for (AddGoodsForm goodsForm : goodsForms) {
            goodsForm.setOrderNo(seaOrder.getOrderNo());
            goodsForm.setBusinessId(seaOrder.getId());
            goodsForm.setBusinessType(BusinessTypeEnum.KY.getCode());
        }
        //批量保存货物信息
        result = this.omsClient.saveOrUpdateGoodsBatch(goodsForms);
        if (result.getCode() != HttpStatus.SC_OK) {
            log.warn("批量保存/修改商品信息失败,商品信息={}");//, new JSONArray(goodsForms));
        }

    }

    /**
     * 生成订单号
     */
    @Override
    public String generationOrderNo() {
        //生成订单号
        String orderNo = StringUtils.loadNum(CommonConstant.S, 12);
        while (true) {
            if (isExistOrder(orderNo)) {//重复
                orderNo = StringUtils.loadNum(CommonConstant.S, 12);
            } else {
                break;
            }
        }
        return orderNo;
    }

    /**
     * 是否存在订单
     */
    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(SeaOrder::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }

    @Override
    public SeaOrder getByMainOrderNO(String orderNo) {
        QueryWrapper<SeaOrder> condition = new QueryWrapper<>();
        condition.lambda().eq(SeaOrder::getMainOrderNo, orderNo);
        return this.getOne(condition);
    }
}
