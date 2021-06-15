package com.jayud.mall.service;

import com.jayud.mall.model.bo.BtchCounterOrderInfoForm;
import com.jayud.mall.model.po.CounterOrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 柜子订单信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-15
 */
public interface ICounterOrderInfoService extends IService<CounterOrderInfo> {

    /**
     * 批量移入(柜子清单-绑定订单)
     * @param form
     */
    void batchIntoCounterOrderInfo(BtchCounterOrderInfoForm form);

    /**
     * 批量移除(柜子清单-绑定订单)
     * @param form
     */
    void batchRemoveCounterOrderInfo(BtchCounterOrderInfoForm form);
}
