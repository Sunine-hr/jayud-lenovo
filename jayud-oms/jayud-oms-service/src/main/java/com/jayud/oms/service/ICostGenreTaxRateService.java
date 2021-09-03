package com.jayud.oms.service;

import com.jayud.oms.model.po.CostGenreTaxRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 费用类型税率表 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-09-02
 */
public interface ICostGenreTaxRateService extends IService<CostGenreTaxRate> {

    /**
     * 获取费用类型id集合获取费用税率
     * @param costGenreIds
     * @return
     */
    List<CostGenreTaxRate> getCostGenreTaxRateByGenreIds(List<Long> costGenreIds);
}
