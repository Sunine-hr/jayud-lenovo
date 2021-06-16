package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.BatchCounterOrderInfoForm;
import com.jayud.mall.model.po.CounterOrderInfo;
import com.jayud.mall.model.vo.CounterOrderInfoVO;

import java.util.List;

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
    void batchIntoCounterOrderInfo(BatchCounterOrderInfoForm form);

    /**
     * 批量移除(柜子清单-绑定订单)
     * @param form
     */
    void batchRemoveCounterOrderInfo(BatchCounterOrderInfoForm form);

    /**
     * 查询柜子清单下的运单信息list
     * @param bId
     * @return
     */
    List<CounterOrderInfoVO> findCounterOrderInfoByBid(Long bId);
}
