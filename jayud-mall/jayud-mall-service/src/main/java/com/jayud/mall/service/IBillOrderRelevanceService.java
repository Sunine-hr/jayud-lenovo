package com.jayud.mall.service;

import com.jayud.mall.model.po.BillOrderRelevance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提单关联订单(任务通知表) 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-17
 */
public interface IBillOrderRelevanceService extends IService<BillOrderRelevance> {

    /**
     * 根据billId，提单关联订单(任务通知表)
     * @param billId
     * @return
     */
    List<BillOrderRelevance> findBillOrderRelevanceByBillId(Long billId);

    /**
     * 修改，提单关联订单(任务通知表)，主要修改 是否通知运单物流轨迹(1通知 2不通知)
     * @param form
     */
    void updateBillOrderRelevance(List<BillOrderRelevance> form);
}
