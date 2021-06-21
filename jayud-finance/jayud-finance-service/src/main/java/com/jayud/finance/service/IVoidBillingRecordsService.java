package com.jayud.finance.service;

import com.jayud.finance.bo.VoidBillForm;
import com.jayud.finance.po.VoidBillingRecords;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 作废账单表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2021-06-21
 */
public interface IVoidBillingRecordsService extends IService<VoidBillingRecords> {

    /**
     * 作废账单
     * @param form
     */
    void voidBill(VoidBillForm form);

    /**
     * 作废账单
     * @param billNo
     * @param type   0-应收,1-应付
     */
    void voidBill(String billNo, Integer type, Integer costStatus);

    /**
     * 清理账单数据
     * @param billNo
     * @param billId
     * @param type  0-应收,1-应付
     * @param costStatus
     * @param costIds
     */
    void cleaningUpBillingData(String billNo, Long billId, Integer type, Integer costStatus, List<Long> costIds);
}
