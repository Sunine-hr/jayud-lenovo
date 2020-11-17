package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.ApplyInvoiceForm;
import com.jayud.finance.bo.AuditInfoForm;
import com.jayud.finance.bo.ListForm;
import com.jayud.finance.bo.QueryPaymentBillDetailForm;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.mapper.OrderReceivableBillDetailMapper;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class OrderReceivableBillDetailServiceImpl extends ServiceImpl<OrderReceivableBillDetailMapper, OrderReceivableBillDetail> implements IOrderReceivableBillDetailService {

    @Autowired
    OmsClient omsClient;

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
}
