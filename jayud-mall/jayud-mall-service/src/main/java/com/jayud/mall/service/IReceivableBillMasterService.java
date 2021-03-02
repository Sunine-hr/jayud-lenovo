package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.ReceivableBillForm;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;

/**
 * <p>
 * 应收账单主单 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
public interface IReceivableBillMasterService extends IService<ReceivableBillMaster> {

    /**
     * 生成应收账单
     * @param form
     * @return
     */
    CommonResult<ReceivableBillMasterVO> createReceivableBill(ReceivableBillForm form);
}
