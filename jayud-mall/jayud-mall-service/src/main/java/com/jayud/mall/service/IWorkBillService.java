package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkBillForm;
import com.jayud.mall.model.bo.WorkBillAddForm;
import com.jayud.mall.model.bo.WorkBillEvaluateForm;
import com.jayud.mall.model.bo.WorkBillReplyForm;
import com.jayud.mall.model.po.WorkBill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.WorkBillVO;

/**
 * <p>
 * 提单工单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-30
 */
public interface IWorkBillService extends IService<WorkBill> {

    /**
     * 订单工单分页查询
     * @param form
     * @return
     */
    IPage<WorkBillVO> findWorkBillByPage(QueryWorkBillForm form);

    /**
     * 结单
     * @param id
     * @return
     */
    CommonResult statementWorkBill(Long id);

    /**
     * 回复
     * @param form
     * @return
     */
    CommonResult replyWorkBill(WorkBillReplyForm form);

    /**
     * 根据id，查看工单
     * @param id
     * @return
     */
    CommonResult<WorkBillVO> findWorkBillById(Long id);

    /**
     * 根据id，删除工单
     * @param id
     * @return
     */
    CommonResult delWorkBillById(Long id);

    /**
     * 评价工单
     * @param form
     * @return
     */
    CommonResult evaluateWorkBillById(WorkBillEvaluateForm form);

    /**
     * 新增工单
     * @param form
     * @return
     */
    CommonResult<WorkBillVO> addWorkBill(WorkBillAddForm form);

    /**
     * 关闭工单
     * @param id
     * @return
     */
    CommonResult closeWorkBill(Long id);
}
