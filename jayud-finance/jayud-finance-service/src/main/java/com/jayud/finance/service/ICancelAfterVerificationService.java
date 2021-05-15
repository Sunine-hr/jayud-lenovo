package com.jayud.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.common.CommonResult;
import com.jayud.finance.bo.HeXiaoConfirmListForm;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.vo.HeXiaoListVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface ICancelAfterVerificationService extends IService<CancelAfterVerification> {

    /**
     * 核销列表
     *
     * @param billNo
     * @return
     */
    List<HeXiaoListVO> heXiaoList(String billNo);

    /**
     * 核销确认
     *
     * @param form
     * @return
     */
    CommonResult heXiaoConfirm(HeXiaoConfirmListForm form);

    /**
     * 根据账单查询核销列表
     */
    List<CancelAfterVerification> getByBillNos(List<String> billNos);
}
