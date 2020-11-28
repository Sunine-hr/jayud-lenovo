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
import com.jayud.finance.vo.HeXiaoListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class CancelAfterVerificationServiceImpl extends ServiceImpl<CancelAfterVerificationMapper, CancelAfterVerification> implements ICancelAfterVerificationService {

    @Autowired
    ICurrencyRateService currencyRateService;

    @Override
    public List<HeXiaoListVO> heXiaoList(String billNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<CancelAfterVerification> heXiaoList = list(queryWrapper);
        return ConvertUtil.convertList(heXiaoList,HeXiaoListVO.class);
    }

    @Override
    public CommonResult heXiaoConfirm(HeXiaoConfirmListForm forms) {
        //已保存折合金额合计 + 本次新增折合金额 > 应收金额 ,则不允许操作
        //TODO
        List<HeXiaoConfirmForm> addList = new ArrayList<>();
        for (HeXiaoConfirmForm form : forms.getHeXiaoConfirmForms()) {
            if(form.getId() == null){
                addList.add(form);//只保存本次添加的数据
            }
        }
        List<CancelAfterVerification> list = new ArrayList<>();
        for (HeXiaoConfirmForm heXiaoConfirmForm : addList) {
            CancelAfterVerification cancelAfterVerification = ConvertUtil.convert(heXiaoConfirmForm,CancelAfterVerification.class);
            cancelAfterVerification.setRealReceiveTime(DateUtils.str2LocalDateTime(heXiaoConfirmForm.getRealReceiveTimeStr(),DateUtils.DATE_TIME_PATTERN));
            cancelAfterVerification.setCreatedUser(forms.getLoginUserName());
            //计算本币金额
            String oCode = cancelAfterVerification.getCurrencyCode();//原始币种,即实收金额的币种
            BigDecimal exchangeRate = currencyRateService.getExchangeRate(oCode,"CNY");
            BigDecimal localMoney = cancelAfterVerification.getRealReceiveMoney().multiply(exchangeRate);
            cancelAfterVerification.setLocalAmount(localMoney);
            list.add(cancelAfterVerification);
        }
        Boolean result = saveBatch(list);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }
}
