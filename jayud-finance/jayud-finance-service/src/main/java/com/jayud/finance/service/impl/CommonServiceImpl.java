package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.feign.FreightAirClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.vo.InputGoodsVO;
import com.jayud.finance.vo.InputOrderAddressVO;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * 处理模板数据
     *
     * @param cmd
     * @param array        原始数据
     * @param mainOrderNos
     * @param type        类型:应收:0,应付:1
     * @return
     */
    @Override
    public JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos, Integer type) {
        Map<String, Object> data = new HashMap<>();
        if (SubOrderSignEnum.KY.getSignOne().equals(cmd)) {
            List<AirOrderTemplate> airOrderTemplate = this.getAirOrderTemplate(mainOrderNos);
            data = airOrderTemplate.stream().collect(Collectors.toMap(AirOrderTemplate::getOrderNo, e -> e));
        }

        //TODO 中港地址截取6个字符
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String startAddress = jsonObject.getStr("startAddress");
                String endAddress = jsonObject.getStr("endAddress");
                jsonObject.put("startAddress", StringUtils.isEmpty(startAddress)?"":startAddress.substring(0,6));
                jsonObject.put("endAddress", StringUtils.isEmpty(endAddress)?"":startAddress.substring(0,6));
            }
        }


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            if (data.size() == 0) {
                break;
            }
            JSONObject jsonObject = array.getJSONObject(i);
            JSONObject object = new JSONObject(data.get(jsonObject.getStr("subOrderNo")));
            //客户字段 应收:结算单位 应付:供应商
            if (type == 0) {
                object.put("customerName", jsonObject.getStr("unitAccount"));
            }else {
                object.put("customerName", jsonObject.getStr("supplierChName"));
            }

            object.putAll(jsonObject);
            jsonArray.add(object);
        }
        return jsonArray.size() == 0 ? array : jsonArray;
    }
}
