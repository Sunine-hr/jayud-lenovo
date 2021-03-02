package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.PayBillForm;
import com.jayud.mall.model.bo.PayBillMasterForm;
import com.jayud.mall.model.po.PayBillMaster;
import com.jayud.mall.model.vo.PayBillMasterVO;

/**
 * <p>
 * 应付账单主单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
public interface IPayBillMasterService extends IService<PayBillMaster> {

    /**
     * 生成应付账单
     * @param form
     * @return
     */
    CommonResult<PayBillMasterVO> createPayBill(PayBillForm form);

    /**
     * 生成应付账单-确认
     * @param form
     * @return
     */
    CommonResult<PayBillMasterVO> affirmPayBill(PayBillMasterForm form);
}
