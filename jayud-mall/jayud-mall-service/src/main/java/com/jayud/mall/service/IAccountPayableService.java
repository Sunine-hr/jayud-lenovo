package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.po.AccountPayable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.AccountPayableVO;

/**
 * <p>
 * 应付对账单表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
public interface IAccountPayableService extends IService<AccountPayable> {

    /**
     * 应付对账单分页查询
     * @param form
     * @return
     */
    IPage<AccountPayableVO> findAccountPayableByPage(QueryAccountPayableForm form);

    /**
     * 应付对账单-查看明细
     * @param id
     * @return
     */
    CommonResult<AccountPayableVO> lookDetail(Long id);

    /**
     * 生成应付月结账单(创建应付对账单)
     * @param form
     * @return
     */
    CommonResult createPayMonthlyStatement(MonthlyStatementForm form);
}
