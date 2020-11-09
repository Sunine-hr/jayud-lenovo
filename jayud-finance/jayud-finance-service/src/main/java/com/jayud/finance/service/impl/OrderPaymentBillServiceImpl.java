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
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderPaymentBillService;
import com.jayud.finance.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    IOrderBillCostTotalService costTotalService;

    @Override
    public IPage<OrderPaymentBillVO> findPaymentBillByPage(QueryPaymentBillForm form) {
        //定义分页参数
        Page<OrderPaymentBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("oi.legal_name"));
        IPage<OrderPaymentBillVO> pageInfo = null;
        if("main".equals(form.getCmd())) {
            pageInfo = baseMapper.findPaymentBillByPage(page, form);
        }else if("zgys".equals(form.getCmd())){

        }else if("bg".equals(form.getCmd())){

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
        page.addOrder(OrderItem.desc("opc.id"));
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
        if(!form.getCmd().contains("pre")){
            //先保存对账单信息，在保存对账单详情信息
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
            paymentBillForm.setAlreadyPaidAmount(paymentBillForm.getAlreadyPaidAmount().add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            Integer billOrderNum = baseMapper.getBillOrderNum(paymentBillForm.getLegalName(),paymentBillForm.getCustomerName(),form.getCmd());
            paymentBillForm.setBillOrderNum(billOrderNum);
            //3.统计账单数billNum
            paymentBillForm.setBillOrderNum(paymentBillForm.getBillNum() + 1);
            OrderPaymentBill orderPaymentBill = ConvertUtil.convert(paymentBillForm,OrderPaymentBill.class);
            //判断该法人主体和客户是否已经生成过账单
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("legal_name",paymentBillForm.getLegalName());
            queryWrapper.eq("customer_name",paymentBillForm.getCustomerName());
            OrderPaymentBill existBill = baseMapper.selectOne(queryWrapper);
            if(existBill != null && existBill.getId() != null){
                orderPaymentBill.setId(existBill.getId());
                orderPaymentBill.setUpdatedTime(LocalDateTime.now());
                orderPaymentBill.setUpdatedUser(UserOperator.getToken());
            }
            if("create".equals(form.getCmd())){
                orderPaymentBill.setIsMain(true);
            }else if("create_zgys".equals(form.getCmd())){
                orderPaymentBill.setIsMain(false);
                orderPaymentBill.setSubType(CommonConstant.ZGYS);
            }else if("create_bg".equals(form.getCmd())){
                orderPaymentBill.setIsMain(false);
                orderPaymentBill.setSubType(CommonConstant.BG);
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
                paymentBillDetails.get(i).setAuditStatus(BillEnum.B_1.getCode());
                paymentBillDetails.get(i).setCreatedOrderTime(DateUtils.str2LocalDateTime(paymentBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_TIME_PATTERN));
                paymentBillDetails.get(i).setMakeUser(UserOperator.getToken());
                paymentBillDetails.get(i).setMakeTime(LocalDateTime.now());
                paymentBillDetails.get(i).setCreatedUser(UserOperator.getToken());
            }
            result = paymentBillDetailService.saveBatch(paymentBillDetails);
            if(!result){
                return false;
            }
            //开始保存费用维度的金额信息  以结算币种进行转换后保存 TODO
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderBillCostTotal(costIds);
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                orderBillCostTotalVO.setBillNo(paymentBillDetailForms.get(0).getBillNo());
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO,OrderBillCostTotal.class);
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
        }
        return result;
    }

    @Override
    public List<ViewBilToOrderVO> viewPaymentBill(ViewBillForm form) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewPaymentBill(form);
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(form);
        for (ViewBilToOrderVO viewBillToOrder : orderList) {
            List<ViewBillToCostClassVO> tempList = new ArrayList<>();
            for(ViewBillToCostClassVO viewBillToCostClass : findCostClass){
                if(viewBillToOrder.getOrderNo().equals(viewBillToCostClass.getOrderNo())){
                   tempList.add(viewBillToCostClass);
                }
            }
            viewBillToOrder.setCostClassVOList(tempList);
        }
        return orderList;
    }

    @Override
    public List<SheetHeadVO> findSheetHead(ViewBillForm form) {
        return baseMapper.findSheetHead(form);
    }

    @Override
    public ViewBillVO getViewBill(ViewBillForm form) {
        return null;
    }
}
