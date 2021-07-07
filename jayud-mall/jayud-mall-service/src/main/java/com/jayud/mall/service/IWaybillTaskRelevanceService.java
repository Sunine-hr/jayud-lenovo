package com.jayud.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.bo.WaybillTaskRelevanceIdForm;
import com.jayud.mall.model.bo.WaybillTaskRelevanceQueryForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.WaybillTaskRelevance;
import com.jayud.mall.model.vo.WaybillTaskRelevanceVO;

import java.util.List;

/**
 * <p>
 * 运单(订单）任务关联 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-27
 */
public interface IWaybillTaskRelevanceService extends IService<WaybillTaskRelevance> {

    /**
     * <p>运单(订单）任务关联</p>
     * <p>根据订单，找到报价，再找报价模板，再找任务组，最后找到运单任务列表</p>
     * <p>保存对应的运单(订单）任务关联</p>
     */
    List<WaybillTaskRelevanceVO> saveWaybillTaskRelevance(OrderInfo orderInfo);

    /**
     * 查询，运单任务列表
     * @param form
     * @return
     */
    List<WaybillTaskRelevanceVO> findWaybillTaskRelevance(WaybillTaskRelevanceQueryForm form);

    /**
     * 根据id，查询任务
     * @param id
     * @return
     */
    WaybillTaskRelevanceVO findWaybillTaskRelevanceById(Long id);

    /**
     * 完成运单任务
     * @param form
     */
    void finishTask(WaybillTaskRelevanceIdForm form);

    /**
     * 延期运单任务
     * @param form
     */
    void postponeTask(WaybillTaskRelevanceIdForm form);
}
