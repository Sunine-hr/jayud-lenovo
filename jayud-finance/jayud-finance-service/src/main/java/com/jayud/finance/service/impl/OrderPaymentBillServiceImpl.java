package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderPaymentBillMapper;
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
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

    @Override
    public IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form) {
        //定义分页参数
        Page<OrderPaymentBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        IPage<OrderPaymentBillVO> pageInfo = null;
        if ("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findPaymentBillByPage(page, form);//法人主体/供应商/可汇总主订单费用的维度统计
        } else if ("zgys".equals(form.getCmd()) || "bg".equals(form.getCmd())
                || "ky".equals(form.getCmd())) {
            pageInfo = baseMapper.findPaymentSubBillByPage(page, form);//法人主体/供应商/子订单费用的维度统计
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> findPaymentBillNum(QueryPaymentBillNumForm form) {
        List<OrderPaymentBillNumVO> resultList = baseMapper.findPaymentBillNum(form);
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
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findNotPaidBillByPage(page, form);
        List<PaymentNotPaidBillVO> pageList = pageInfo.getRecords();
        for (PaymentNotPaidBillVO paymentNotPaidBillVO : pageList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if(!StringUtil.isNullOrEmpty(paymentNotPaidBillVO.getEndAddress())){
                String[] strs = paymentNotPaidBillVO.getEndAddress().split(",");
                if(strs.length > 1){
                    paymentNotPaidBillVO.setEndAddress(receivableBillService.getWarehouseAddress(paymentNotPaidBillVO.getOrderNo()));
                }
            }
        }
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
            StringBuilder sb = new StringBuilder();
            sb.append("请配置[");
            Boolean flag = true;
            orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency, form.getAccountTermStr());
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                BigDecimal exchangeRate = orderBillCostTotalVO.getExchangeRate();//如果费率为0，则抛异常回滚数据
                if ((exchangeRate == null || exchangeRate.compareTo(new BigDecimal(0)) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals(settlementCurrency)) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                    String dCurrency = currencyRateService.getNameByCode(settlementCurrency);
                    sb.append("原始币种:" + oCurrency + ",兑换币种:" + dCurrency + ";");
                    flag = false;
                }
                if(orderBillCostTotalVO.getCurrencyCode().equals("CNY")){
                    orderBillCostTotalVO.setLocalMoney(orderBillCostTotalVO.getOldLocalMoney());
                }
            }
            //出账时要以结算期为汇率记录本币金额，需要校验是否配置汇率
            List<OrderBillCostTotalVO> tempOrderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, "CNY", form.getAccountTermStr());
            for (OrderBillCostTotalVO orderBillCostTotalVO : tempOrderBillCostTotalVOS) {
                BigDecimal localMoney = orderBillCostTotalVO.getLocalMoney();//如果本币金额为0，说明汇率为空没配置
                if ((localMoney == null || localMoney.compareTo(new BigDecimal("0")) == 0) && !orderBillCostTotalVO.getCurrencyCode().equals("CNY")) {
                    //根据币种查询币种描述
                    String oCurrency = currencyRateService.getNameByCode(orderBillCostTotalVO.getCurrencyCode());
                    sb.append("原始币种:"+oCurrency+",兑换币种:人民币;");
                    flag = false;
                }
            }
            if (!flag) {
                sb.append("]的汇率");
                return CommonResult.error(10001, sb.toString());
            }
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
        if("create".equals(form.getCmd()) && costIds.size() > 0){
            //先保存对账单信息，在保存对账单详情信息
            OrderPaymentBill orderPaymentBill = ConvertUtil.convert(paymentBillForm,OrderPaymentBill.class);
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal alreadyPaidAmount = getAlreadyPaidAmount(paymentBillForm.getLegalName(),paymentBillForm.getSupplierChName(),form.getSubType());
            orderPaymentBill.setAlreadyPaidAmount(alreadyPaidAmount.add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            List<String> validOrders = new ArrayList<>();
            orderNos = orderNos.stream().distinct().collect(Collectors.toList());
            for (String orderNo : orderNos) {
                List<OrderPaymentBillDetail> orderNoObjects= paymentBillDetailService.getNowFOrderExist(paymentBillForm.getLegalName(),paymentBillForm.getSupplierChName(),form.getSubType(),orderNo);
                if(orderNoObjects == null || orderNoObjects.size() == 0){
                    validOrders.add(orderNo);
                }
            }
            Integer nowBillOrderNum = validOrders.size();
            Integer billOrderNum = getBillOrderNum(paymentBillForm.getLegalName(), paymentBillForm.getSupplierChName(), form.getSubType());
            orderPaymentBill.setBillOrderNum(billOrderNum + nowBillOrderNum);
            //3.统计账单数billNum
            Integer billNum = getBillNum(paymentBillForm.getLegalName(), paymentBillForm.getSupplierChName(), form.getSubType());
            orderPaymentBill.setBillNum(billNum + 1);
            if ("main".equals(form.getSubType())) {
                orderPaymentBill.setIsMain(true);
            }else {
                orderPaymentBill.setIsMain(false);
            }
            orderPaymentBill.setSubType(form.getSubType());
            //判断该法人主体和客户是否已经生成过账单
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sub_type", form.getSubType());
            queryWrapper.eq("legal_name", paymentBillForm.getLegalName());
            queryWrapper.eq("supplier_ch_name", paymentBillForm.getSupplierChName());
            OrderPaymentBill existBill = baseMapper.selectOne(queryWrapper);
            if (existBill != null && existBill.getId() != null) {
                orderPaymentBill.setId(existBill.getId());
                orderPaymentBill.setUpdatedTime(LocalDateTime.now());
                orderPaymentBill.setUpdatedUser(UserOperator.getToken());
            }
            orderPaymentBill.setCreatedUser(UserOperator.getToken());
            result = saveOrUpdate(orderPaymentBill);
            if (!result) {
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
            //开始保存对账单详情数据
            List<OrderPaymentBillDetail> paymentBillDetails = ConvertUtil.convertList(paymentBillDetailForms, OrderPaymentBillDetail.class);
            for (int i = 0; i < paymentBillDetails.size(); i++) {
                paymentBillDetails.get(i).setStatus("1");
                paymentBillDetails.get(i).setBillNo(form.getBillNo());
                paymentBillDetails.get(i).setAccountTerm(form.getAccountTermStr());
                paymentBillDetails.get(i).setSettlementCurrency(form.getSettlementCurrency());
                paymentBillDetails.get(i).setAuditStatus(BillEnum.B_1.getCode());
                paymentBillDetails.get(i).setCreatedOrderTime(DateUtils.convert2Date(paymentBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_PATTERN));
                paymentBillDetails.get(i).setMakeUser(form.getLoginUserName());
                paymentBillDetails.get(i).setMakeTime(LocalDateTime.now());
                paymentBillDetails.get(i).setCreatedUser(form.getLoginUserName());
                paymentBillDetails.get(i).setBillId(orderPaymentBill.getId());
            }
            result = paymentBillDetailService.saveBatch(paymentBillDetails);
            if (!result) {
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
                orderBillCostTotal.setLocalMoney(orderBillCostTotalVO.getLocalMoney());
                orderBillCostTotal.setMoneyType("1");
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
    public List<ViewFBilToOrderVO> viewPaymentBill(List<Long> costIds) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewPaymentBill(costIds);
        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);
        for (ViewFBilToOrderVO viewBillToOrder : orderList) {
            //处理目的地:当有两条或两条以上时,则获取中转仓地址
            if (!StringUtil.isNullOrEmpty(viewBillToOrder.getEndAddress())) {
                String[] strs = viewBillToOrder.getEndAddress().split(",");
                if (strs.length > 1) {
                    viewBillToOrder.setEndAddress(receivableBillService.getWarehouseAddress(viewBillToOrder.getOrderNo()));
                }
            }
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if((StringUtil.isNullOrEmpty(viewBillToOrder.getSubOrderNo()) && StringUtil.isNullOrEmpty(viewBillToCostClass.getSubOrderNo())
                        && viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo()))
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
                            if ("name".equals(f.getName())) {
                                addProperties = String.valueOf(f.get(viewBillToCostClass)).toLowerCase();//待新增得属性
                            }
                            if ("money".equals(f.getName())) {
                                addValue = String.valueOf(f.get(viewBillToCostClass));//待新增属性得值
                            }
                            propertiesMap.put(addProperties, addValue);
                        }
                        viewBillToOrder = (ViewFBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
                    } catch (Exception e) {
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
    public ViewBillVO getViewBillByCostIds(List<Long> costIds,String cmd) {
        return baseMapper.getViewBillByCostIds(costIds,cmd);
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


}
