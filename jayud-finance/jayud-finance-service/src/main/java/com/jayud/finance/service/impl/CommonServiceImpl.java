package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.feign.FreightAirClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.feign.TmsClient;
import com.jayud.finance.feign.TrailerClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.vo.InputGoodsVO;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import com.jayud.finance.vo.template.order.TmsOrderTemplate;
import com.jayud.finance.vo.template.order.TrailerOrderTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private TrailerClient trailerClient;

    /**
     * 获取空运明细
     */
    @Override
    public Map<String, Object> getAirOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = freightAirClient.getAirOrderByMainOrderNos(mainOrderNos).getData();
        JSONArray array = new JSONArray(data);
        List<Long> airOrderIds = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            airOrderIds.add(array.getJSONObject(i).getLong("id"));
        }

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        //查询商品信息
        List<InputGoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
//        List<InputOrderAddressVO> orderAddressVOS = this.omsClient.getOrderAddressByBusIds(airOrderIds, BusinessTypeEnum.KY.getCode()).getData();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            switch (templateEnum) {
                case KY:
                case KY_NORM:
                    AirOrderTemplate airOrderTemplate = ConvertUtil.convert(jsonObject, AirOrderTemplate.class);
                    //组装商品信息
                    airOrderTemplate.assemblyGoodsInfo(goods);
                    airOrderTemplate.setGoodTime(DateUtils.format(airOrderTemplate.getGoodTime(), "yyyy-MM-dd"));
                    //拼装主订单信息
                    airOrderTemplate.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? airOrderTemplate.getMainOrderNo() : airOrderTemplate.getOrderNo(), airOrderTemplate);
                    //组装地址
                    // record.assemblyOrderAddress(orderAddressVOS);
                    break;
            }

        }
        return map;
    }

    /**
     * 获取中港明细
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
                case ZGYS_NORM:
                    TmsOrderTemplate tmsOrderTemplate = ConvertUtil.convert(jsonObject, TmsOrderTemplate.class);
                    tmsOrderTemplate.assembleData(jsonObject);
                    //组装主订单信息
                    tmsOrderTemplate.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? tmsOrderTemplate.getMainOrderNo() : tmsOrderTemplate.getOrderNo(), tmsOrderTemplate);
                    break;
//                case ZGYS_ONE:
//                    break;

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
            //空运
            if (templateEnum.getCmd().equals(BillTemplateEnum.KY.getCmd())) {
                data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //中港
            if (templateEnum.getCmd().equals(BillTemplateEnum.ZGYS.getCmd())) {
                data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //拖车
            if (templateEnum.getCmd().equals(BillTemplateEnum.TC.getCmd())) {
                data = this.getTrailerOrderTemplate(mainOrderNos, cmd, templateEnum);
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
            //空运
            if (templateEnum.getCmd().contains(BillTemplateEnum.KY.getCmd())) {
                data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //中港
            if (templateEnum.getCmd().contains(BillTemplateEnum.ZGYS.getCmd())) {
                data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //拖车
            if (templateEnum.getCmd().contains(BillTemplateEnum.TC.getCmd())) {
                data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
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

    /**
     * 拖车模板
     *
     * @param mainOrderNos
     * @param cmd
     * @param templateEnum
     * @return
     */
    @Override
    public Map<String, Object> getTrailerOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = this.trailerClient.getTrailerInfoByMainOrderNos(mainOrderNos).getData();
        JSONArray array = new JSONArray(data);
        Map<String, Object> map = new HashMap<>();
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            switch (templateEnum) {
                case TC:
                case TC_NORM:
                    TrailerOrderTemplate trailerOrderTemplate = ConvertUtil.convert(jsonObject, TrailerOrderTemplate.class);
                    trailerOrderTemplate.assembleData(jsonObject);
                    //组装主订单信息
                    trailerOrderTemplate.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? trailerOrderTemplate.getMainOrderNo() : trailerOrderTemplate.getOrderNo(), trailerOrderTemplate);
                    break;
//                case ZGYS_ONE:
//                    break;

            }

        }
        return map;
    }


}
