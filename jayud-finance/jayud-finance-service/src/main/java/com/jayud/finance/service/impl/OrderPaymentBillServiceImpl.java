package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
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
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.util.ReflectUtil;
import com.jayud.finance.vo.*;
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
    IOrderBillCostTotalService costTotalService;

    @Override
    public IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form) {
        //定义分页参数
        Page<OrderPaymentBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oi.legal_name"));
        IPage<OrderPaymentBillVO> pageInfo = null;
        if("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findPaymentBillByPage(page, form);//法人主体/供应商/可汇总主订单费用的维度统计
        }else if("zgys".equals(form.getCmd()) || "bg".equals(form.getCmd())){
            pageInfo = baseMapper.findPaymentSubBillByPage(page, form);//法人主体/供应商/子订单费用的维度统计
        }
        return pageInfo;
    }

    @Override
    public Map<String, Object> findPaymentBillNum(QueryPaymentBillNumForm form) {
        List<OrderPaymentBillNumVO> resultList = baseMapper.findPaymentBillNum(form);
        Map<String, Object> result = new HashMap<>();
        result.put(CommonConstant.LIST,resultList);
        result.put(CommonConstant.BILL_NUM_TOTAL,resultList.stream().collect(Collectors.summarizingInt(OrderPaymentBillNumVO::getBillNum)));//订单数合计
        result.put(CommonConstant.RMB_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getRmb).reduce(BigDecimal.ZERO,BigDecimal::add));//人民币合计
        result.put(CommonConstant.DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//美元合计
        result.put(CommonConstant.EURO_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getEuro).reduce(BigDecimal.ZERO,BigDecimal::add));//欧元合计
        result.put(CommonConstant.HK_DOLLAR_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getHKDollar).reduce(BigDecimal.ZERO,BigDecimal::add));//港币合计
        result.put(CommonConstant.LOCAL_AMOUNT_TOTAL,resultList.stream().map(OrderPaymentBillNumVO::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//本币金额合计
        result.put(CommonConstant.HE_XIAO_AMOUNT,resultList.stream().map(OrderPaymentBillNumVO::getHeXiaoAmount).reduce(BigDecimal.ZERO,BigDecimal::add));//已收金额，即财务已核销金额合计
        return result;
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findNotPaidBillByPage(QueryNotPaidBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.costId"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findNotPaidBillByPage(page, form);
        return pageInfo;
    }

    @Override
    public Boolean createPaymentBill(CreatePaymentBillForm form) {
        OrderPaymentBillForm paymentBillForm = form.getPaymentBillForm();//账单信息
        List<OrderPaymentBillDetailForm> paymentBillDetailForms = form.getPaymentBillDetailForms();//账单详细信息
        Boolean result = true;
        //无论暂存还是生成账单都需要修改order_payment_cost表的is_bill
        List<Long> costIds = new ArrayList<>();
        for (OrderPaymentBillDetailForm paymentBillDetail : paymentBillDetailForms) {
            costIds.add(paymentBillDetail.getCostId());
        }
        if(costIds.size() > 0){
            OprCostBillForm oprCostBillForm = new OprCostBillForm();
            oprCostBillForm.setCmd(form.getCmd());
            oprCostBillForm.setCostIds(costIds);
            oprCostBillForm.setOprType("payment");
            result = omsClient.oprCostBill(oprCostBillForm).getData();
            if(!result){
                return false;
            }
        }
        //生成账单操作才是生成对账单数据
        if("create".equals(form.getCmd())){
            //先保存对账单信息，在保存对账单详情信息
            OrderPaymentBill orderPaymentBill = ConvertUtil.convert(paymentBillForm,OrderPaymentBill.class);
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            orderPaymentBill.setAlreadyPaidAmount(paymentBillForm.getAlreadyPaidAmount().add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            Integer billOrderNum = getBillOrderNum(paymentBillForm.getLegalName(),paymentBillForm.getSupplierChName(),form.getCmd());
            orderPaymentBill.setBillOrderNum(billOrderNum);
            //3.统计账单数billNum
            orderPaymentBill.setBillOrderNum(paymentBillForm.getBillNum() + 1);
            if("main".equals(form.getSubType())){
                orderPaymentBill.setIsMain(true);
            }else if("zgys".equals(form.getSubType())){
                orderPaymentBill.setIsMain(false);
            }else if("bg".equals(form.getSubType())){
                orderPaymentBill.setIsMain(false);
            }
            orderPaymentBill.setSubType(form.getSubType());
            //判断该法人主体和客户是否已经生成过账单
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("sub_type",form.getSubType());
            queryWrapper.eq("legal_name",paymentBillForm.getLegalName());
            queryWrapper.eq("supplier_ch_name",paymentBillForm.getSupplierChName());
            OrderPaymentBill existBill = baseMapper.selectOne(queryWrapper);
            if(existBill != null && existBill.getId() != null){
                orderPaymentBill.setId(existBill.getId());
                orderPaymentBill.setUpdatedTime(LocalDateTime.now());
                orderPaymentBill.setUpdatedUser(UserOperator.getToken());
            }
            orderPaymentBill.setCreatedUser(UserOperator.getToken());
            result = saveOrUpdate(orderPaymentBill);
            if(!result){
                return false;
            }
            //开始保存对账单详情数据
            List<OrderPaymentBillDetail> paymentBillDetails = ConvertUtil.convertList(paymentBillDetailForms,OrderPaymentBillDetail.class);
            for (int i = 0;i<paymentBillDetails.size();i++) {
                paymentBillDetails.get(i).setStatus("1");
                paymentBillDetails.get(i).setBillNo(form.getBillNo());
                paymentBillDetails.get(i).setBeginAccountTerm(DateUtils.str2LocalDateTime(form.getBeginAccountTermStr(),DateUtils.DATE_TIME_PATTERN));
                paymentBillDetails.get(i).setEndAccountTerm(DateUtils.str2LocalDateTime(form.getEndAccountTermStr(),DateUtils.DATE_TIME_PATTERN));
                paymentBillDetails.get(i).setSettlementCurrency(form.getSettlementCurrency());
                paymentBillDetails.get(i).setAuditStatus(BillEnum.B_1.getCode());
                paymentBillDetails.get(i).setCreatedOrderTime(DateUtils.convert2Date(paymentBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_PATTERN));
                paymentBillDetails.get(i).setMakeUser(form.getLoginUserName());
                paymentBillDetails.get(i).setMakeTime(LocalDateTime.now());
                paymentBillDetails.get(i).setCreatedUser(form.getLoginUserName());
                paymentBillDetails.get(i).setBillId(orderPaymentBill.getId());
            }
            result = paymentBillDetailService.saveBatch(paymentBillDetails);
            if(!result){
                return false;
            }
            //开始保存费用维度的金额信息  以结算币种进行转换后保存
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
            String settlementCurrency = form.getSettlementCurrency();
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds,settlementCurrency);
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                orderBillCostTotalVO.setBillNo(form.getBillNo());
                orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                BigDecimal money = orderBillCostTotalVO.getMoney().multiply(orderBillCostTotalVO.getExchangeRate());
                orderBillCostTotalVO.setMoney(money);
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO,OrderBillCostTotal.class);
                orderBillCostTotal.setMoneyType("1");
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
        }
        return result;
    }

    @Override
    public List<ViewBilToOrderVO> viewPaymentBill(List<Long> costIds) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewPaymentBill(costIds);
        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(costIds);
        for (ViewBilToOrderVO viewBillToOrder : orderList) {
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if(viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo())){
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
                                addProperties = String.valueOf(f.get(viewBillToCostClass));//待新增得属性
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
    public ViewBillVO getViewBillByCostIds(List<Long> costIds) {
        return baseMapper.getViewBillByCostIds(costIds);
    }

    @Override
    public Integer getBillOrderNum(String legalName, String supplierChName, String cmd) {
        return baseMapper.getBillOrderNum(legalName,supplierChName,cmd);
    }

    @Override
    public Boolean isExistBillNo(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<OrderPaymentBillDetail> fBillDetails = paymentBillDetailService.list(queryWrapper);
        List<OrderReceivableBillDetail> sBillDetails = receivableBillDetailService.list(queryWrapper);
        Integer resultFCount = 0;
        Integer resultSCount = 0;
        if(fBillDetails != null){
            resultFCount = fBillDetails.size();
        }
        if(sBillDetails != null){
            resultSCount = sBillDetails.size();
        }
        Integer resultCount = resultFCount + resultSCount;
        if(resultCount > 0){
            return true;
        }
        return false;
    }


}
