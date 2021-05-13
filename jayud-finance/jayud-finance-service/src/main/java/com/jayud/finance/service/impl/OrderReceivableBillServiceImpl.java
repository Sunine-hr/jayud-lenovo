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
import com.jayud.finance.feign.FreightAirClient;
import com.jayud.finance.feign.InlandTpClient;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.*;
import com.jayud.finance.vo.InlandTP.OrderInlandTransportDetails;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
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
public class OrderReceivableBillServiceImpl extends ServiceImpl<OrderReceivableBillMapper, OrderReceivableBill> implements IOrderReceivableBillService {
    @Autowired
    OmsClient omsClient;

    @Autowired
    IOrderReceivableBillDetailService receivableBillDetailService;
    @Autowired
    IOrderBillCostTotalService costTotalService;
    @Autowired
    ICurrencyRateService currencyRateService;
    @Autowired
    OauthClient oauthClient;
    @Autowired
    private InlandTpClient inlandTpClient;
    @Autowired
    private FreightAirClient freightAirClient;
    @Autowired
    private CommonService commonService;

    @Override
    public IPage<OrderReceiveBillVO> findReceiveBillByPage(QueryReceiveBillForm form) {

        //获取当前用户所属法人主体
        ApiResult legalEntityByLegalName = oauthClient.getLegalIdBySystemName(form.getLoginUserName());
        List<Long> legalIds = (List<Long>) legalEntityByLegalName.getData();

        //定义分页参数
        Page<OrderReceiveBillVO> page = new Page(form.getPageNum(), form.getPageSize());
        IPage<OrderReceiveBillVO> pageInfo = null;
        if ("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findReceiveBillByPage(page, form, legalIds);//法人主体/结算单位/可汇总主订单费用的维度统计
            for (OrderReceiveBillVO record : pageInfo.getRecords()) {
                List<Map<String, Object>> maps = baseMapper.statisticsNotPaidBillInfo(true, record.getUnitCode(),
                        record.getLegalEntityId(), null, new HashMap<>());
                record.statisticsNotPaidBillInfo(maps, true);
            }
        } else {
            //动态sql参数
            Map<String, Object> param = new HashMap<>();
            param.put("cmd", form.getCmd());
            Map<String, Object> sqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
            pageInfo = baseMapper.findReceiveSubBillByPage(page, form, sqlParam, legalIds);//法人主体/结算单位/子订单费用的维度统计
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> findReceiveBillNum(QueryReceiveBillNumForm form) {
        List<OrderPaymentBillNumVO> resultList = baseMapper.findReceiveBillNum(form);
        //查询结算汇率
        List<String> billNos = resultList.stream().map(OrderPaymentBillNumVO::getBillNo).collect(Collectors.toList());
        List<OrderBillCostTotal> costTotals = this.costTotalService.getByBillNo(billNos, OrderBillCostTotalTypeEnum.RECEIVABLE.getCode());
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
        /*result.put(CommonConstant.BILL_NUM_TOTAL,resultList.stream().mapToInt(OrderPaymentBillNumVO::getBillNum).sum());//订单数合计
        result.put(CommonConstant.RMB_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getRmb).reduce(BigDecimal.ZERO,BigDecimal::add));//人民币合计
        result.put(CommonConstant.DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//美元合计
        result.put(CommonConstant.EURO_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getEuro).reduce(BigDecimal.ZERO,BigDecimal::add));//欧元合计
        result.put(CommonConstant.HK_DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getHKDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//港币合计
        result.put(CommonConstant.LOCAL_AMOUNT_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//本币金额合计
        result.put(CommonConstant.HE_XIAO_AMOUNT,resultList.stream().map(OrderPaymentBillNumVO::getHeXiaoAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//已收金额，即财务已核销金额合计*/
        return result;
    }

    @Override
    public IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form) {
        //定义分页参数
        Page<ReceiveNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());

        //定义排序规则
        page.addOrder(OrderItem.desc("temp.costId"));
        IPage<ReceiveNotPaidBillVO> pageInfo = null;

        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
            Map<String, Object> param = new HashMap<>();
            param.put("cmd", form.getCmd());
            form.setIsQueryOrderAddress(true);
            Map<String, Object> dynamicSqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
            pageInfo = this.baseMapper.findBaseNotPaidBillByPage(page, form, dynamicSqlParam);
        } else {
            pageInfo = baseMapper.findNotPaidBillByPage(page, form);

        }

        List<ReceiveNotPaidBillVO> pageList = pageInfo.getRecords();
        if (CollectionUtils.isEmpty(pageList)) {
            return pageInfo;
        }

        //内陆处理
        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
            List<String> mainOrderNos = pageList.stream().map(ReceiveNotPaidBillVO::getOrderNo).collect(Collectors.toList());
            List<OrderInlandTransportDetails> data = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
            pageList.forEach(e -> e.assembleInlandTPData(data));
        }


        //中港处理
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(form.getCmd())) {
            for (ReceiveNotPaidBillVO receiveNotPaidBillVO : pageList) {
                //处理目的地:当有两条或两条以上时,则获取中转仓地址
                if (!StringUtil.isNullOrEmpty(receiveNotPaidBillVO.getEndAddress())) {
                    String[] strs = receiveNotPaidBillVO.getEndAddress().split(",");
                    if (strs.length > 1) {
                        receiveNotPaidBillVO.setEndAddress(getWarehouseAddress(receiveNotPaidBillVO.getOrderNo()));
                    }
                }
            }
        }

        return pageInfo;
    }

//    @Override
//    public IPage<ReceiveNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form) {
//        //定义分页参数
//        Page<ReceiveNotPaidBillVO> page = new Page(form.getPageNum(), form.getPageSize());
//
//        //定义排序规则
//        page.addOrder(OrderItem.desc("temp.costId"));
//        IPage<ReceiveNotPaidBillVO> pageInfo = null;
//
//        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
//            Map<String, Object> param = new HashMap<>();
//            param.put("cmd", form.getCmd());
//            form.setIsQueryOrderAddress(true);
//            Map<String, Object> dynamicSqlParam = this.dynamicSQLFindReceiveBillByPageParam(param);
//            pageInfo = this.baseMapper.findBaseNotPaidBillByPage(page, form, dynamicSqlParam);
//        } else {
//            pageInfo = baseMapper.findNotPaidBillByPage(page, form);
//
//        }
//        this.assembleTemplate(form.getCmd(),page.getRecords());
//
//        List<ReceiveNotPaidBillVO> pageList = pageInfo.getRecords();
//        if (CollectionUtils.isEmpty(pageList)) {
//            return pageInfo;
//        }
//
//        //内陆处理
//        if (SubOrderSignEnum.NL.getSignOne().equals(form.getCmd())) {
//            List<String> mainOrderNos = pageList.stream().map(ReceiveNotPaidBillVO::getOrderNo).collect(Collectors.toList());
//            List<OrderInlandTransportDetails> data = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
//            pageList.forEach(e -> e.assembleInlandTPData(data));
//        }
//
//
//        //中港处理
//        if (SubOrderSignEnum.ZGYS.getSignOne().equals(form.getCmd())) {
//            for (ReceiveNotPaidBillVO receiveNotPaidBillVO : pageList) {
//                //处理目的地:当有两条或两条以上时,则获取中转仓地址
//                if (!StringUtil.isNullOrEmpty(receiveNotPaidBillVO.getEndAddress())) {
//                    String[] strs = receiveNotPaidBillVO.getEndAddress().split(",");
//                    if (strs.length > 1) {
//                        receiveNotPaidBillVO.setEndAddress(getWarehouseAddress(receiveNotPaidBillVO.getOrderNo()));
//                    }
//                }
//            }
//        }
//
//        return pageInfo;
//    }


    @Override
    public CommonResult createReceiveBill(CreateReceiveBillForm form) {
        OrderReceiveBillForm receiveBillForm = form.getReceiveBillForm();//账单信息
        List<OrderReceiveBillDetailForm> receiveBillDetailForms = form.getReceiveBillDetailForms();//账单详细信息
        Boolean result = true;
        //无论暂存还是生成账单都需要修改order_receivable_cost表的is_bill
        List<Long> costIds = new ArrayList<>();
        List<String> orderNos = new ArrayList<>(); //为了统计已出账订单数
        for (OrderReceiveBillDetailForm receiveBillDetailForm : receiveBillDetailForms) {
            costIds.add(receiveBillDetailForm.getCostId());
            orderNos.add(receiveBillDetailForm.getOrderNo());
        }

        String settlementCurrency = form.getSettlementCurrency();
        List<OrderBillCostTotalVO> orderBillCostTotalVOS = new ArrayList<>();
        //校验是否配置了相应币种的汇率
        //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换

        if ("create".equals(form.getCmd()) && costIds.size() > 0) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("请配置[");
//            Boolean flag = true;
//            orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency, form.getAccountTermStr());
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
//
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
//                List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, "CNY", form.getAccountTermStr());
//                for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
//                    BigDecimal localMoneyRate = orderBillCostTotalVO.getLocalMoneyRate();//如果本币金额为0，说明汇率为空没配置
//                    if ((localMoneyRate == null || localMoneyRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
//                        //根据币种查询币种描述
//                        String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
//                        // sb.append("原始币种:" + oCurrency + ",兑换币种:人民币;");
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
            //配置汇率
            orderBillCostTotalVOS = this.configureExchangeRate(costIds, settlementCurrency, form.getAccountTermStr(),
                    form.getIsCustomExchangeRate(), form.getCustomExchangeRate());
        }
        OprCostBillForm oprCostBillForm = new OprCostBillForm();
        oprCostBillForm.setCmd(form.getCmd());
        oprCostBillForm.setCostIds(costIds);
        oprCostBillForm.setOprType("receivable");
        result = omsClient.oprCostBill(oprCostBillForm).getData();
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //生成账单操作才是生成对账单数据
        if ("create".equals(form.getCmd()) && costIds.size() > 0) {
            //先保存对账单信息，在保存对账单详情信息
            OrderReceivableBill orderReceivableBill = ConvertUtil.convert(receiveBillForm, OrderReceivableBill.class);
            //统计账单
            orderReceivableBill = this.statisticsBill(orderBillCostTotalVOS, orderReceivableBill, orderNos, form.getSubType());

            if ("main".equals(form.getSubType())) {
                orderReceivableBill.setIsMain(true);
            } else {
                orderReceivableBill.setIsMain(false);
            }
            orderReceivableBill.setSubType(form.getSubType());
            //判断该法人主体和客户是否已经生成过账单
//            QueryWrapper queryWrapper = new QueryWrapper();
//            queryWrapper.eq("sub_type", form.getSubType());
//            queryWrapper.eq("legal_name", receiveBillForm.getLegalName());
//            queryWrapper.eq("unit_account", receiveBillForm.getUnitAccount());

//            OrderReceivableBill existBill = baseMapper.selectOne(condition);
            //判断该法人主体和客户是否已经生成过账单
            //TODO 修改成根据法人id和结算code
            List<OrderReceivableBill> existBill = this.getByCondition(new OrderReceivableBill().setSubType(form.getSubType())
                    .setLegalEntityId(receiveBillForm.getLegalEntityId())
                    .setUnitCode(receiveBillForm.getUnitCode()));

            if (CollectionUtils.isNotEmpty(existBill) && existBill.get(0).getId() != null) {
                orderReceivableBill.setId(existBill.get(0).getId());
                orderReceivableBill.setUpdatedTime(LocalDateTime.now());
                orderReceivableBill.setUpdatedUser(form.getLoginUserName());
                orderReceivableBill.setLegalEntityId(null);
                orderReceivableBill.setUnitCode(null);
            }

            orderReceivableBill.setCreatedUser(form.getLoginUserName());
            result = saveOrUpdate(orderReceivableBill);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存对账单详情数据
            List<OrderReceivableBillDetail> receivableBillDetails = ConvertUtil.convertList(receiveBillDetailForms, OrderReceivableBillDetail.class);
            //录用费用明细
            Map<Long, OrderBillCostTotalVO> map = orderBillCostTotalVOS.stream().collect(Collectors.toMap(e -> e.getCostId(), e -> e));

            for (int i = 0; i < receivableBillDetails.size(); i++) {
                OrderReceivableBillDetail orderReceivableBillDetail = receivableBillDetails.get(i);
                orderReceivableBillDetail.setStatus("1");
                orderReceivableBillDetail.setBillNo(form.getBillNo());
                orderReceivableBillDetail.setAccountTerm(form.getAccountTermStr());
                orderReceivableBillDetail.setSettlementCurrency(form.getSettlementCurrency());
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_1.getCode());
                orderReceivableBillDetail.setCreatedOrderTime(DateUtils.convert2Date(receiveBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_PATTERN));
                orderReceivableBillDetail.setMakeUser(form.getLoginUserName());
                orderReceivableBillDetail.setMakeTime(LocalDateTime.now());
                orderReceivableBillDetail.setCreatedUser(form.getLoginUserName());
                orderReceivableBillDetail.setBillId(orderReceivableBill.getId());
                orderReceivableBillDetail.setLocalAmount(map.get(orderReceivableBillDetail.getCostId()).getLocalMoney());
            }
            result = receivableBillDetailService.saveBatch(receivableBillDetails);
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
                orderBillCostTotal.setLocalMoney(orderBillCostTotalVO.getLocalMoney());//本币金额
                orderBillCostTotal.setMoneyType("2");
                orderBillCostTotal.setExchangeRate(exchangeRate);
                orderBillCostTotal.setCurrentCurrencyCode(currentCurrencyCode);
                orderBillCostTotal.setOrderNo(orderBillCostTotalVO.getOrderNo() == null ? orderBillCostTotalVO.getMainOrderNo() : orderBillCostTotalVO.getOrderNo());
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
    public JSONArray viewReceiveBill(ViewSBillForm form, List<Long> costIds) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewReceiveBill(costIds);
        JSONArray array = new JSONArray(orderList);

        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<String> mainOrderNos = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);

        for (int i = 0; i < orderList.size(); i++) {
            ViewBilToOrderVO viewBillToOrder = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);
            this.tmsSpecialDataProcessing(form, viewBillToOrder);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
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
//                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
//                        viewBillToOrder.setDynamicMap(propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewBillToOrder);
            mainOrderNos.add(viewBillToOrder.getOrderNo());
//            list.add(viewBillToOrder);
        }
        //内陆数据处理
        array = this.inlandTPDataProcessing(form, array, mainOrderNos);

        return array;
    }


    @Override
    public JSONArray viewReceiveBillInfo(ViewSBillForm form, List<Long> costIds) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewReceiveBill(costIds);
        JSONArray array = new JSONArray(orderList);

        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<String> mainOrderNos = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);

        for (int i = 0; i < orderList.size(); i++) {
            ViewBilToOrderVO viewBillToOrder = orderList.get(i);
            JSONObject jsonObject = array.getJSONObject(i);
            this.tmsSpecialDataProcessing(form, viewBillToOrder);

            for (ViewBillToCostClassVO viewBillToCostClass : findCostClass) {
                if ((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
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
//                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
//                        viewBillToOrder.setDynamicMap(propertiesMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewBillToOrder);
            mainOrderNos.add(viewBillToOrder.getOrderNo());
//            list.add(viewBillToOrder);
        }
        //内陆数据处理
//        array = this.inlandTPDataProcessing(form, array, mainOrderNos);
        array = this.commonService.templateDataProcessing(form.getCmd(), form.getCmd(), array, mainOrderNos, 0);
        return array;
    }


    @Override
    public List<SheetHeadVO> findSheetHead(List<Long> costIds) {
        List<SheetHeadVO> allHeadList = new ArrayList<>();
        List<SheetHeadVO> fixHeadList = new ArrayList<>();
        try {
            ViewBilToOrderHeadVO viewBilToOrderVO = new ViewBilToOrderHeadVO();
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
                ViewBilToOrderHeadVO viewBilToOrderVO = new ViewBilToOrderHeadVO();
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
    public Integer getSBillOrderNum(String legalName, String unitAccount, String subType) {
        return baseMapper.getSBillOrderNum(legalName, unitAccount, subType);
    }

    @Override
    public BigDecimal getSAlreadyPaidAmount(String legalName, String unitAccount, String subType) {
        return baseMapper.getSAlreadyPaidAmount(legalName, unitAccount, subType);
    }

    /**
     * TODO 根据法人id和结算code
     * 统计已出账金额alreadyPaidAmount
     *
     * @return
     */
    @Override
    public BigDecimal getSAlreadyPaidAmountByLegalId(@Param("legalId") Long legalId, @Param("unitCode") String unitCode, @Param("subType") String subType) {
        return baseMapper.getSAlreadyPaidAmountByLegalId(legalId, unitCode, subType);
    }


    @Override
    public Integer getSBillNum(String legalName, String unitAccount, String subType) {
        return baseMapper.getSBillNum(legalName, unitAccount, subType);
    }


    @Override
    public List<Long> findSaveConfirmData(List<Long> costIds) {
        return baseMapper.findSaveConfirmData(costIds);
    }

    @Override
    public String getWarehouseAddress(String orderNo) {
        return baseMapper.getWarehouseAddress(orderNo);
    }

    @Override
    public List<OrderBillCostTotalVO> configureExchangeRate(List<Long> costIds, String settlementCurrency, String accountTermStr,
                                                            Boolean isCustomExchangeRate, List<InitComboxStrVO> customExchangeRates) {
//        String settlementCurrency = form.getSettlementCurrency();
        StringBuilder sb = new StringBuilder();
        sb.append("请配置[");
        Boolean flag = true;
        List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency, accountTermStr);
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
            List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, "CNY", accountTermStr);
            for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
                BigDecimal localMoneyRate = orderBillCostTotalVO.getLocalMoneyRate();//如果本币金额为0，说明汇率为空没配置
                if ((localMoneyRate == null || localMoneyRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                    // sb.append("原始币种:" + oCurrency + ",兑换币种:人民币;");
                    msg.add("原始币种:" + oCurrency + ",兑换币种:人民币;");
                    flag = false;
                }
            }
        }
        if (!flag) {
            msg.forEach(sb::append);
            sb.append("]的汇率");
            throw new JayudBizException(10001, sb.toString());
        }

        return orderBillCostTotalVOS;
    }

    /**
     * 统计账单
     * @param orderBillCostTotalVOS
     * @param orderReceivableBill
     * @param orderNos
     * @param subType
     * @return
     */
    @Override
    public OrderReceivableBill statisticsBill(List<OrderBillCostTotalVO> orderBillCostTotalVOS,
                                              OrderReceivableBill orderReceivableBill,
                                              List<String> orderNos, String subType) {
        //1.统计已出账金额alreadyPaidAmount
        BigDecimal nowBillAmount = orderBillCostTotalVOS.stream().map(OrderBillCostTotalVO::getLocalMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
//            BigDecimal alreadyPaidAmount = getSAlreadyPaidAmount(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
        //TODO 修改成根据法人id和结算code
        BigDecimal alreadyPaidAmount = getSAlreadyPaidAmountByLegalId(orderReceivableBill.getLegalEntityId(), orderReceivableBill.getUnitCode(), subType);
        orderReceivableBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
        //2.统计已出账订单数billOrderNum
        List<String> validOrders = new ArrayList<>();
        orderNos = orderNos.stream().distinct().collect(Collectors.toList());
        for (String orderNo : orderNos) {
//                List<OrderReceivableBillDetail> orderNoObjects = receivableBillDetailService.getNowSOrderExist(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType(), orderNo);
            // 已存在账单不加入集合
            List<OrderReceivableBillDetail> orderNoObjects = receivableBillDetailService.getNowSOrderExistByLegalId(orderReceivableBill.getLegalEntityId(), orderReceivableBill.getUnitCode(), subType, orderNo);
            if (orderNoObjects == null || orderNoObjects.size() == 0) {
                validOrders.add(orderNo);
            }
        }
        Integer nowBillOrderNum = validOrders.size();
//            Integer billOrderNum = getSBillOrderNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
        //TODO 修改成根据法人id和结算code
        Integer billOrderNum = this.baseMapper.getSBillOrderNumByLegalId(orderReceivableBill.getLegalEntityId(), orderReceivableBill.getUnitCode(), subType);
        orderReceivableBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
        //3.统计账单数billNum
//            Integer billNum = getSBillNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
        //TODO 修改成根据法人id和结算code
        Integer billNum = this.baseMapper.getSBillNumByLegalId(orderReceivableBill.getLegalEntityId(), orderReceivableBill.getUnitCode(), subType);
        orderReceivableBill.setBillNum(billNum + 1);

        return orderReceivableBill;
    }

    /**
     * 根据条件查询信息
     *
     * @param orderReceivableBill
     * @return
     */
    @Override
    public List<OrderReceivableBill> getByCondition(OrderReceivableBill orderReceivableBill) {
        QueryWrapper<OrderReceivableBill> condition = new QueryWrapper<>(orderReceivableBill);
        return this.baseMapper.selectList(condition);
    }


    private Map<String, Object> dynamicSQLFindReceiveBillByPageParam(Map<String, Object> map) {
        String cmd = MapUtil.getStr(map, "cmd");
        Map<String, Object> sqlParam = new HashMap<>();
        String subOrderSign = SubOrderSignEnum.getSignOne2SignTwo(cmd);
        sqlParam.put("table", SubOrderSignEnum.MAIN.getSignTwo().equals(subOrderSign) ? "" : subOrderSign);
        return sqlParam;
    }


    private JSONArray inlandTPDataProcessing(ViewSBillForm form, JSONArray array, List<String> mainOrderNos) {
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

    private void tmsSpecialDataProcessing(ViewSBillForm form, ViewBilToOrderVO viewBillToOrder) {
        //中港运输 处理目的地:当有两条或两条以上时,则获取中转仓地址
        if (SubOrderSignEnum.ZGYS.getSignOne().equals(form.getCmd())) {
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
        }
    }


//    public Page<OrderTemplate> assembleTemplate(String cmd, List<ReceiveNotPaidBillVO> pageList) {
//
//        if (CollectionUtils.isEmpty(pageList)) {
//            return null;
//        }
//        List<String> mainOrderNos = pageList.stream().map(ReceiveNotPaidBillVO::getOrderNo).collect(Collectors.toList());
//
//        if (SubOrderSignEnum.KY.getSignOne().equals(cmd)) {
//            List<AirOrderTemplate> airOrderTemplate = this.commonService.getAirOrderTemplate(mainOrderNos);
//            Map<String, AirOrderTemplate> tmp = airOrderTemplate.stream().collect(Collectors.toMap(AirOrderTemplate::getMainOrderNo, e -> e));
//            for (ReceiveNotPaidBillVO receiveNotPaidBillVO : pageList) {
//                AirOrderTemplate convert = ConvertUtil.convert(tmp.get(receiveNotPaidBillVO.getOrderNo()), AirOrderTemplate.class);
//
//            }
//        }
//
//        Page<Template> page = new Page<>();
//        return null;
//    }


//    private JSONArray templateDataProcessing(String cmd, JSONArray array, List<String> mainOrderNos) {
//        Map<String, Object> data = new HashMap<>();
//        if (SubOrderSignEnum.KY.getSignOne().equals(cmd)) {
//            List<AirOrderTemplate> airOrderTemplate = this.commonService.getAirOrderTemplate(mainOrderNos);
//            data = airOrderTemplate.stream().collect(Collectors.toMap(AirOrderTemplate::getOrderNo, e -> e));
//        }
//        if (SubOrderSignEnum.NL.getSignOne().equals(cmd)) {
////            List<OrderInlandTransportDetails> details = this.inlandTpClient.getInlandOrderInfoByMainOrderNos(mainOrderNos).getData();
////            data = details.stream().collect(Collectors.toMap(OrderInlandTransportDetails::getOrderNo, e -> e));
//        }
//
//        //TODO 中港地址截取6个字符
//
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < array.size(); i++) {
//            if (data.size() == 0) {
//                break;
//            }
//            JSONObject jsonObject = array.getJSONObject(i);
//            JSONObject object = new JSONObject(data.get(jsonObject.getStr("subOrderNo")));
//            object.put("customerName", jsonObject.getStr("unitAccount"));
//            object.putAll(jsonObject);
//            jsonArray.add(object);
//        }
//        return jsonArray.size() == 0 ? array : jsonArray;
//    }
}
