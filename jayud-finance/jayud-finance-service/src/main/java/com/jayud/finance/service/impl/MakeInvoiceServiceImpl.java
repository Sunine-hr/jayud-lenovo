package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.finance.bo.MakeInvoiceForm;
import com.jayud.finance.bo.MakeInvoiceListForm;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.mapper.MakeInvoiceMapper;
import com.jayud.finance.po.MakeInvoice;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.IMakeInvoiceService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.vo.MakeInvoiceVO;
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
public class MakeInvoiceServiceImpl extends ServiceImpl<MakeInvoiceMapper, MakeInvoice> implements IMakeInvoiceService {

    @Autowired
    IOrderReceivableBillDetailService receivableBillDetailService;

    @Autowired
    IOrderPaymentBillDetailService paymentBillDetailService;

    @Override
    public List<MakeInvoiceVO> findInvoiceList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.BILL_NO,billNo);
        List<MakeInvoice> makeInvoices = list(queryWrapper);
        return ConvertUtil.convertList(makeInvoices,MakeInvoiceVO.class);
    }

    @Override
    public CommonResult makeInvoice(MakeInvoiceListForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.BILL_NO,form.getBillNo());
        BigDecimal totalHeXiaoMoney = new BigDecimal("0");//申请金额，即可核销总金额
        //只有开票/付款申请审核-B_6才允许开票/付款核销
        String oprType = CommonConstant.DOUBLE_QUOTE;//1-开票 2-收票
        if(CommonConstant.RECEIVABLE.equals(form.getCmd())){
            oprType = CommonConstant.VALUE_1;
            List<OrderReceivableBillDetail> receivableBillDetails = receivableBillDetailService.list(queryWrapper);
            OrderReceivableBillDetail receivableBillDetail = receivableBillDetails.get(0);
            totalHeXiaoMoney = receivableBillDetail.getInvoiceAmount();
            if(!BillEnum.B_6.getCode().equals(receivableBillDetail.getAuditStatus())){
                return CommonResult.error(ResultEnum.MAKE_INVOICE_CONDITION_1);
            }
        }else if(CommonConstant.PAYMENT.equals(form.getCmd())){
            oprType = CommonConstant.VALUE_2;
            List<OrderPaymentBillDetail> paymentBillDetails = paymentBillDetailService.list(queryWrapper);
            OrderPaymentBillDetail paymentBillDetail = paymentBillDetails.get(0);
            totalHeXiaoMoney = paymentBillDetail.getPaymentAmount();
            if(!BillEnum.B_6.getCode().equals(paymentBillDetail.getAuditStatus())){
                return CommonResult.error(ResultEnum.MAKE_INVOICE_CONDITION_2);
            }
        }
        //如果申请金额<已保存核销金额+本次新增的,则不允许在添加
        List<MakeInvoice> existInvoices = baseMapper.selectList(queryWrapper);
        BigDecimal existHeXiaoMoney = new BigDecimal("0");//已经核销金额,并且是未作废的
        for (MakeInvoice existInvoice : existInvoices) {
            existHeXiaoMoney = existHeXiaoMoney.add(existInvoice.getMoney());
        }
        BigDecimal willHeXiaoMoney = new BigDecimal("0");//本次新增核销的金额
        for (MakeInvoiceForm makeInvoiceForm : form.getMakeInvoices()) {
            willHeXiaoMoney = willHeXiaoMoney.add(makeInvoiceForm.getMoney());
        }
        if(totalHeXiaoMoney.compareTo(existHeXiaoMoney.add(willHeXiaoMoney)) == -1){
            return CommonResult.error(ResultEnum.MAKE_INVOICE_CONDITION_3);
        }

        List<MakeInvoiceForm> makeInvoiceForms = form.getMakeInvoices();
        List<MakeInvoice> makeInvoices = new ArrayList<>();
        for(MakeInvoiceForm makeInvoiceForm : makeInvoiceForms){
            if(makeInvoiceForm.getId() != null){//只取本次添加得
                continue;
            }
            MakeInvoice makeInvoice = ConvertUtil.convert(makeInvoiceForm,MakeInvoice.class);
            makeInvoice.setMakeTime(DateUtils.str2LocalDateTime(makeInvoiceForm.getMakeTimeStr(),DateUtils.DATE_TIME_PATTERN));
            makeInvoice.setFileUrl(StringUtils.getFileStr(makeInvoiceForm.getFileViewList()));
            makeInvoice.setFileName(StringUtils.getFileNameStr(makeInvoiceForm.getFileViewList()));
            makeInvoice.setStatus(CommonConstant.VALUE_1);//有效
            makeInvoice.setCreatedUser(form.getLoginUserName());
            makeInvoice.setOprType(oprType);
            makeInvoices.add(makeInvoice);
        }
        Boolean result = saveBatch(makeInvoices);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @Override
    public Boolean makeInvoiceDel(Long invoiceId) {
        MakeInvoice makeInvoice = new MakeInvoice();
        makeInvoice.setId(invoiceId);
        makeInvoice.setStatus(CommonConstant.VALUE_0);
        makeInvoice.setCreatedTime(LocalDateTime.now());
        makeInvoice.setCreatedUser(UserOperator.getToken());
        return updateById(makeInvoice);
    }
}
