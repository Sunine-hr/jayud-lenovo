package com.jayud.finance.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.Utilities;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.enums.OrderBillCostTotalTypeEnum;
import com.jayud.finance.feign.InlandTpClient;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderPaymentBillMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.*;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import com.jayud.finance.vo.template.order.AirOrderTemplate;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderPaymentBillServiceImpl extends ServiceImpl<OrderPaymentBillMapper, OrderPaymentBill> implements IOrderPaymentBillService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    IOrderPaymentBillDetailService paymentBillDetailService;

    @Autowired
    IOrderReceivableBillDetailService receivableBillDetailService;
    @Autowired
    IOrderReceivableBillService receivableBillService;
    @Autowired
    IOrderBillCostTotalService costTotalService;
    @Autowired
    ICurrencyRateService currencyRateService;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private InlandTpClient inlandTpClient;
    @Autowired
    private CommonService commonService;

    @Override
    public IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form) {

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        //定义分页参数
        Page<OrderPaymentBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        IPage<OrderPaymentBillVO> pageInfo = null;
        if ("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findPaymentBillByPage(page, form, legalIds);//法人主体/供应商/可汇总主订单费用的维度统计
            for (OrderPaymentBillVO record : pageInfo.getRecords()) {
                List<Map<String, Object>> maps = baseMapper.statisticsNotPaidBillInfo(true, record.getSupplierCode(),
                        record.getLegalEntityId(), null, new HashMap<>());
                record.statisticsNotPaidBillInfo(maps, true);
            }
        } else {
            //动态sql参数
            Map<String, Object> param = new HashMap<>();
            param.put("cmd", form.getCmd());
            Map<String, Object> sqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
            pageInfo = baseMapper.findPaymentSubBillByPage(page, form, sqlParam, legalIds);//法人主体/供应商/子订单费用的维度统计
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> findPaymentBillNum(QueryPaymentBillNumForm form) {

//        //获取当前用户所属法人主体
//        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
//        List<Long> legalIds = (List<Long>)legalEntityByLegalName.getData();

        List<OrderPaymentBillNumVO> resultList = baseMapper.findPaymentBillNum(form);
        //查询结算汇率
        List<String> billNos = resultList.stream().map(OrderPaymentBillNumVO::getBillNo).collect(Collectors.toList());
        List<OrderBillCostTotal> costTotals = this.costTotalService.getByBillNo(billNos, OrderBillCostTotalTypeEnum.PAYMENT.getCode());
        costTotals = costTotals.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(e -> e.getCurrencyCode() + ";" + e.getCurrentCurrencyCode()))), ArrayList::new));

        //查询币种名称
        List<InitComboxStrVO> data = omsClient.initCurrencyInfo().getData();
        Map<String, String> currencyMap = data.stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        for (OrderPaymentBillNumVO billNumVO : resultList) {
            billNumVO.assembleSettlementRate(costTotals, currencyMap);
        }

        Map<String, Object> result = new HashMap<>();
        result.put(CommonConstant.LIST, resultList);
        return result;
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.costId"));

//        IPage<PaymentNotPaidBillVO> pageInfo = null;
//        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
//            Map<String, Object> param = new HashMap<>();
//            param.put("cmd", form.getCmd());
//            form.setIsQueryOrderAddress(true);
//            Map<String, Object> dynamicSqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
//            pageInfo = this.baseMapper.findBaseNotPaidBillByPage(page, form, dynamicSqlParam);
//        } else {
//            pageInfo = baseMapper.findNotPaidBillByPage(page, form);
//        }
        Map<String, Object> param = new HashMap<>();
        param.put("cmd", form.getCmd());
        form.setIsQueryOrderAddress(true);
        Map<String, Object> dynamicSqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findNotPaidBillByPage(page, form, dynamicSqlParam);

        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();

        //内陆处理
        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
            List<String> mainOrderNos = pageList.stream().map(PaymentNotPaidBillVO::getOrderNo).collect(Collectors.toList());
            List<OrderInlandTransportDetails> data = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
            pageList.forEach(e -> e.assembleInlandTPData(data));
        }

        //中港处理
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(form.getCmd())) {
            for (PaymentNotPaidBillVO paymentNotPaidBillVO : pageList) {
                //处理目的地:当有两条或两条以上时,则获取中转仓地址
                if (!StringUtil.isNullOrEmpty(paymentNotPaidBillVO.getEndAddress())) {
                    String[] strs = paymentNotPaidBillVO.getEndAddress().split(",");
                    if (strs.length > 1) {
                        paymentNotPaidBillVO.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBillVO.getOrderNo()));
                    }
                }
            }
        }

        //币种
        Map<String, String> currencyMap = this.omsClient.initCurrencyInfo().getData().stream().collect(Collectors.toMap(InitComboxStrVO::getCode, InitComboxStrVO::getName));
        pageInfo.getRecords().forEach(e -> {
            e.assembleAmountStr(currencyMap.get(e.getCurrencyCode()));
        });
        return pageInfo;
    }

    @Override
    public CommonResult createPaymentBill(CreatePaymentBillForm form) {
        OrderPaymentBillForm paymentBillForm = form.getPaymentBillForm();//账单信息
        List<OrderPaymentBillDetailForm> paymentBillDetailForms = form.getPaymentBillDetailForms();//账单详细信息
        Boolean result = true;
        //无论暂存还是生成账单都需要修改order_payment_cost表的is_bill
        List<Long> costIds = new ArrayList<>();
        List<String> orderNos = new ArrayList<>(); //为了统计已出账订单数
        for (OrderPaymentBillDetailForm paymentBillDetail : paymentBillDetailForms) {
            costIds.add(paymentBillDetail.getCostId());
            orderNos.add(paymentBillDetail.getOrderNo());
        }
        String settlementCurrency = form.getSettlementCurrency();
        List<OrderBillCostTotalVO> orderBillCostTotalVOS = new ArrayList<>();
        //校验是否配置了相应币种的汇率
        //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
        if ("create".equals(form.getCmd()) && costIds.size() > 0) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("请配置[");
//            Boolean flag = true;
//            orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency, form.getAccountTermStr());
//            AtomicBoolean isCheck = new AtomicBoolean(true);
//            //是否自定义汇率
//            if (form.getIsCustomExchangeRate()) {
//                //组装数据
//                Map<String, BigDecimal> customExchangeRate = new HashMap<>();
//                form.getCustomExchangeRate().forEach(e -> customExchangeRate.put(e.getCode(), e.getNote() == null ? new BigDecimal(0) : new BigDecimal(e.getNote())));
//                orderBillCostTotalVOS.forEach(e -> {
//                    BigDecimal rate = customExchangeRate.get(e.getCurrencyCode());
//                    e.setExchangeRate(rate);
//                    //结算币种是CNY
//                    if ("CNY".equals(settlementCurrency)) {
//                        isCheck.set(false);
//                        e.setLocalMoneyRate(rate);
//                        e.setLocalMoney(e.getMoney().multiply(rate));
//                    }
//                });
//            }
//            Set<String> msg = new HashSet<>();
//            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
//                BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
//                if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(settlementCurrency)) {
//                    //根据币种查询币种描述
//                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
//                    String dCurrency = currencyRateService.getNameByCode(settlementCurrency);
////                    sb.append("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                    msg.add("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
//                    flag = false;
//                }
//                if (orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                    orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
//                }
//            }
//            if (isCheck.get()) {
//                //出账时要以结算期为汇率记录本币金额，需要校验是否配置汇率
//                List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, "CNY", form.getAccountTermStr());
//                for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
//                    BigDecimal localMoney = orderBillCostTotalVO.getLocalMoney();//如果本币金额为0，说明汇率为空没配置
//                    if ((localMoney == null || localMoney.compareTo(new BigDecimal("0")) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                        //根据币种查询币种描述
//                        String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
////                        sb.append("原始币种:" + oCurrency + ",兑换币种:人民币;");
//                        msg.add("原始币种:" + oCurrency + ",兑换币种:人民币;");
//                        flag = false;
//                    }
//                }
//            }
//            if (!flag) {
//                msg.forEach(sb::append);
//                sb.append("]的汇率");
//                return CommonResult.error(10001, sb.toString());
//            }
            orderBillCostTotalVOS = this.configureExchangeRate(costIds, settlementCurrency, form.getAccountTermStr(),
                    form.getIsCustomExchangeRate(), form.getCustomExchangeRate());
        }
        OprCostBillForm oprCostBillForm = new OprCostBillForm();
        oprCostBillForm.setCmd(form.getCmd());
        oprCostBillForm.setCostIds(costIds);
        oprCostBillForm.setOprType("payment");
        result = omsClient.oprCostBill(oprCostBillForm).getData();
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //生成账单操作才是生成对账单数据
        if ("create".equals(form.getCmd()) && costIds.size() > 0) {
            //先保存对账单信息，在保存对账单详情信息
            OrderPaymentBill orderPaymentBill = ConvertUtil.convert(paymentBillForm, OrderPaymentBill.class);

            orderPaymentBill = this.statisticsBill(orderBillCostTotalVOS, orderPaymentBill, orderNos, form.getSubType());

            if ("main".equals(form.getSubType())) {
                orderPaymentBill.setIsMain(true);
            } else {
                orderPaymentBill.setIsMain(false);
            }
            orderPaymentBill.setSubType(form.getSubType());
            //判断该法人主体和客户是否已经生成过账单
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("sub_type", form.getSubType());
//            queryWrapper.eq("legal_name", paymentBillForm.getLegalName());
//            queryWrapper.eq("supplier_ch_name", paymentBillForm.getSupplierChName());
//            OrderPaymentBill existBill = baseMapper.selectOne(queryWrapper);

            //判断该法人主体和客户是否已经生成过账单
            //TODO 法人id和供应商code
            List<OrderPaymentBill> existBill = this.getByCondition(new OrderPaymentBill()
                    .setSubType(form.getSubType()).setLegalEntityId(paymentBillForm.getLegalEntityId())
                    .setSupplierCode(paymentBillForm.getSupplierCode()));

            if (CollectionUtils.isNotEmpty(existBill) && existBill.get(0).getId() != null) {
                orderPaymentBill.setId(existBill.get(0).getId());
                orderPaymentBill.setUpdatedTime(LocalDateTime.now());
                orderPaymentBill.setUpdatedUser(UserOperator.getToken());
                orderPaymentBill.setLegalEntityId(null);
                orderPaymentBill.setSupplierCode(null);
            }
//            else {
//                //查询法人主体id
//                Object data = oauthClient.getAllLegalEntityByLegalName(paymentBillForm.getLegalName()).getData();
//                Long legalEntityId = new JSONObject(data).getLong("id");
//                //查询供应商代码
//                data = omsClient.getSupplierInfoByName(paymentBillForm.getSupplierChName()).getData();
//                String supplierCode = new JSONObject(data).getStr("supplierCode");
//                orderPaymentBill.setLegalEntityId(legalEntityId).setSupplierCode(supplierCode);
//            }


            orderPaymentBill.setCreatedUser(UserOperator.getToken());
            result = saveOrUpdate(orderPaymentBill);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存对账单详情数据
            List<OrderPaymentBillDetail> paymentBillDetails = ConvertUtil.convertList(paymentBillDetailForms, OrderPaymentBillDetail.class);
            //录用费用明细
            Map<Long, OrderBillCostTotalVO> map = orderBillCostTotalVOS.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));
            for (int i = 0; i < paymentBillDetails.size(); i++) {
                OrderPaymentBillDetail paymentBillDetail = paymentBillDetails.get(i);
                paymentBillDetail.setStatus("1");
                paymentBillDetail.setBillNo(form.getBillNo());
                paymentBillDetail.setAccountTerm(form.getAccountTermStr());
                paymentBillDetail.setSettlementCurrency(form.getSettlementCurrency());
                paymentBillDetail.setAuditStatus(BillEnum.B_1.getCode());
                paymentBillDetail.setCreatedOrderTime(DateUtils.convert2Date(paymentBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                paymentBillDetail.setMakeUser(form.getLoginUserName());
                paymentBillDetail.setMakeTime(LocalDateTime.now());
                paymentBillDetail.setCreatedUser(form.getLoginUserName());
                paymentBillDetail.setBillId(orderPaymentBill.getId());
                paymentBillDetail.setLocalAmount(map.get(paymentBillDetail.getCostId()).getLocalMoney());
            }
            result = paymentBillDetailService.saveBatch(paymentBillDetails);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存费用维度的金额信息  以结算币种进行转换后保存
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                String currentCurrencyCode = orderBillCostTotalVO.getCurrencyCode();
                orderBillCostTotalVO.setBillNo(form.getBillNo());
                orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                BigDecimal money = orderBillCostTotalVO.getMoney();//录入费用时的金额
                BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();
                if (exchangeRate == null || exchangeRate.compareTo(new BigDecimal("0")) == 0) {
                    exchangeRate = new BigDecimal("1");
                }
                money = money.multiply(exchangeRate);
                orderBillCostTotalVO.setMoney(money);
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                orderBillCostTotal.setLocalMoney(orderBillCostTotalVO.getLocalMoney());
                orderBillCostTotal.setExchangeRate(exchangeRate);
                orderBillCostTotal.setCurrentCurrencyCode(currentCurrencyCode);
                orderBillCostTotal.setOrderNo(orderBillCostTotalVO.getOrderNo() == null ? orderBillCostTotalVO.getMainOrderNo() : orderBillCostTotalVO.getOrderNo());
                orderBillCostTotal.setMoneyType("1");
                orderBillCostTotal.setIsCustomExchangeRate(form.getIsCustomExchangeRate());
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        return CommonResult.success();
    }

    @Override
    public JSONArray viewPaymentBill(ViewFBillForm form, List<Long> costIds) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewPaymentBill(costIds);

        JSONArray array = new JSONArray(orderList);

        List<String> mainOrderNos = new ArrayList<>();
        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);

        for (int i = 0; i < orderList.size(); i++) {
            ViewFBilToOrderVO viewBillToOrder = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);

            //中港运输特殊处理
            this.tmsSpecialDataProcessing(form, viewBillToOrder);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo())
                        && viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))) {
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String, Object> propertiesMap = new HashMap<String, Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field f = fields[j];
                            f.setAccessible(true);
                            if ("name".equals(f.getName())) {
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if ("money".equals(f.getName())) {
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        jsonObject.putAll(propertiesMap);
//                        viewBillToOrder = (ViewFBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mainOrderNos.add(viewBillToOrder.getOrderNo());
            newOrderList.add(viewBillToOrder);
        }

        //内陆数据处理
        array = this.inlandTPDataProcessing(form, array, mainOrderNos);
        return array;
    }


    @Override
    public JSONArray viewPaymentBillInfo(ViewFBillForm form, List<Long> costIds) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewPaymentBill(costIds);

        JSONArray array = new JSONArray(orderList);

        List<String> mainOrderNos = new ArrayList<>();
        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);

        for (int i = 0; i < orderList.size(); i++) {
            ViewFBilToOrderVO viewBillToOrder = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);

            //中港运输特殊处理
            this.tmsSpecialDataProcessing(form, viewBillToOrder);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo())
                        && viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))) {
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String, Object> propertiesMap = new HashMap<String, Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int j = 0; j < fields.length; j++) {
                            Field f = fields[j];
                            f.setAccessible(true);
                            if ("name".equals(f.getName())) {
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if ("money".equals(f.getName())) {
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        jsonObject.putAll(propertiesMap);
//                        viewBillToOrder = (ViewFBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            mainOrderNos.add(viewBillToOrder.getOrderNo());
            newOrderList.add(viewBillToOrder);
        }

        //内陆数据处理
//        array = this.inlandTPDataProcessing(form, array, mainOrderNos);
        array = this.commonService.templateDataProcessing(form.getCmd(), form.getCmd(), array, mainOrderNos, 1);
        return array;
    }


    @Override
    public List<SheetHeadVO> findSheetHead(List<Long> costIds) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            ViewFBilToOrderHeadVO viewBilToOrderVO = new ViewFBilToOrderHeadVO();
            Class cls = viewBilToOrderVO.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                SheetHeadVO sheetHeadVO = new SheetHeadVO();
                sheetHeadVO.setName(f.getName());
                sheetHeadVO.setViewName(String.valueOf(f.get(viewBilToOrderVO)));
                fixHeadList.add(sheetHeadVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSheetHead(costIds);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public List<SheetHeadVO> findSheetHeadInfo(List<Long> costIds, String cmd) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            Class template = BillTemplateEnum.getTemplate(cmd);
            if (template != null) {
                List<Map<String, Object>> maps = Utilities.assembleEntityHead(template, false);
                fixHeadList = Utilities.obj2List(maps, SheetHeadVO.class);
            } else {//TODO 增强不影响原有系统,除非更替完成
                ViewFBilToOrderHeadVO viewBilToOrderVO = new ViewFBilToOrderHeadVO();
                Class cls = viewBilToOrderVO.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    SheetHeadVO sheetHeadVO = new SheetHeadVO();
                    sheetHeadVO.setName(f.getName());
                    sheetHeadVO.setViewName(String.valueOf(f.get(viewBilToOrderVO)));
                    fixHeadList.add(sheetHeadVO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSheetHead(costIds);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public ViewBillVO getViewBillByCostIds(List<Long> costIds, String cmd) {
        Map<String, Object> param = new HashMap<>();
        param.put("cmd", cmd);
        Map<String, Object> dynamicSqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
        return baseMapper.getViewBillByCostIds(costIds, cmd, dynamicSqlParam);
    }

    @Override
    public Integer getBillOrderNum(String legalName, String supplierChName, String subType) {
        return baseMapper.getBillOrderNum(legalName, supplierChName, subType);
    }

    @Override
    public BigDecimal getAlreadyPaidAmount(String legalName, String supplierChName, String subType) {
        return baseMapper.getAlreadyPaidAmount(legalName, supplierChName, subType);
    }

    @Override
    public Integer getBillNum(String legalName, String supplierChName, String subType) {
        return baseMapper.getBillNum(legalName, supplierChName, subType);
    }


    @Override
    public Boolean isExistBillNo(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", billNo);
        List<OrderPaymentBillDetail> fBillDetails = paymentBillDetailService.list(queryWrapper);
        List<OrderReceivableBillDetail> sBillDetails = receivableBillDetailService.list(queryWrapper);
        Integer resultFCount = 0;
        Integer resultSCount = 0;
        if (fBillDetails != null) {
            resultFCount = fBillDetails.size();
        }
        if (sBillDetails != null) {
            resultSCount = sBillDetails.size();
        }
        Integer resultCount = resultFCount + resultSCount;
        if (resultCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Long> findSaveConfirmData(List<Long> costIds) {
        return baseMapper.findSaveConfirmData(costIds);
    }

    private Map<String, Object> dynamicSQLFindReceiveBillByPageParam(Map<String, Object> param) {
        String cmd = MapUtil.getStr(param, "cmd");
        Map<String, Object> sqlParam = new HashMap<>();
        String subOrderSign = SubOrderSignEnum.getSignOne2SignTwo(cmd);
        sqlParam.put("table", SubOrderSignEnum.MAIN.getSignTwo().equals(subOrderSign) ? "" : subOrderSign);
        return sqlParam;
    }


    private void tmsSpecialDataProcessing(ViewFBillForm form, ViewFBilToOrderVO viewBillToOrder) {
        //处理目的地:当有两条或两条以上时,则获取中转仓地址
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(form.getCmd())) {
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
        }
    }


    private JSONArray inlandTPDataProcessing(ViewFBillForm form, JSONArray array, List<String> mainOrderNos) {
        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
            if (CollectionUtils.isEmpty(array)) {
                return array;
            }
            List<OrderInlandTransportDetails> details = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ViewBilToInlandTPOrderVO viewBilToInlandTPOrderVO = new ViewBilToInlandTPOrderVO();
                JSONObject result = viewBilToInlandTPOrderVO.assembleData(jsonObject, details);
                jsonArray.add(result);
            }
            return jsonArray;
        }
        return array;
    }


    @Override
    public List<OrderBillCostTotalVO> configureExchangeRate(List<Long> costIds, String settlementCurrency, String accountTermStr,
                                                            Boolean isCustomExchangeRate, List<InitComboxStrVO> customExchangeRates) {
        StringBuilder sb = new StringBuilder();
        sb.append("请配置[");
        Boolean flag = true;
        List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency, accountTermStr);
        AtomicBoolean isCheck = new AtomicBoolean(true);
        //是否自定义汇率
        if (isCustomExchangeRate) {
            //组装数据
            Map<String, BigDecimal> customExchangeRate = new HashMap<>();
            customExchangeRates.forEach(e -> customExchangeRate.put(e.getCode(), e.getNote() == null ? new BigDecimal(0) : new BigDecimal(e.getNote())));
            orderBillCostTotalVOS.forEach(e -> {
                BigDecimal rate = customExchangeRate.get(e.getCurrencyCode());
                e.setExchangeRate(rate);
                //结算币种是CNY
                if ("CNY".equals(settlementCurrency)) {
                    isCheck.set(false);
                    e.setLocalMoneyRate(rate);
                    e.setLocalMoney(e.getMoney().multiply(rate));
                }
            });
        }
        Set<String> msg = new HashSet<>();
        for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
            BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
            if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(settlementCurrency)) {
                //根据币种查询币种描述
                String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                String dCurrency = currencyRateService.getNameByCode(settlementCurrency);
//                    sb.append("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
                msg.add("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
                flag = false;
            }
            if (orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
            }
        }
        if (isCheck.get()) {
            //出账时要以结算期为汇率记录本币金额，需要校验是否配置汇率
            List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, "CNY", accountTermStr);
            for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
                BigDecimal localMoney = orderBillCostTotalVO.getLocalMoney();//如果本币金额为0，说明汇率为空没配置
                if ((localMoney == null || localMoney.compareTo(new BigDecimal("0")) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
//                        sb.append("原始币种:" + oCurrency + ",兑换币种:人民币;");
                    msg.add("原始币种:" + oCurrency + ",兑换币种:人民币;");
                    flag = false;
                }
            }
            //推金蝶,需要配置结算币种本币金额
            Map<String, BigDecimal> exchangeRates = this.currencyRateService.getExchangeRates("CNY", accountTermStr);
            if (exchangeRates.get(settlementCurrency) == null) {
                msg.add("结算币种:" + currencyRateService.getNameByCode(settlementCurrency) + ",兑换币种:人民币;");
                flag = false;
            }
        }
        if (!flag) {
            msg.forEach(sb::append);
            sb.append("]的汇率");
            throw new JayudBizException(10001, sb.toString());
        }
        return orderBillCostTotalVOS;
    }

    @Override
    public OrderPaymentBill statisticsBill(List<OrderBillCostTotalVO> orderBillCostTotalVOS, OrderPaymentBill orderPaymentBill, List<String> orderNos, String subType) {
        //1.统计已出账金额alreadyPaidAmount
        BigDecimal nowBillAmount = orderBillCostTotalVOS.stream().map(OrderBillCostTotalVO::getLocalMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
//            BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal alreadyPaidAmount = getAlreadyPaidAmount(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType);
        //TODO 法人id和供应商code
        BigDecimal alreadyPaidAmount = this.baseMapper.getAlreadyPaidAmountByLegalId(orderPaymentBill.getLegalEntityId(), orderPaymentBill.getSupplierCode(), subType);

        orderPaymentBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
        //2.统计已出账订单数billOrderNum
        List<String> validOrders = new ArrayList<>();
        orderNos = orderNos.stream().distinct().collect(Collectors.toList());
        for (String orderNo : orderNos) {
//            List<OrderPaymentBillDetail> orderNoObjects = paymentBillDetailService.getNowFOrderExist(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType, orderNo);
            //TODO 法人id和供应商code
            List<OrderPaymentBillDetail> orderNoObjects = paymentBillDetailService.getNowFOrderExistByLegalId(orderPaymentBill.getLegalEntityId(),
                    orderPaymentBill.getSupplierCode(), subType, orderNo);
            if (orderNoObjects == null || orderNoObjects.size() == 0) {
                validOrders.add(orderNo);
            }
        }
        Integer nowBillOrderNum = validOrders.size();
//        Integer billOrderNum = getBillOrderNum(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType);
        //TODO 法人id和供应商code
        Integer billOrderNum = this.baseMapper.getBillOrderNumByLegalId(orderPaymentBill.getLegalEntityId(), orderPaymentBill.getSupplierCode(), subType);
        orderPaymentBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
        //3.统计账单数billNum
//        Integer billNum = getBillNum(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), subType);
        //TODO 法人id和供应商code
        Integer billNum = this.baseMapper.getBillNumByLegalId(orderPaymentBill.getLegalEntityId(), orderPaymentBill.getSupplierCode(), subType);
        orderPaymentBill.setBillNum(billNum + 1);
        return orderPaymentBill;
    }

    @Override
    public List<OrderPaymentBill> getByCondition(OrderPaymentBill paymentBill) {
        QueryWrapper<OrderPaymentBill> condition = new QueryWrapper<>(paymentBill);
        return this.baseMapper.selectList(condition);
    }
}
