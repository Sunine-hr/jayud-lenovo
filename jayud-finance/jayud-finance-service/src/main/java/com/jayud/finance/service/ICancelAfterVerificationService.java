package com.jayud.finance.service;

import com.jayud.finance.bo.HeXiaoConfirmForm;
import com.jayud.finance.po.CancelAfterVerification;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.vo.HeXiaoListVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
public interface ICancelAfterVerificationService extends IService<CancelAfterVerification> {

    /**
     * 核销列表
     * @param billNo
     * @return
     */
    List<HeXiaoListVO> heXiaoList(String billNo);

    /**
     * 核销确认
     * @param form
     * @return
     */
    Boolean heXiaoConfirm(List<HeXiaoConfirmForm> form);
}
