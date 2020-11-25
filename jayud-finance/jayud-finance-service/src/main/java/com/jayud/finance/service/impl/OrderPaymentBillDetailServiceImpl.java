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
import com.jayud.finance.po.OrderBillCostTotal;
import com.jayud.finance.po.OrderPaymentBill;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.service.*;
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

import static java.util.stream.Collectors.toList;

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
        page.addOrder(OrderItem.desc("opbd.make_time"));
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
                return CommonResult.error(10001,"不符合操作条件");
            }
        }
        for (String billNo : form.getBillNos()) {
            OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
            orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
            orderPaymentBillDetail.setUpdatedUser(form.getLoginUserName());
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("bill_no",billNo);
            update(orderPaymentBillDetail,updateWrapper);

            //保存操作记录in
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtUniqueFlag(billNo);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
            auditInfoForm.setAuditUser(form.getLoginUserName());
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
        //可编辑的条件：客服主管审核对账单不通过,客服主管反审核对账单，财务审核对账单不通过，财务反审核
        OrderPaymentBillDetail existObject = paymentBillDetailList.get(0);
        if((!BillEnum.B_2_1.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_7.getCode().equals(existObject.getAuditStatus()) ||
                BillEnum.B_8.getCode().equals(existObject.getAuditStatus()) || BillEnum.B_4_1.getCode().equals(existObject.getAuditStatus()))){
            return false;
        }
        //处理需要删除的费用
        List<OrderPaymentBillDetailForm> delCosts = form.getDelCosts();
        List<Long> delCostIds = new ArrayList<>();
        for (OrderPaymentBillDetailForm billDetail : delCosts) {
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
            orderPaymentBill.setBillNum(orderPaymentBill.getBillNum() + 1);
            orderPaymentBill.setUpdatedTime(LocalDateTime.now());
            orderPaymentBill.setUpdatedUser(form.getLoginUserName());
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
                paymentBillDetails.get(i).setAuditStatus("edit_no_commit");
                paymentBillDetails.get(i).setCreatedOrderTime(DateUtils.convert2Date(paymentBillDetailForms.get(i).getCreatedTimeStr(),DateUtils.DATE_PATTERN));
                paymentBillDetails.get(i).setMakeUser(form.getLoginUserName());
                paymentBillDetails.get(i).setMakeTime(LocalDateTime.now());
                paymentBillDetails.get(i).setCreatedUser(form.getLoginUserName());
            }
            result = saveOrUpdateBatch(paymentBillDetails);
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
            String settlementCurrency = oldFBillDetail.getSettlementCurrency();
            List<OrderBillCostTotalVO> orderBillCostTotalVOS = costTotalService.findOrderFBillCostTotal(costIds, settlementCurrency);
            for (OrderBillCostTotalVO orderBillCostTotalVO : orderBillCostTotalVOS) {
                orderBillCostTotalVO.setBillNo(form.getBillNo());
                orderBillCostTotalVO.setCurrencyCode(settlementCurrency);
                BigDecimal localMoney = orderBillCostTotalVO.getMoney();//本币金额
                BigDecimal money = localMoney.multiply(orderBillCostTotalVO.getExchangeRate());
                orderBillCostTotalVO.setMoney(money);
                OrderBillCostTotal orderBillCostTotal = ConvertUtil.convert(orderBillCostTotalVO, OrderBillCostTotal.class);
                orderBillCostTotal.setLocalMoney(localMoney);
                orderBillCostTotal.setMoneyType("1");
                orderBillCostTotals.add(orderBillCostTotal);
            }
            result = costTotalService.saveBatch(orderBillCostTotals);
            if (!result) {
                return false;
            }
        }
        if("submit".equals(form.getCmd())){//提交
            editBillSubmit(form.getBillNo(),form.getLoginUserName());
        }
        return true;
    }

    @Override
    public Boolean editBillSubmit(String billNo,String loginUserName) {
        //保存操作记录
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtUniqueFlag(billNo);
        auditInfoForm.setAuditTypeDesc("编辑对账单提交到客服主管审核");
        auditInfoForm.setAuditStatus(BillEnum.B_1.getCode());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(loginUserName);
        omsClient.saveAuditInfo(auditInfoForm);

        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_1.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(loginUserName);
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",billNo);
        return update(orderPaymentBillDetail,updateWrapper);
    }

    @Override
    public List<ViewFBilToOrderVO> viewBillDetail(String billNo) {
        List<ViewFBilToOrderVO> orderList = baseMapper.viewBillDetail(billNo);
        List<ViewFBilToOrderVO> newOrderList = new ArrayList<>();
        List<ViewBillToCostClassVO> findCostClass = baseMapper.findCostClass(billNo);
        for (ViewFBilToOrderVO viewBillToOrder : orderList) {
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
                        viewBillToOrder = (ViewFBilToOrderVO) ReflectUtil.getObject(viewBillToOrder, propertiesMap);
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
    public List<SheetHeadVO> findSheetHead(String billNo) {
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
        }catch (Exception e){
            e.printStackTrace();
        }
        List<SheetHeadVO> dynamicHeadList = baseMapper.findSheetHead(billNo);
        for (SheetHeadVO sheetHead : dynamicHeadList) {
            sheetHead.setName(sheetHead.getName().toLowerCase());
        }
        allHeadList.addAll(fixHeadList);
        allHeadList.addAll(dynamicHeadList);
        return allHeadList;
    }

    @Override
    public ViewBillVO getViewBill(String billNo) {
        return baseMapper.getViewBill(billNo);
    }

    @Override
    public CommonResult billAudit(BillAuditForm form) {
        //审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderPaymentBillDetail> existList = baseMapper.selectList(queryWrapper);
        OrderPaymentBillDetail existObject = existList.get(0);
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        String auditStatus = "";
        if("audit".equals(form.getCmd())) {
            if(!BillEnum.B_1.getCode().equals(existObject.getAuditStatus())){
                return CommonResult.error(100001,"不符合审核条件");
            }
            if ("0".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2.getCode();
            } else if ("1".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_2_1.getCode();
            }
            auditInfoForm.setAuditTypeDesc("应付对账单审核");
        }else if("cw_audit".equals(form.getCmd())){//财务对账单审核
            if(!BillEnum.B_3.getCode().equals(existObject.getAuditStatus())){
                return CommonResult.error(100001,"不符合审核条件");
            }
            if ("0".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_4.getCode();
            } else if ("1".equals(form.getAuditStatus())) {
                auditStatus = BillEnum.B_4_1.getCode();
            }
            auditInfoForm.setAuditTypeDesc("财务对账单审核");
        }
        orderPaymentBillDetail.setAuditStatus(auditStatus);
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("bill_no",form.getBillNo());
        Boolean result = update(orderPaymentBillDetail,updateWrapper);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //记录审核信息
        auditInfoForm.setExtUniqueFlag(form.getBillNo());
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_payment_bill_detail表bill_no");
        auditInfoForm.setAuditUser(form.getLoginUserName());
        omsClient.saveAuditInfo(auditInfoForm);
        return CommonResult.success();
    }

    /**
     * //1.客服主管-应收反审核 kf_s_reject
     * 2.客服主管-应付反审核 kf_f_reject
     * //3.财务-应收反审核 cw_s_reject
     * 4.财务-应付反审核 cw_f_reject
     *   ①未申请开票或付款的或作废的才可进行反审核
     */
    @Override
    public CommonResult contraryAudit(ListForm form) {
        //反审核条件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderPaymentBillDetail> existList = baseMapper.selectList(queryWrapper);
        if("kf_f_reject".equals(form.getCmd())){
            for (OrderPaymentBillDetail existObject : existList) {
                if(!BillEnum.B_2.getCode().equals(existObject.getAuditStatus())){
                    return CommonResult.error(10001,"存在不符合操作条件的数据");
                }
            }
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
            for (OrderPaymentBillDetail existObject : existList) {
                if(!BillEnum.B_5_1.getCode().equals(existObject.getAuditStatus())){
                    return CommonResult.error(10001,"存在不符合操作条件的数据");
                }
            }
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
        return CommonResult.success();
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

    @Override
    public Boolean editFSaveConfirm(List<Long> costIds) {
        //在录入费用表的is_bill做标识为save_confirm
        return omsClient.editSaveConfirm(costIds,"payment","save_confirm").getData();
    }

    @Override
    public Boolean editFDel(List<Long> costIds) {
        //从删除的costIds里面挑出那种保存确定的数据,回滚到未出账状态
        List<Long> saveConfirmIds = orderPaymentBillService.findSaveConfirmData(costIds);
        Boolean result = omsClient.editSaveConfirm(saveConfirmIds,"payment","edit_del").getData();
        if(!result){
            return false;
        }
        //已存在的数据删除,只在账单详情表做记录
        List<Long> editDelIds = costIds.stream().filter(item -> !saveConfirmIds.contains(item)).collect(toList());
        OrderPaymentBillDetail paymentBillDetail = new OrderPaymentBillDetail();
        paymentBillDetail.setAuditStatus("edit_del");//持续操作中的过度状态
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.in("cost_id",editDelIds);
        result = update(paymentBillDetail,updateWrapper);
        if(!result){
            return false;
        }
        return true;
    }


}
