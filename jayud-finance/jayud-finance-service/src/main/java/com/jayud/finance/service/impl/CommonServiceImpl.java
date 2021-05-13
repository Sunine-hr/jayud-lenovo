package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.feign.FreightAirClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.feign.TmsClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.vo.InputGoodsVO;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import com.jayud.finance.vo.template.order.TmsOrderTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private TmsClient tmsClient;
    @Autowired
    private OmsClient omsClient;

    /**
     * 获取空运明细
     */
    @Override
    public Map<String, Object> getAirOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = freightAirClient.getAirOrderByMainOrderNos(mainOrderNos).getData();
        List<AirOrderTemplate> airOrderTemplates = Utilities.obj2List(data, AirOrderTemplate.class);
        List<Long> airOrderIds = airOrderTemplates.stream().map(AirOrderTemplate::getId).collect(Collectors.toList());
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        //查询商品信息
        List<InputGoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
//        List<InputOrderAddressVO> orderAddressVOS = this.omsClient.getOrderAddressByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
        Map<String, Object> map = new HashMap<>();
        for (AirOrderTemplate record : airOrderTemplates) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            record.setGoodTime(DateUtils.format(record.getGoodTime(), "yyyy-MM-dd"));
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            map.put(cmd.equals("main") ? record.getMainOrderNo() : record.getOrderNo(), record);
            //组装地址
//            record.assemblyOrderAddress(orderAddressVOS);
        }
        return map;
    }

    /**
     * 获取空运明细
     */
    public Map<String, Object> getTmsOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = this.tmsClient.getTmsOrderInfoByMainOrderNos(mainOrderNos).getData();
        JSONArray array = new JSONArray(data);
        Map<String, Object> map = new HashMap<>();
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            switch (templateEnum) {
                case ZGYS:
                    TmsOrderTemplate tmsOrderTemplate = ConvertUtil.convert(jsonObject, TmsOrderTemplate.class);
                    tmsOrderTemplate.assembleData(jsonObject);
                    //组装商品信息
                    tmsOrderTemplate.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? tmsOrderTemplate.getMainOrderNo() : tmsOrderTemplate.getOrderNo(), tmsOrderTemplate);
                    break;
                case ZGYS_ONE:
                    break;

            }

        }

        return map;
    }

    /**
     * 处理模板数据
     *
     * @param cmd
     * @param array        原始数据
     * @param mainOrderNos
     * @param type         类型:应收:0,应付:1
     * @return
     */
    @Override
    public JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos, Integer type) {
        Map<String, Object> data = new HashMap<>();

        BillTemplateEnum templateEnum = BillTemplateEnum.getTemplateEnum(cmd);
        if (templateEnum != null) {
            switch (templateEnum) {
                case KY: //空运
                    data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
                    break;
                case ZGYS: //中港
                case ZGYS_ONE:
                    data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
                    break;

            }
        }


        //TODO 中港地址截取6个字符
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String startAddress = jsonObject.getStr("startAddress");
                String endAddress = jsonObject.getStr("endAddress");
                if (startAddress != null && startAddress.length() > 6) {
                    jsonObject.put("startAddress", startAddress.substring(0, 6));
                }
                if (endAddress != null && endAddress.length() > 6) {
                    jsonObject.put("endAddress", endAddress.substring(0, 6));
                }
            }
        }


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            if (data.size() == 0) {
                break;
            }
            JSONObject jsonObject = array.getJSONObject(i);

            String orderNosKey = cmd.equals(SubOrderSignEnum.MAIN.getSignOne()) ? "orderNo" : "subOrderNo";
            JSONObject object = new JSONObject(data.get(jsonObject.getStr(orderNosKey)));
            //客户字段 应收:结算单位 应付:供应商 TODO 产品核对过,直接取主订单客户名称
//            if (type == 0) {
//                object.put("customerName", jsonObject.getStr("unitAccount"));
//            } else {
//                object.put("customerName", jsonObject.getStr("supplierChName"));
//            }

            object.putAll(jsonObject);
            jsonArray.add(object);
        }
        return jsonArray.size() == 0 ? array : jsonArray;
    }


    /**
     * 处理模板数据
     *
     * @param cmd
     * @param array        原始数据
     * @param mainOrderNos
     * @param type         类型:应收:0,应付:1
     * @return
     */
    @Override
    public JSONArray templateDataProcessing(String cmd, String templateCmd, JSONArray array, List<String> mainOrderNos, Integer type) {
        Map<String, Object> data = new HashMap<>();

        BillTemplateEnum templateEnum = BillTemplateEnum.getTemplateEnum(templateCmd);
        if (templateEnum != null) {


            switch (templateEnum) {
                case KY: //空运
                    data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
                    break;
                case ZGYS: //中港
                case ZGYS_ONE:
                    data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
                    break;
            }
        }


        //TODO 中港地址截取6个字符
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String startAddress = jsonObject.getStr("startAddress");
                String endAddress = jsonObject.getStr("endAddress");
                if (startAddress != null && startAddress.length() > 6) {
                    jsonObject.put("startAddress", startAddress.substring(0, 6));
                }
                if (endAddress != null && endAddress.length() > 6) {
                    jsonObject.put("endAddress", endAddress.substring(0, 6));
                }
            }
        }


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            if (data.size() == 0) {
                break;
            }
            JSONObject jsonObject = array.getJSONObject(i);

            String orderNosKey = cmd.equals(SubOrderSignEnum.MAIN.getSignOne()) ? "orderNo" : "subOrderNo";
            JSONObject object = new JSONObject(data.get(jsonObject.getStr(orderNosKey)));
            //客户字段 应收:结算单位 应付:供应商 TODO 产品核对过,直接取主订单客户名称
//            if (type == 0) {
//                object.put("customerName", jsonObject.getStr("unitAccount"));
//            } else {
//                object.put("customerName", jsonObject.getStr("supplierChName"));
//            }

            jsonObject.putAll(object);
            jsonArray.add(jsonObject);
        }
        return jsonArray.size() == 0 ? array : jsonArray;
    }


}
