package com.jayud.mall.service;

import com.jayud.mall.model.bo.ReceivableCostForm;
import com.jayud.mall.model.po.ReceivableCost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 应收/应付费用名称 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface IReceivableCostService extends IService<ReceivableCost> {

    /**
     * 查询应收/应付费用名称list
     * @param form
     * @return
     */
    List<ReceivableCost> findReceivableCost(ReceivableCostForm form);
}
