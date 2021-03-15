package com.jayud.mall.service;

import com.jayud.mall.model.bo.TradingRecordForm;
import com.jayud.mall.model.po.TradingRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 交易记录表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
public interface ITradingRecordService extends IService<TradingRecord> {

    /**
     * 客户充值
     * @param form
     */
    void customerPay(TradingRecordForm form);
}
