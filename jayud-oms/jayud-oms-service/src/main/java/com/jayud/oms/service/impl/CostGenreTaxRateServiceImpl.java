package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.CostGenreTaxRate;
import com.jayud.oms.mapper.CostGenreTaxRateMapper;
import com.jayud.oms.service.ICostGenreTaxRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 费用类型税率表 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-09-02
 */
@Service
public class CostGenreTaxRateServiceImpl extends ServiceImpl<CostGenreTaxRateMapper, CostGenreTaxRate> implements ICostGenreTaxRateService {

    /**
     * @param costGenreIds
     * @return
     */
    @Override
    public List<CostGenreTaxRate> getCostGenreTaxRateByGenreIds(List<Long> costGenreIds) {
        QueryWrapper<CostGenreTaxRate> condition = new QueryWrapper<>();
        condition.lambda().in(CostGenreTaxRate::getCostGenreId, costGenreIds);
        return this.baseMapper.selectList(condition);
    }
}
