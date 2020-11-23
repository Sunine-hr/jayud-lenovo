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
import com.jayud.finance.mapper.OrderReceivableBillDetailMapper;
import com.jayud.finance.po.*;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillService;
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

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class OrderReceivableBillDetailServiceImpl extends ServiceImpl<OrderReceivableBillDetailMapper, OrderReceivableBillDetail> implements IOrderReceivableBillDetailService {

    @Autowired
    OmsClient omsClient;

    @Autowired
    IOrderReceivableBillService receivableBillService;

    @Autowired
    IOrderBillCostTotalService costTotalService;

    @Override
    public IPage<OrderPaymentBillDetailVO> findReceiveBillDetailByPage(QueryPaymentBillDetailForm form) {
        //定义分页参数
        Page<OrderPaymentBillDetailVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orbd.created_time"));
        IPage<OrderPaymentBillDetailVO> pageInfo = baseMapper.findReceiveBillDetailByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<OrderPaymentBillDetailVO> findReceiveBillDetail(QueryPaymentBillDetailForm form) {
        return baseMapper.findReceiveBillDetailByPage(form);
    }

    @Override
    public CommonResult submitSCw(ListForm form) {
        //参数校验
        if(form.getBillNos() == null || form.getBillNos().size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //校验数据状态
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderReceivableBillDetail> billDetails = baseMapper.selectList(queryWrapper);
        for (OrderReceivableBillDetail billDetail : billDetails) {
            if(!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
            orderReceivableBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
            orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no",billNo);
            update(orderReceivableBillDetail,updateWrapper);

            //保存操作记录
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(billNo);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
        }
        return CommonResult.success();
    }

    @Override
    public Boolean applyInvoice(ApplyInvoiceForm form) {
        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        orderReceivableBillDetail.setInvoiceAmount(form.getInvoiceAmount());
        orderReceivableBillDetail.setApplyStatus(BillEnum.F_1.getCode());
        orderReceivableBillDetail.setAuditStatus(BillEnum.B_5.getCode());
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());

        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("开票申请确认");
        auditInfoForm.setAuditStatus(BillEnum.B_5.getCode());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        return update(orderReceivableBillDetail,updateWrapper);
    }

    @Override
    public Boolean applyInvoiceCancel(String billNo) {
        OrderReceivableBillDetail orderReceiveBillDetail = new OrderReceivableBillDetail();
        orderReceiveBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderReceiveBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceiveBillDetail.setUpdatedUser(UserOperator.getToken());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        return update(orderReceiveBillDetail,queryWrapper);
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findEditSBillByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findEditSBillByPage(page, form);
        return pageInfo;
    }

    @Override
    public Boolean editSBill(EditSBillForm form) {
        //如果该账单编号下的现有订单都删除光了，不处理，让她重新出账
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderReceivableBillDetail> receivableBillDetails = baseMapper.selectList(queryWrapper);
        if(receivableBillDetails.size() == 0){
            return false;
        }
        //处理需要删除的费用
        List<OrderReceiveBillDetailForm> delCosts = form.getDelCosts();
        List<Long> delCostIds = new ArrayList<>();
        for (OrderReceiveBillDetailForm billDetail : delCosts) {
            delCostIds.add(billDetail.getCostId());
        }
        if(delCostIds.size() > 0){
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.eq("bill_no",form.getBillNo());
            removeWrapper.in("costId",delCostIds);
            remove(removeWrapper);

            //相应的费用录入也需要做记录
            OprCostBillForm oprCostBillForm = new OprCostBillForm();
            oprCostBillForm.setCmd("del");
            oprCostBillForm.setOprType("receivable");
            oprCostBillForm.setCostIds(delCostIds);
            omsClient.oprCostBill(oprCostBillForm);

        }
        //处理要新增的费用
        if(form.getReceiveBillDetailForms().size() > 0) {
            Boolean result = true;
            List<OrderReceiveBillDetailForm> receiveBillDetailForms = form.getReceiveBillDetailForms();//账单详细信息
            OrderReceivableBill orderReceivableBill = receivableBillService.getById(form.getBillId());//获取账单信息
            //生成账单需要修改order_receivable_cost表的is_bill
            List<Long> costIds = new ArrayList<>();
            for (OrderReceiveBillDetailForm receiveBillDetail : receiveBillDetailForms) {
                costIds.add(receiveBillDetail.getCostId());
            }
            if(costIds.size() > 0){
                OprCostBillForm oprCostBillForm = new OprCostBillForm();
                oprCostBillForm.setCmd("create");//这里的保存相当于生成对账单
                oprCostBillForm.setCostIds(costIds);
                oprCostBillForm.setOprType("receivable");
                result = omsClient.oprCostBill(oprCostBillForm).getData();
                if(!result){
                    return false;
                }
            }
            //生成对账单数据
            //对现有账单信息进行修改
            //1.统计已出账金额alreadyPaidAmount
            BigDecimal nowBillAmount = receiveBillDetailForms.stream().map(OrderReceiveBillDetailForm::getLocalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            orderReceivableBill.setAlreadyPaidAmount(orderReceivableBill.getAlreadyPaidAmount().add(nowBillAmount));
            //2.统计已出账订单数billOrderNum
            Integer billOrderNum = receivableBillService.getSBillOrderNum(orderReceivableBill.getLegalName(), orderReceivableBill.getCustomerName(), "create");
            orderReceivableBill.setBillOrderNum(billOrderNum);
            //3.统计账单数billNum
            orderReceivableBill.setBillOrderNum(orderReceivableBill.getBillNum() + 1);
            orderReceivableBill.setUpdatedTime(LocalDateTime.now());
            orderReceivableBill.setUpdatedUser(UserOperator.getToken());
            result = receivableBillService.updateById(orderReceivableBill);
            if (!result) {
                return false;
            }
            //开始保存对账单详情数据
            //获取剩余旧数据的状态和结算期和结算币种,账单编号维度
            OrderReceivableBillDetail oldSBillDetail = receivableBillDetails.get(0);
            List<OrderReceivableBillDetail> receiveBillDetails = ConvertUtil.convertList(receiveBillDetailForms, OrderReceivableBillDetail.class);
            for (int i = 0; i < receiveBillDetails.size(); i++) {
                receiveBillDetails.get(i).setStatus("1");
                receiveBillDetails.get(i).setBillNo(form.getBillNo());
                receiveBillDetails.get(i).setBeginAccountTerm(oldSBillDetail.getBeginAccountTerm());
                receiveBillDetails.get(i).setEndAccountTerm(oldSBillDetail.getEndAccountTerm());
                receiveBillDetails.get(i).setSettlementCurrency(oldSBillDetail.getSettlementCurrency());
                receiveBillDetails.get(i).setAuditStatus("edit_no_commit");//编辑保存未提交的，给前台做区分
                receiveBillDetails.get(i).setCreatedOrderTime(DateUtils.stringToDate(receiveBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_PATTERN));
                receiveBillDetails.get(i).setMakeUser(UserOperator.getToken());
                receiveBillDetails.get(i).setMakeTime(LocalDateTime.now());
                receiveBillDetails.get(i).setCreatedUser(UserOperator.getToken());
            }
            result = saveOrUpdateBatch(receiveBillDetails);
            if (!result) {
                return false;
            }
            //删除旧的费用
            QueryWrapper removeWrapper = new QueryWrapper();
            removeWrapper.in("cost_id",costIds);
            costTotalService.remove(removeWrapper);
            //开始保存费用维度的金额信息  以结算币种进行转换后保存
            List<OrderBillCostTotal> orderBillCostTotals = new ArrayList<>();
            //根据费用ID统计费用信息,将原始费用信息根据结算币种进行转换
            String settlementCurrency = oldSBillDetail.getSettlementCurrency();
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderSBillCostTotal(costIds, settlementCurrency);
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
        if("submit".equals(form.getCmd())){//提交
            editSBillSubmit(form.getBillNo());
        }
        return true;
    }

    @Override
    public Boolean editSBillSubmit(String billNo) {
        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(billNo);
        auditInfoForm.setAuditTypeDesc("编辑对账单提交财务审核");
        auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);

        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        orderReceivableBillDetail.setAuditStatus(BillEnum.B_3.getCode());
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",billNo);
        return update(orderReceivableBillDetail,updateWrapper);
    }

    @Override
    public List<ViewBilToOrderVO> viewSBillDetail(String billNo) {
        List<ViewBilToOrderVO> orderList = baseMapper.viewSBillDetail(billNo);
        List<ViewBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findSCostClass(billNo);
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
    public List<SheetHeadVO> findSSheetHead(String billNo) {
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
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSSheetHead(billNo);
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public ViewBillVO getViewSBill(String billNo) {
        return baseMapper.getViewSBill(billNo);
    }

    @Override
    public Boolean billSAudit(BillAuditForm form) {
        OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
        String auditStatus = "";
        if("0".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2.getCode();
        }else if("1".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2_1.getCode();
        }
        orderReceivableBillDetail.setAuditStatus(auditStatus);
        orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
        orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        Boolean result = update(orderReceivableBillDetail,updateWrapper);
        if(!result){
            return false;
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditTypeDesc("应收对账单审核");
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
        auditInfoForm.setAuditUser(UserOperator.getToken());
        omsClient.saveAuditInfo(auditInfoForm);
        return true;
    }

    @Override
    public Boolean contrarySAudit(ListForm form) {
        if("kf_f_reject".equals(form.getCmd())){
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderReceivableBillDetail,updateWrapper);
            }
        }else if("cw_f_reject".equals(form.getCmd())){
            for (String billNo : form.getBillNos()) {
                OrderReceivableBillDetail orderReceivableBillDetail = new OrderReceivableBillDetail();
                orderReceivableBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderReceivableBillDetail.setUpdatedTime(LocalDateTime.now());
                orderReceivableBillDetail.setUpdatedUser(UserOperator.getToken());
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",billNo);
                update(orderReceivableBillDetail,updateWrapper);
            }
        }
        return true;
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findSBillAuditByPage(QueryFBillAuditForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("orc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findSBillAuditByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<PaymentNotPaidBillVO> findSBillAudit(QueryFBillAuditForm form) {
        return baseMapper.findSBillAuditByPage(form);
    }


    @Override
    public List<FCostVO> findSCostList(String billNo) {
        return baseMapper.findSCostList(billNo);
    }

    @Override
    public Boolean auditSInvoice(BillAuditForm form) {
        OrderReceivableBillDetail billDetail = new OrderReceivableBillDetail();
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
            auditInfoForm.setAuditTypeDesc("收款审核");
            auditInfoForm.setAuditStatus(applyStatus);
            auditInfoForm.setAuditComment(form.getAuditComment());
            auditInfoForm.setExtDesc("order_receivable_bill_detail表bill_no");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
            return true;
        }else{
            return false;
        }
    }


}
