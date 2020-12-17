package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
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

    @Override
    public IPage<OrderReceiveBillVO> findReceiveBillByPage(QueryReceiveBillForm form) {
        //定义分页参数
        Page<OrderReceiveBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<OrderReceiveBillVO> pageInfo = null;
        if("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findReceiveBillByPage(page, form);//法人主体/结算单位/可汇总主订单费用的维度统计
        }else if("zgys".equals(form.getCmd()) || "bg".equals(form.getCmd())){
            pageInfo = baseMapper.findReceiveSubBillByPage(page, form);//法人主体/结算单位/子订单费用的维度统计
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> findReceiveBillNum(QueryReceiveBillNumForm form) {
        List<OrderPaymentBillNumVO> resultList = baseMapper.findReceiveBillNum(form);
        Map<String, Object> result = new HashMap<>();
        result.put(CommonConstant.LIST,resultList);
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
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.costId"));
        IPage<ReceiveNotPaidBillVO> pageInfo = baseMapper.findNotPaidBillByPage(page, form);
        List<ReceiveNotPaidBillVO> pageList = pageInfo.getRecords();
        for (ReceiveNotPaidBillVO receiveNotPaidBillVO : pageList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(receiveNotPaidBillVO.getEndAddress())){
                String[] strs = receiveNotPaidBillVO.getEndAddress().split(",");
                if(strs.length > 1){
                    receiveNotPaidBillVO.setEndAddress(getWarehouseAddress(receiveNotPaidBillVO.getOrderNo()));
                }
            }
        }
        return pageInfo;
    }

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
        if("create".equals(form.getCmd()) && costIds.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("请配置[");
            Boolean flag = true;
            orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency,form.getAccountTermStr());
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
                if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(settlementCurrency)) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                    String dCurrency = currencyRateService.getNameByCode(settlementCurrency);
                    sb.append("原始币种:"+oCurrency+",兑换币种:"+dCurrency+";");
                    flag = false;
                }
                if(orderBillCostTotalVO.getCurrencyCode().equals("CNY")){
                    orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
                }
            }
            List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, "CNY",form.getAccountTermStr());
            for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
                BigDecimal localMoney = orderBillCostTotalVO.getLocalMoney();//如果本币金额为0，说明汇率为空没配置
                if ((localMoney == null || localMoney.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                    sb.append("原始币种:"+oCurrency+",兑换币种:人民币;");
                    flag = false;
                }
            }
            if(!flag){
                sb.append("]的汇率");
                return CommonResult.error(10001,sb.toString());
            }
        }
        OprCostBillForm oprCostBillForm = new OprCostBillForm();
        oprCostBillForm.setCmd(form.getCmd());
        oprCostBillForm.setCostIds(costIds);
        oprCostBillForm.setOprType("receivable");
        result = omsClient.oprCostBill(oprCostBillForm).getData();
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //生成账单操作才是生成对账单数据
        if("create".equals(form.getCmd()) && costIds.size() > 0){
            //先保存对账单信息，在保存对账单详情信息
            OrderReceivableBill orderReceivableBill = ConvertUtil.convert(receiveBillForm,OrderReceivableBill.class);
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = receiveBillDetailForms.stream().map(OrderReceiveBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal alreadyPaidAmount = getSAlreadyPaidAmount(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
            orderReceivableBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            List<String> validOrders = new ArrayList<>();
            orderNos = orderNos.stream().distinct().collect(Collectors.toList());
            for (String orderNo : orderNos) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("order_no",orderNo);
                List<OrderReceivableBillDetail> orderNoObjects= receivableBillDetailService.list(queryWrapper1);
                if(orderNoObjects == null || orderNoObjects.size() == 0){
                    validOrders.add(orderNo);
                }
            }
            Integer nowBillOrderNum = validOrders.size();
            Integer billOrderNum = getSBillOrderNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
            orderReceivableBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
            //3.统计账单数billNum
            Integer billNum = getSBillNum(orderReceivableBill.getLegalName(), orderReceivableBill.getUnitAccount(), form.getSubType());
            orderReceivableBill.setBillNum(billNum + 1);
            if("main".equals(form.getSubType())){
                orderReceivableBill.setIsMain(true);
            }else if("zgys".equals(form.getSubType())){
                orderReceivableBill.setIsMain(false);
            }else if("bg".equals(form.getSubType())){
                orderReceivableBill.setIsMain(false);
            }
            orderReceivableBill.setSubType(form.getSubType());
            //判断该法人主体和客户是否已经生成过账单
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sub_type",form.getSubType());
            queryWrapper.eq("legal_name",receiveBillForm.getLegalName());
            queryWrapper.eq("unit_account",receiveBillForm.getUnitAccount());
            OrderReceivableBill existBill = baseMapper.selectOne(queryWrapper);
            if(existBill != null && existBill.getId() != null){
                orderReceivableBill.setId(existBill.getId());
                orderReceivableBill.setUpdatedTime(LocalDateTime.now());
                orderReceivableBill.setUpdatedUser(form.getLoginUserName());
            }
            orderReceivableBill.setCreatedUser(form.getLoginUserName());
            result = saveOrUpdate(orderReceivableBill);
            if(!result){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存对账单详情数据
            List<OrderReceivableBillDetail> receivableBillDetails = ConvertUtil.convertList(receiveBillDetailForms,OrderReceivableBillDetail.class);
            for (int i = 0;i<receivableBillDetails.size();i++) {
                receivableBillDetails.get(i).setStatus("1");
                receivableBillDetails.get(i).setBillNo(form.getBillNo());
                receivableBillDetails.get(i).setAccountTerm(form.getAccountTermStr());
                receivableBillDetails.get(i).setSettlementCurrency(form.getSettlementCurrency());
                receivableBillDetails.get(i).setAuditStatus(BillEnum.B_1.getCode());
                receivableBillDetails.get(i).setCreatedOrderTime(DateUtils.convert2Date(receiveBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_PATTERN));
                receivableBillDetails.get(i).setMakeUser(form.getLoginUserName());
                receivableBillDetails.get(i).setMakeTime(LocalDateTime.now());
                receivableBillDetails.get(i).setCreatedUser(form.getLoginUserName());
                receivableBillDetails.get(i).setBillId(orderReceivableBill.getId());
            }
            result = receivableBillDetailService.saveBatch(receivableBillDetails);
            if(!result){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存费用维度的金额信息  以结算币种进行转换后保存
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                orderBillCostTotalVO.setBillNo(form.getBillNo());
                orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                BigDecimal money = orderBillCostTotalVO.getMoney();//录入费用时的金额
                BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();
                if(exchangeRate == null || exchangeRate.compareTo(new BigDecimal("0")) == 0){
                    exchangeRate = new BigDecimal("1");
                }
                money = money.multiply(exchangeRate);
                orderBillCostTotalVO.setMoney(money);
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO,OrderBillCostTotal.class);
                orderBillCostTotal.setLocalMoney(orderBillCostTotalVO.getLocalMoney());//本币金额
                orderBillCostTotal.setMoneyType("2");
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
            if(!result){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        return CommonResult.success();
    }

    @Override
    public List<ViewBilToOrderVO> viewReceiveBill(List<Long> costIds) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewReceiveBill(costIds);
        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);
        for (ViewBilToOrderVO viewBillToOrder : orderList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())){
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if(strs.length > 1){
                    viewBillToOrder.setEndAddress(getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo()) &&
                        viewBillToCostClass.getOrderNo().equals(viewBillToOrder.getOrderNo()))
                        || ((!StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo())) && viewBillToOrder.getSubOrderNo().equals(viewBillToCostClass.getSubOrderNo()))){
                    try {
                        String addProperties = "";
                        String addValue = "";
                        Map<String,Object> propertiesMap = new HashMap<String,Object>();
                        Class cls = viewBillToCostClass.getClass();
                        Field[] fields = cls.getDeclaredFields();
                        for (int i = 0; i < fields.length; i++) {
                            Field f = fields[i];
                            f.setAccessible(true);
                            if("name".equals(f.getName())){
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if("money".equals(f.getName())){
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        viewBillToOrder = (ViewBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            newOrderList.add(viewBillToOrder);
        }
        return newOrderList;
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
        }catch (Exception e){
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
    public ViewBillVO getViewBillByCostIds(List<Long> costIds,String cmd) {
        return baseMapper.getViewBillByCostIds(costIds,cmd);
    }


    @Override
    public Integer getSBillOrderNum(String legalName, String unitAccount, String subType) {
        return baseMapper.getSBillOrderNum(legalName,unitAccount,subType);
    }

    @Override
    public BigDecimal getSAlreadyPaidAmount(String legalName, String unitAccount, String subType) {
        return baseMapper.getSAlreadyPaidAmount(legalName,unitAccount,subType);
    }

    @Override
    public Integer getSBillNum(String legalName, String unitAccount, String subType) {
        return baseMapper.getSBillNum(legalName,unitAccount,subType);
    }


    @Override
    public List<Long> findSaveConfirmData(List<Long> costIds) {
        return baseMapper.findSaveConfirmData(costIds);
    }

    @Override
    public String getWarehouseAddress(String orderNo) {
        return baseMapper.getWarehouseAddress(orderNo);
    }


}
