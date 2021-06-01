package com.jayud.finance.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.BeanUtils;
import com.jayud.finance.bo.ListForm;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.FinanceService;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private IOrderPaymentBillDetailService paymentBillDetailService;
    @Autowired
    private IOrderReceivableBillDetailService receivableBillDetailService;

    /**
     * 是否可以推送金蝶
     *
     * @param list
     * @param type
     */
    @Override
    public void isPushKingdee(List list, Integer type) {
        //校验是否可推送金蝶
        //1.必须财务已审核通过
        //TODO 2021-5-19改版 客户审核通过就可以推金蝶
        StringBuilder sb = new StringBuilder("账单编号:");

        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Boolean flag = false;
        for (Object obj : list) {
            JSONObject jsonObject = new JSONObject(obj);
            String billNo = jsonObject.getStr(BeanUtils.convertToFieldName(OrderReceivableBillDetail::getBillNo));
            String auditStatus = jsonObject.getStr(BeanUtils.convertToFieldName(OrderReceivableBillDetail::getAuditStatus));
            //通用检验
            if (StringUtil.isNullOrEmpty(auditStatus) || !this.checkPushKingdee(jsonObject, auditStatus)) {
                flag = true;
                sb.append(billNo + ";");
            }

        }
        sb.append("财务未审核通过,不能推送金蝶");
        if (flag) {
            throw new JayudBizException(10001, sb.toString());
        }
    }

    /**
     * 推金蝶
     *
     * @param jsonObject
     * @param auditStatus
     * @return
     */
    private boolean checkPushKingdee(JSONObject jsonObject, String auditStatus) {
        //TODO 财务审核对账单通过和开票申请(付款申请)通过可以推送
        if (BillEnum.B_4.getCode().equals(auditStatus)
                || BillEnum.B_5.getCode().equals(auditStatus)
                || BillEnum.B_5_1.getCode().equals(auditStatus)
                || BillEnum.B_6.getCode().equals(auditStatus)
                || BillEnum.B_6_1.getCode().equals(auditStatus)
                || BillEnum.B_9.getCode().equals(auditStatus)) {
            return true;
        }
        return false;
    }

    /**
     * 校验反审核
     *
     * @param auditStatus
     * @return
     */
    @Override
    public boolean checkAntiAudite(String auditStatus) {
        //TODO 财务审核对账单通过和开票申请(付款申请)通过可以推送
        if (BillEnum.B_4.getCode().equals(auditStatus)
                || BillEnum.B_5.getCode().equals(auditStatus)
                || BillEnum.B_5_1.getCode().equals(auditStatus)
                || BillEnum.B_6.getCode().equals(auditStatus)
                || BillEnum.B_6_1.getCode().equals(auditStatus)
                || BillEnum.B_9.getCode().equals(auditStatus)) {
            return true;
        }
        return false;
    }

}
