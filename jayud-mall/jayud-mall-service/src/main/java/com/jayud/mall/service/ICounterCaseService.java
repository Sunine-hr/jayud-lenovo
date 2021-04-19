package com.jayud.mall.service;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.SaveCounterCaseForm;
import com.jayud.mall.model.po.CounterCase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 货柜对应运单箱号信息 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-02-22
 */
public interface ICounterCaseService extends IService<CounterCase> {

    /**
     * 保存货柜关联运单箱号信息
     * @param form
     * @return
     */
    CommonResult saveCounterCase(SaveCounterCaseForm form);
}
