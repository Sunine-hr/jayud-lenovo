package com.jayud.finance.service.impl;

import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.feign.FreightAirClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.vo.InputGoodsVO;
import com.jayud.finance.vo.InputOrderAddressVO;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 公共处理类
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private FreightAirClient freightAirClient;
    @Autowired
    private OmsClient omsClient;

    /**
     * 获取空运明细
     */
    @Override
    public List<AirOrderTemplate> getAirOrderTemplate(List<String> mainOrderNos) {
        Object data = freightAirClient.getAirOrderByMainOrderNos(mainOrderNos).getData();
        List<AirOrderTemplate> airOrderTemplates = Utilities.obj2List(data, AirOrderTemplate.class);
        List<Long> airOrderIds = airOrderTemplates.stream().map(AirOrderTemplate::getId).collect(Collectors.toList());
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        //查询商品信息
        List<InputGoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
//        List<InputOrderAddressVO> orderAddressVOS = this.omsClient.getOrderAddressByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
        for (AirOrderTemplate record : airOrderTemplates) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装地址
//            record.assemblyOrderAddress(orderAddressVOS);
        }
        return airOrderTemplates;
    }
}
