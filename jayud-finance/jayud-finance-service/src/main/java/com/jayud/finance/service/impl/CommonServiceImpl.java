package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jayud.common.ApiResult;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.feign.*;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.service.IVoidBillingRecordsService;
import com.jayud.finance.vo.InputGoodsVO;
import com.jayud.finance.vo.SheetHeadVO;
import com.jayud.finance.vo.template.order.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private InlandTpClient inlandTpClient;
    @Autowired
    private IOrderPaymentBillService orderPaymentBillService;
    @Autowired
    private IOrderReceivableBillService orderReceivableBillService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IVoidBillingRecordsService voidBillingRecordsService;
    @Autowired
    private OceanShipClient oceanShipClient;

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
     * 获取内陆明细
     */
    public Map<String, Object> getInlandOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
        JSONArray array = new JSONArray(data);
        Map<String, Object> map = new HashMap<>();
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            switch (templateEnum) {
                case NL_NORM:
                    InlandTPTemplate template = ConvertUtil.convert(jsonObject, InlandTPTemplate.class);
                    template.assembleData(jsonObject);
                    //组装主订单信息
                    template.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? template.getMainOrderNo() : template.getSubOrderNo(), template);
                    break;
            }

        }

        return map;
    }

    /**
     * 获取内陆明细
     */
    @Override
    public Map<String, Object> getSeaOrderTemplate(List<String> mainOrderNos, String cmd, BillTemplateEnum templateEnum) {
        Object data = this.oceanShipClient.getSeaOrderInfoByMainOrderNos(mainOrderNos).getData();
        JSONArray array = new JSONArray(data);
        Map<String, Object> map = new HashMap<>();
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrderNos);
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            switch (templateEnum) {
                case HY_NORM:
                    SeaOrderTemplate template = ConvertUtil.convert(jsonObject, SeaOrderTemplate.class);
                    template.assembleData(jsonObject);
                    //组装主订单信息
                    template.assemblyMainOrderData(result.getData());
                    map.put(cmd.equals("main") ? template.getMainOrderNo() : template.getSubOrderNo(), template);
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
//    @Override
//    public JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos, Integer type) {
//        Map<String, Object> data = new HashMap<>();
//
//        BillTemplateEnum templateEnum = BillTemplateEnum.getTemplateEnum(cmd);
//        if (templateEnum != null) {
//            //空运
//            if (templateEnum.getCmd().equals(BillTemplateEnum.KY.getCmd())) {
//                data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
//            }
//            //中港
//            if (templateEnum.getCmd().equals(BillTemplateEnum.ZGYS.getCmd())) {
//                data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
//            }
//            //拖车
//            if (templateEnum.getCmd().equals(BillTemplateEnum.TC.getCmd())) {
//                data = this.getTrailerOrderTemplate(mainOrderNos, cmd, templateEnum);
//            }
//            //内陆
//            if (templateEnum.getCmd().equals(BillTemplateEnum.NL.getCmd())) {
//                data = this.getTrailerOrderTemplate(mainOrderNos, cmd, templateEnum);
//            }
//        }
//
//
//        //TODO 中港地址截取6个字符
//        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)) {
//            for (int i = 0; i < array.size(); i++) {
//                JSONObject jsonObject = array.getJSONObject(i);
//                String startAddress = jsonObject.getStr("startAddress");
//                String endAddress = jsonObject.getStr("endAddress");
//                if (startAddress != null && startAddress.length() > 6) {
//                    jsonObject.put("startAddress", startAddress.substring(0, 6));
//                }
//                if (endAddress != null && endAddress.length() > 6) {
//                    jsonObject.put("endAddress", endAddress.substring(0, 6));
//                }
//            }
//        }
//
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < array.size(); i++) {
//            if (data.size() == 0) {
//                break;
//            }
//            JSONObject jsonObject = array.getJSONObject(i);
//
//            String orderNosKey = cmd.equals(SubOrderSignEnum.MAIN.getSignOne()) ? "orderNo" : "subOrderNo";
//            JSONObject object = new JSONObject(data.get(jsonObject.getStr(orderNosKey)));
//            //客户字段 应收:结算单位 应付:供应商 TODO 产品核对过,直接取主订单客户名称
////            if (type == 0) {
////                object.put("customerName", jsonObject.getStr("unitAccount"));
////            } else {
////                object.put("customerName", jsonObject.getStr("supplierChName"));
////            }
//
//            object.putAll(jsonObject);
//            jsonArray.add(object);
//        }
//        return jsonArray.size() == 0 ? array : jsonArray;
//    }


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
            String tmp = StringUtils.subStringVals(templateEnum.getCmd(), "-");
            //空运
            if (tmp.equals(BillTemplateEnum.KY.getCmd())) {
                data = this.getAirOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //中港
            if (tmp.equals(BillTemplateEnum.ZGYS.getCmd())) {
                data = this.getTmsOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //拖车
            if (tmp.equals(BillTemplateEnum.TC.getCmd())) {
                data = this.getTrailerOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //内陆
            if (tmp.equals(BillTemplateEnum.NL.getCmd())) {
                data = this.getInlandOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
            //海运
            if (tmp.equals(BillTemplateEnum.HY.getCmd())) {
                data = this.getSeaOrderTemplate(mainOrderNos, cmd, templateEnum);
            }
        }

        //TODO 中港地址截取6个字符
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(cmd)
                || SubOrderSignEnum.MAIN.getSignOne().equals(cmd)) {
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

    /**
     * 生成账单编号
     *
     * @param type          类型(0:应收,1:应付)
     * @param legalEntityId
     * @return
     */
    @Override
    public String generateBillNo(Long legalEntityId, Integer type) {
        LocalDateTime now = LocalDateTime.now();
        String date = DateUtils.LocalDateTime2Str(now, "YYMM");
        //查询法人主体代码
        Object legalEntitys = this.oauthClient.getLegalEntityByLegalIds(Collections.singletonList(legalEntityId)).getData();
        JSONObject jsonObject = new JSONArray(legalEntitys).getJSONObject(0);
        String legalCode = jsonObject.getStr("legalCode");
        //查询该日期订单数量
        String format = "YYYY-MM";
        String sqlFormat = "%Y-%m";
        StringBuilder billNo = new StringBuilder();
        switch (BillTypeEnum.getEnum(type)) {
            case RECEIVABLE:
                billNo.append(OrderTypeEnum.AR.getCode())
                        .append(legalCode).append(date);

                int count = this.orderReceivableBillService.getCountByMakeTime(DateUtils.LocalDateTime2Str(now, format), sqlFormat);
                //TODO 查询废错订单
                int voidNum = this.voidBillingRecordsService.getCountByMakeTime(DateUtils.LocalDateTime2Str(now, format), sqlFormat, BillTypeEnum.RECEIVABLE.getCode());
                //当前数量+1
                billNo.append(StringUtils.zeroComplement(4, count + voidNum + 1));
                break;
            case PAYMENT:
                billNo.append(OrderTypeEnum.AP.getCode())
                        .append(legalCode).append(date);
                //查询该日期订单数量
                count = this.orderPaymentBillService.getCountByMakeTime(DateUtils.LocalDateTime2Str(now, format), sqlFormat);
                //TODO 查询废错订单
                voidNum = this.voidBillingRecordsService.getCountByMakeTime(DateUtils.LocalDateTime2Str(now, format), sqlFormat, BillTypeEnum.PAYMENT.getCode());
                //当前数量+1
                billNo.append(StringUtils.zeroComplement(4, count + voidNum + 1));
                break;
        }
        return billNo.toString();
    }

    /**
     * 动态头部合计费用
     */
    @Override
    public Map<String, Map<String, BigDecimal>> totalDynamicHeadCost(int dynamicHeadCostIndex,
                                                                     List<SheetHeadVO> sheetHeadVOS,
                                                                     JSONArray datas) {
        LinkedHashMap<String, String> dynamicHead = new LinkedHashMap<>();
        for (int i = 0; i < sheetHeadVOS.size(); i++) {
            SheetHeadVO sheetHeadVO = sheetHeadVOS.get(i);
            if (i > dynamicHeadCostIndex) {
                dynamicHead.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            }
        }
        Map<String, BigDecimal> totalCostItem = new HashMap<>();
        Map<String, BigDecimal> totalItem = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            JSONObject jsonObject = datas.getJSONObject(i);
            dynamicHead.forEach((k, v) -> {
                BigDecimal cost = jsonObject.getBigDecimal(k);
                if (cost != null) {
                    if (v.startsWith("合计")) {
                        totalItem.merge(v, cost, BigDecimal::add);
                    } else {
                        totalCostItem.merge(v, cost, BigDecimal::add);
                    }
                }
            });

        }
        Map<String, Map<String, BigDecimal>> cost = new HashMap<>();
        cost.put("totalCostItem", totalCostItem);
        cost.put("totalItem", totalItem);
        return cost;
    }

    @Override
    public String calculatingCosts(List<String> amountStrs) {
        Map<String, BigDecimal> cost = new HashMap<>();
        for (String amount : amountStrs) {
            if (StringUtils.isEmpty(amount)) {
                continue;
            }
            if (amount.contains(",")) {
                String[] split = amount.split(",");
                for (int i = 0; i < split.length; i++) {
                    String[] tmp = split[i].split(" ");
                    String currencyName = tmp[1];
                    cost.merge(currencyName, new BigDecimal(tmp[0]), BigDecimal::add);
                }
            } else {
                String[] split = amount.split(" ");
                String currencyName = split[1];
                cost.merge(currencyName, new BigDecimal(split[0]), BigDecimal::add);
            }
        }
        //返回合计的费用
        StringBuilder sb = new StringBuilder();
        cost.forEach((k, v) -> sb.append(v).append(" ").append(k).append(","));

        return sb.toString();
    }


    public static void main(String[] args) {
        String str = "合计(港元)";
        System.out.println(str.startsWith("合计"));
    }

}
