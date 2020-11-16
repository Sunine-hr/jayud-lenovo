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
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderPaymentBillDetailMapper;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.po.MakeInvoice;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public CommonResult submitFCw(ListForm form) {
        //校验数据状态
        List<OrderPaymentBillDetail> billDetails = baseMapper.selectBatchIds(form.getIds());
        for (OrderPaymentBillDetail billDetail : billDetails) {
            if(!BillEnum.B_2.getCode().equals(billDetail.getAuditStatus())){
                return CommonResult.error(ResultEnum.OPR_FAIL);
            }
        }
        for (Long id : form.getIds()) {
            OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
            orderPaymentBillDetail.setId(id);
            orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
            orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
            orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
            updateById(orderPaymentBillDetail);

            //保存操作记录
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtId(id);
            auditInfoForm.setAuditTypeDesc("提交财务");
            auditInfoForm.setAuditStatus(BillEnum.B_3.getCode());
            auditInfoForm.setExtDesc("order_payment_bill_detail表");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
        }
        return CommonResult.success();
    }


    @Override
    public Boolean applyPayment(ApplyPaymentForm form) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setId(form.getBillDetailId());
        orderPaymentBillDetail.setPaymentAmount(form.getPaymentAmount());
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_1.getCode());
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_5.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        return updateById(orderPaymentBillDetail);
    }

    @Override
    public Boolean applyPaymentCancel(Long billDetailId) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setId(billDetailId);
        orderPaymentBillDetail.setApplyStatus(BillEnum.F_4.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        return updateById(orderPaymentBillDetail);
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
            CreatePaymentBillForm billForm = new CreatePaymentBillForm();
            billForm.setCmd("create");
            billForm.setPaymentBillDetailForms(form.getPaymentBillDetailForms());
            OrderPaymentBillForm orderPaymentBillForm = new OrderPaymentBillForm();
            //根据账单号构建信息
            //TODO
            billForm.setPaymentBillForm(orderPaymentBillForm);
            orderPaymentBillService.createPaymentBill(billForm);
        }
        return true;
    }

    @Override
    public Boolean editBillSubmit(Long billDetailId) {
        OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
        orderPaymentBillDetail.setId(billDetailId);
        orderPaymentBillDetail.setAuditStatus(BillEnum.B_3.getCode());
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        return updateById(orderPaymentBillDetail);
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
        orderPaymentBillDetail.setId(form.getBillDetailId());
        String auditStatus = "";
        if("0".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2.getCode();
        }else if("1".equals(form.getAuditStatus())){
            auditStatus = BillEnum.B_2_1.getCode();
        }
        orderPaymentBillDetail.setAuditStatus(auditStatus);
        orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
        orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
        Boolean result = updateById(orderPaymentBillDetail);
        if(!result){
            return false;
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(form.getBillDetailId());
        auditInfoForm.setAuditTypeDesc("应付对账单审核");
        auditInfoForm.setAuditStatus(auditStatus);
        auditInfoForm.setAuditComment(form.getAuditComment());
        auditInfoForm.setExtDesc("order_payment_bill_detail表");
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
        List<OrderPaymentBillDetail> list = new ArrayList<>();
        if("kf_f_reject".equals(form.getCmd())){
            for (Long id : form.getIds()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setId(id);
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_7.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                list.add(orderPaymentBillDetail);
            }
        }else if("cw_f_reject".equals(form.getCmd())){
            for (Long id : form.getIds()) {
                OrderPaymentBillDetail orderPaymentBillDetail = new OrderPaymentBillDetail();
                orderPaymentBillDetail.setId(id);
                orderPaymentBillDetail.setAuditStatus(BillEnum.B_8.getCode());
                orderPaymentBillDetail.setUpdatedTime(LocalDateTime.now());
                orderPaymentBillDetail.setUpdatedUser(UserOperator.getToken());
                list.add(orderPaymentBillDetail);
            }
        }
        return updateBatchById(list);
    }

    @Override
    public IPage<FinanceAccountVO> findFinanceAccountByPage(QueryFinanceAccountForm form) {
        //定义分页参数
        Page<FinanceAccountVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<FinanceAccountVO> pageInfo = baseMapper.findFinanceAccountByPage(page, form);
        return pageInfo;
    }

    @Override
    public IPage<PaymentNotPaidBillVO> findFBillAuditByPage(QueryEditBillForm form) {
        //定义分页参数
        Page<PaymentNotPaidBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("opc.id"));
        IPage<PaymentNotPaidBillVO> pageInfo = baseMapper.findFBillAuditByPage(page, form);
        return pageInfo;
    }

    @Override
    public List<HeXiaoListVO> heXiaoList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<CancelAfterVerification> heXiaoList = verificationService.list(queryWrapper);
        return ConvertUtil.convertList(heXiaoList,HeXiaoListVO.class);
    }

    @Override
    public Boolean heXiaoConfirm(List<HeXiaoConfirmForm> forms) {
        List<HeXiaoConfirmForm> addList = new ArrayList<>();
        for (HeXiaoConfirmForm form : forms) {
            if(form.getId() == null){
                addList.add(form);
            }
        }
        //计算本币金额,提供给前台累加
        List<CancelAfterVerification> list = ConvertUtil.convertList(addList,CancelAfterVerification.class);
        return verificationService.saveBatch(list);
    }

    @Override
    public List<FCostVO> findFCostList(String billNo) {
        return baseMapper.findFCostList(billNo);
    }

    @Override
    public Boolean auditInvoice(BillAuditForm form) {
        OrderPaymentBillDetail billDetail = new OrderPaymentBillDetail();
        billDetail.setId(form.getBillDetailId());
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
        Integer num = baseMapper.updateById(billDetail);
        if(num > 0){
            //保存审核信息
            AuditInfoForm auditInfoForm = new AuditInfoForm();
            auditInfoForm.setExtId(form.getBillDetailId());
            auditInfoForm.setAuditTypeDesc("付款审核");
            auditInfoForm.setAuditStatus(applyStatus);
            auditInfoForm.setAuditComment(form.getAuditComment());
            auditInfoForm.setExtDesc("order_payment_bill_detail表");
            auditInfoForm.setAuditUser(UserOperator.getToken());
            omsClient.saveAuditInfo(auditInfoForm);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<MakeInvoiceVO> findInvoiceList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<MakeInvoice> makeInvoices = makeInvoiceService.list(queryWrapper);
        return ConvertUtil.convertList(makeInvoices,MakeInvoiceVO.class);
    }

    @Override
    public Boolean makeInvoice(MakeInvoiceForm form) {
        MakeInvoice makeInvoic = ConvertUtil.convert(form,MakeInvoice.class);
        return makeInvoiceService.save(makeInvoic);
    }

    @Override
    public Boolean makeInvoiceDel(Long invoiceId) {
        MakeInvoice makeInvoice = new MakeInvoice();
        makeInvoice.setId(invoiceId);
        makeInvoice.setStatus("0");
        makeInvoice.setCreatedTime(LocalDateTime.now());
        makeInvoice.setCreatedUser(UserOperator.getToken());
        return makeInvoiceService.updateById(makeInvoice);
    }


}
