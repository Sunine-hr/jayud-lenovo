package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkOrderForm;
import com.jayud.mall.model.bo.WorkOrderAddForm;
import com.jayud.mall.model.bo.WorkOrderEvaluateForm;
import com.jayud.mall.model.bo.WorkOrderReplyForm;
import com.jayud.mall.model.po.WorkOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.WorkOrderVO;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-26
 */
public interface IWorkOrderService extends IService<WorkOrder> {

    /**
     * 订单工单分页查询
     * @param form
     * @return
     */
    IPage<WorkOrderVO> findWorkOrderByPage(QueryWorkOrderForm form);

    /**
     * 根据id查看工单
     * @param id
     * @return
     */
    CommonResult<WorkOrderVO> findWorkOrderById(Long id);

    /**
     * 根据id，删除工单
     * @param id
     * @return
     */
    CommonResult delWorkOrderById(Long id);

    /**
     * 根据id，评价工单
     * @param form
     * @return
     */
    CommonResult evaluateWorkOrderById(WorkOrderEvaluateForm form);

    /**
     * 客户添加工单
     * @param form
     * @return
     */
    CommonResult<WorkOrderVO> addWorkOrder(WorkOrderAddForm form);

    /**
     * 结单
     * @param id
     * @return
     */
    CommonResult statementWorkOrder(Long id);

    /**
     * 回复
     * @param form
     * @return
     */
    CommonResult replyWorkOrder(WorkOrderReplyForm form);
}
