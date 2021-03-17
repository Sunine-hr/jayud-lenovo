package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountReceivable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.AccountReceivableVO;

/**
 * <p>
 * 应收对账单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
public interface IAccountReceivableService extends IService<AccountReceivable> {

    /**
     * 应收对账单分页查询
     * @param form
     * @return
     */
    IPage<AccountReceivableVO> findAccountReceivableByPage(QueryAccountReceivableForm form);

    /**
     * 应收账单-查看明细
     * @param id
     * @return
     */
    CommonResult<AccountReceivableVO> lookDetail(Long id);

    /**
     * 生成应收月结账单(创建应收对账单)
     * @param form
     * @return
     */
    CommonResult createRecMonthlyStatement(MonthlyStatementForm form);
}
