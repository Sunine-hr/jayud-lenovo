package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryWorkBillForm;
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
}
