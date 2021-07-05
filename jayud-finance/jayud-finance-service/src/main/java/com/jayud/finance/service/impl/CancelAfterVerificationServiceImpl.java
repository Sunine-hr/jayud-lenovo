package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.bo.HeXiaoConfirmForm;
import com.jayud.finance.bo.HeXiaoConfirmListForm;
import com.jayud.finance.mapper.CancelAfterVerificationMapper;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.service.ICancelAfterVerificationService;
import com.jayud.finance.service.ICurrencyRateService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.vo.CostAmountVO;
import com.jayud.finance.vo.HeXiaoListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Service
public class CancelAfterVerificationServiceImpl extends ServiceImpl<CancelAfterVerificationMapper, CancelAfterVerification> implements ICancelAfterVerificationService {

    @Autowired
    ICurrencyRateService currencyRateService;

    @Autowired
    IOrderPaymentBillDetailService paymentBillDetailService;

    @Autowired
    IOrderReceivableBillDetailService receivableBillDetailService;

    @Override
    public List<HeXiaoListVO> heXiaoList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", billNo);
        List<CancelAfterVerification> heXiaoList = list(queryWrapper);
        return ConvertUtil.convertList(heXiaoList, HeXiaoListVO.class);
    }

    @Override
    public CommonResult heXiaoConfirm(HeXiaoConfirmListForm forms) {
        //本次添加的金额 > 待收/未收金额 ,则不允许操作
        CostAmountVO costFAmountVO = paymentBillDetailService.getFCostAmountView(forms.getBillNo());//应付核销
        CostAmountVO costSAmountVO = receivableBillDetailService.getSCostAmountView(forms.getBillNo());//应收核销
        BigDecimal wsAmount = new BigDecimal("0");//未收金额
        BigDecimal dfAmount = new BigDecimal("0");//待付金额
        BigDecimal nowAddAmount = new BigDecimal("0");//本次添加的金额
        List<HeXiaoConfirmForm> addList = new ArrayList<>();
        for (HeXiaoConfirmForm form : forms.getHeXiaoConfirmForms()) {
            if (form.getId() == null) {
                addList.add(form);//只保存本次添加的数据
                nowAddAmount = nowAddAmount.add(form.getDiscountMoney()).add(form.getShortAmount());
            }
        }
        if (costSAmountVO != null) {//说明本次是应收核销
            wsAmount = costSAmountVO.getWsAmount();
            if (nowAddAmount.compareTo(wsAmount) == 1) {
                return CommonResult.error(10001, "本次添加的金额超过未收金额");
            }
        }
        if (costFAmountVO != null) {//说明本次是应付核销
            dfAmount = costFAmountVO.getDfAmount();
            if (nowAddAmount.compareTo(dfAmount) == 1) {
                return CommonResult.error(10001, "本次添加的金额超过待付金额");
            }
        }
        List<CancelAfterVerification> list = new ArrayList<>();
        for (HeXiaoConfirmForm heXiaoConfirmForm : addList) {
            CancelAfterVerification cancelAfterVerification = ConvertUtil.convert(heXiaoConfirmForm, CancelAfterVerification.class);
            cancelAfterVerification.setRealReceiveTime(DateUtils.str2LocalDateTime(heXiaoConfirmForm.getRealReceiveTimeStr(), DateUtils.DATE_TIME_PATTERN));
            cancelAfterVerification.setCreatedUser(forms.getLoginUserName());
            //计算本币金额
            String oCode = cancelAfterVerification.getCurrencyCode();//原始币种,即实收金额的币种
            BigDecimal exchangeRate = currencyRateService.getExchangeRate(oCode, "CNY");
            if (oCode.equals("CNY") && exchangeRate == null) {
                exchangeRate = new BigDecimal(1);
            }
            if (exchangeRate == null) {
                return CommonResult.error(10001, "请先配置原始货币:" + oCode + "兑换货币：CNY的汇率");
            }
            BigDecimal localMoney = cancelAfterVerification.getRealReceiveMoney().multiply(exchangeRate);
            cancelAfterVerification.setLocalMoney(localMoney);
            list.add(cancelAfterVerification);
        }
        Boolean result = saveBatch(list);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    /**
     * 根据账单查询核销列表
     */
    @Override
    public List<CancelAfterVerification> getByBillNos(List<String> billNos) {
        QueryWrapper<CancelAfterVerification> condition = new QueryWrapper<>();
        condition.lambda().in(CancelAfterVerification::getBillNo, billNos);
        return this.baseMapper.selectList(condition);
    }
}
