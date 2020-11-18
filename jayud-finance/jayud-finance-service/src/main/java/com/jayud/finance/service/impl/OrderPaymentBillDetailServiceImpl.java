package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderPaymentBillDetailMapper;
import com.jayud.finance.po.*;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderPaymentBillDetailServiceImpl extends ServiceImpl<OrderPaymentBillDetailMapper, OrderPaymentBillDetail> implements IOrderPaymentBillDetailService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    IOrderBillCostTotalService billCostTotalService;

    @Autowired
    IOrderPaymentBillService orderPaymentBillService;

    @Autowired
    ICancelAfterVerificationService verificationService;

    @Autowired
    IMakeInvoiceService makeInvoiceService;

    @Autowired
    IOrderBillCostTotalService costTotalService;

    @Override
    public IPage<OrderPaymentBillDetailVO> findPaymentBillDetailByPage(QueryPaymentBillDetailForm form) {
        //定义分页参数
        Page<OrderPaymentBillDetailVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opbd.created_time"));
        IPage<OrderPaymentBillDetailVO> pageInfo = baseMapper.findPaymentBillDetailByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<OrderPaymentBillDetailVO> findPaymentBillDetail(QueryPaymentBillDetailForm form) {
        return baseMapper.findPaymentBillDetailByPage(form);
    }


    @Override
    public CommonResult submitFCw(ListForm form) {
        //参数校验
        if(form.getBillNos() == null || form.getBillNos().size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //校验数据状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderPaymentBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        for (OrderPaymentBillDetail billDetail : billDetails) {
            if(!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
            orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
            orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no",billNo);
            update(orderPaymentBillDetail,updateWrapper);

            //保存操作记录in
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(billNo);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
        }
        return CommonResult.success();
    }


    @Override
    public Boolean applyPayment(ApplyPaymentForm form) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setPaymentAmount(form.getPaymentAmount());
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_1.getCode());
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_5.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("付款申请确认");
        auditInfoForm.setAuditStatus(BillEnum.B_5.getCode());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        return update(orderPaymentBillDetail,updateWrapper);
    }

    @Override
    public Boolean applyPaymentCancel(String billNo) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        return update(orderPaymentBillDetail,queryWrapper);
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findEditBillByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findEditBillByPage(page, form);
        return pageInfo;
    }

    @Override
    public Boolean editBill(EditBillForm form) {
        //如果该账单编号下的现有订单都删除光了，不处理，让她重新出账
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderPaymentBillDetail> paymentBillDetailList = baseMapper.selectList(queryWrapper);
        if(paymentBillDetailList.size() == 0){
            return false;
        }
        //处理需要删除的费用
        List<Long> delCostIds = form.getDelCostIds();
        if(delCostIds.size() > 0){
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.eq("bill_no",form.getBillNo());
            removeWrapper.in("costId",form.getDelCostIds());
            remove(removeWrapper);

            //相应的费用录入也需要做记录
            OprCostBillForm oprCostBillForm = new OprCostBillForm();
            oprCostBillForm.setCmd("del");
            oprCostBillForm.setOprType("payment");
            oprCostBillForm.setCostIds(delCostIds);
            omsClient.oprCostBill(oprCostBillForm);

        }
        //处理要新增的费用
        if(form.getPaymentBillDetailForms().size() > 0) {
            Boolean result = true;
            List<OrderPaymentBillDetailForm> paymentBillDetailForms = form.getPaymentBillDetailForms();//账单详细信息
            OrderPaymentBill orderPaymentBill = orderPaymentBillService.getById(form.getBillId());//获取账单信息
            //生成账单需要修改order_payment_cost表的is_bill
            List<Long> costIds = new ArrayList<>();
            for (OrderPaymentBillDetailForm paymentBillDetail : paymentBillDetailForms) {
                costIds.add(paymentBillDetail.getCostId());
            }
            if(costIds.size() > 0){
                OprCostBillForm oprCostBillForm = new OprCostBillForm();
                oprCostBillForm.setCmd("create");//这里的保存相当于生成对账单
                oprCostBillForm.setCostIds(costIds);
                oprCostBillForm.setOprType("payment");
                result = omsClient.oprCostBill(oprCostBillForm).getData();
                if(!result){
                    return false;
                }
            }
            //生成对账单数据
            //对现有账单信息进行修改
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = paymentBillDetailForms.stream().map(OrderPaymentBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderPaymentBill.setAlreadyPaidAmount(orderPaymentBill.getAlreadyPaidAmount().add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            Integer billOrderNum = orderPaymentBillService.getBillOrderNum(orderPaymentBill.getLegalName(), orderPaymentBill.getSupplierChName(), "create");
            orderPaymentBill.setBillOrderNum(billOrderNum);
            //3.统计账单数billNum
            orderPaymentBill.setBillOrderNum(orderPaymentBill.getBillNum() + 1);
            orderPaymentBill.setUpdatedTime(LocalDateTime.now());
            orderPaymentBill.setUpdatedUser(UserOperator.getToken());
            result = orderPaymentBillService.updateById(orderPaymentBill);
            if (!result) {
                return false;
            }
            //开始保存对账单详情数据
            //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
            OrderPaymentBillDetail oldFBillDetail = paymentBillDetailList.get(0);
            List<OrderPaymentBillDetail> paymentBillDetails = ConvertUtil.convertList(paymentBillDetailForms, OrderPaymentBillDetail.class);
            for (int i = 0; i < paymentBillDetails.size(); i++) {
                paymentBillDetails.get(i).setStatus("1");
                paymentBillDetails.get(i).setBillNo(form.getBillNo());
                paymentBillDetails.get(i).setBeginAccountTerm(oldFBillDetail.getBeginAccountTerm());
                paymentBillDetails.get(i).setEndAccountTerm(oldFBillDetail.getEndAccountTerm());
                paymentBillDetails.get(i).setSettlementCurrency(oldFBillDetail.getSettlementCurrency());
                paymentBillDetails.get(i).setAuditStatus(oldFBillDetail.getAuditStatus());
                paymentBillDetails.get(i).setCreatedOrderTime(DateUtils.str2LocalDateTime(paymentBillDetailForms.get(i).getCreatedTimeStr(), DateUtils.DATE_TIME_PATTERN));
                paymentBillDetails.get(i).setMakeUser(UserOperator.getToken());
                paymentBillDetails.get(i).setMakeTime(LocalDateTime.now());
                paymentBillDetails.get(i).setCreatedUser(UserOperator.getToken());
            }
            result = saveBatch(paymentBillDetails);
            if (!result) {
                return false;
            }
            //开始保存费用维度的金额信息  以结算币种进行转换后保存
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
            String settlementCurrency = oldFBillDetail.getSettlementCurrency();
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency);
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                orderBillCostTotalVO.setBillNo(form.getBillNo());
                orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                BigDecimal money = orderBillCostTotalVO.getMoney().multiply(orderBillCostTotalVO.getExchangeRate());
                orderBillCostTotalVO.setMoney(money);
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                orderBillCostTotal.setMoneyType("1");
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean editBillSubmit(String billNo) {
        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(billNo);
        auditInfoForm.setAuditTypeDesc("编辑对账单提交财务审核");
        auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",billNo);
        return update(orderPaymentBillDetail,updateWrapper);
    }

    @Override
    public List<ViewBilToOrderVO> viewBillDetail(String billNo) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewBillDetail(billNo);
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(billNo);
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
    public List<SheetHeadVO> findSheetHead(String billNo) {
        return baseMapper.findSheetHead(billNo);
    }

    @Override
    public ViewBillVO getViewBill(String billNo) {
        return baseMapper.getViewBill(billNo);
    }

    @Override
    public Boolean billAudit(BillAuditForm form) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        String auditStatus = "";
        if("0".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2.getCode();
        }else if("1".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2_1.getCode();
        }
        orderPaymentBillDetail.setAuditStatus(auditStatus);
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        Boolean result = update(orderPaymentBillDetail,updateWrapper);
        if(!result){
            return false;
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("应付对账单审核");
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);
        return true;
    }

    /**
     * //1.客服主管-应收反审核 kf_s_reject
     * 2.客服主管-应付反审核 kf_f_reject
     * //3.财务-应收反审核 cw_s_reject
     * 4.财务-应付反审核 cw_f_reject
     *   ①未申请开票或付款的或作废的才可进行反审核
     */
    @Override
    public Boolean contraryAudit(ListForm form) {
        if("kf_f_reject".equals(form.getCmd())){
            for (String billNo : form.getBillNos()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderPaymentBillDetail,updateWrapper);
            }
        }else if("cw_f_reject".equals(form.getCmd())){
            for (String billNo : form.getBillNos()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderPaymentBillDetail,updateWrapper);
            }
        }
        return true;
    }

    @Override
    public IPage<FinanceAccountVO> findFinanceAccountByPage(QueryFinanceAccountForm form) {
        //定义分页参数
        Page<FinanceAccountVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("temp.createTimeStr"));
        IPage<FinanceAccountVO> pageInfo = baseMapper.findFinanceAccountByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<FinanceAccountVO> findFinanceAccount(QueryFinanceAccountForm form) {
        return baseMapper.findFinanceAccountByPage(form);
    }


    @Override
    public IPage<PaymentNotPaidBillVO> findFBillAuditByPage(QueryFBillAuditForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findFBillAuditByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<PaymentNotPaidBillVO> findFBillAudit(QueryFBillAuditForm form) {
        return baseMapper.findFBillAuditByPage(form);
    }

    @Override
    public List<FCostVO> findFCostList(String billNo) {
        return baseMapper.findFCostList(billNo);
    }

    @Override
    public Boolean auditFInvoice(BillAuditForm form) {
        OrderPaymentBillDetail billDetail = new OrderPaymentBillDetail();
        String applyStatus = "";
        String status = "";
        if("0".equals(form.getAuditStatus())){
            applyStatus = BillEnum.F_2.getCode();
            status = BillEnum.B_6.getCode();
        }else {
            applyStatus = BillEnum.F_3.getCode();
            status = BillEnum.B_6_1.getCode();
        }
        billDetail.setApplyStatus(applyStatus);
        billDetail.setAuditStatus(status);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        Integer num = baseMapper.update(billDetail,updateWrapper);
        if(num > 0){
            //保存审核信息
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(form.getBillNo());
            auditInfoForm.setAuditTypeDesc("付款审核");
            auditInfoForm.setAuditStatus(applyStatus);
            auditInfoForm.setAuditComment(form.getAuditComment());
            auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
            return true;
        }else{
            return false;
        }
    }


}
