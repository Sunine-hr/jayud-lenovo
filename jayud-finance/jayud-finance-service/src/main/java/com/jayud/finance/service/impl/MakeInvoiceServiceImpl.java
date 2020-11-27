package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
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
        queryWrapper.eq("bill_no",billNo);
        List<MakeInvoice> makeInvoices = list(queryWrapper);
        return ConvertUtil.convertList(makeInvoices,MakeInvoiceVO.class);
    }

    @Override
    public CommonResult makeInvoice(MakeInvoiceListForm form) {
        //只有开票/付款申请审核-B_6才允许开票/付款核销
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        String oprType = "";//1-开票 2-收票
        if("receivable".equals(form.getCmd())){
            oprType = "1";
            List<OrderReceivableBillDetail> receivableBillDetails = receivableBillDetailService.list(queryWrapper);
            OrderReceivableBillDetail receivableBillDetail = receivableBillDetails.get(0);
            if(!BillEnum.B_6.getCode().equals(receivableBillDetail.getAuditStatus())){
                return CommonResult.error(10001,"开票申请审核通过才可操作");
            }
        }else if("payment".equals(form.getCmd())){
            oprType = "2";
            List<OrderPaymentBillDetail> paymentBillDetails = paymentBillDetailService.list(queryWrapper);
            OrderPaymentBillDetail paymentBillDetail = paymentBillDetails.get(0);
            if(!BillEnum.B_6.getCode().equals(paymentBillDetail.getAuditStatus())){
                return CommonResult.error(10001,"付款申请审核通过才可操作");
            }
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
            makeInvoice.setStatus("1");//有效
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
        makeInvoice.setStatus("0");
        makeInvoice.setCreatedTime(LocalDateTime.now());
        makeInvoice.setCreatedUser(UserOperator.getToken());
        return updateById(makeInvoice);
    }
}
