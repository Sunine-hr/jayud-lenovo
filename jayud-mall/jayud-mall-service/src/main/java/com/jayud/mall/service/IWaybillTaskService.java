package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.po.WaybillTask;
import com.jayud.mall.model.vo.WaybillTaskVO;

import java.util.List;

/**
 * <p>
 * 运单(订单)任务列表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
public interface IWaybillTaskService extends IService<WaybillTask> {

    /**
     * 根据报价id，查询运单任务信息list
     * @param OfferInfoId   报价id
     * @return
     */
    @Deprecated
    List<WaybillTaskVO> findWaybillTaskByOfferInfoId(Integer OfferInfoId);

    /**
     * 根据订单id，查询运单任务信息list
     * @param orderInfoId
     * @return
     */
    List<WaybillTaskVO> findWaybillTaskByOrderInfoId(Long orderInfoId);
}
