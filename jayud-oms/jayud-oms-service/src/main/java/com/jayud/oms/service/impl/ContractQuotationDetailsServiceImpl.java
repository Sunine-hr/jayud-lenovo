package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.oms.model.po.ContractQuotationDetails;
import com.jayud.oms.mapper.ContractQuotationDetailsMapper;
import com.jayud.oms.service.IContractQuotationDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 合同报价详情 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Service
public class ContractQuotationDetailsServiceImpl extends ServiceImpl<ContractQuotationDetailsMapper, ContractQuotationDetails> implements IContractQuotationDetailsService {

    @Override
    public List<ContractQuotationDetails> getByCondition(ContractQuotationDetails contractQuotationDetails) {
        QueryWrapper<ContractQuotationDetails> condition = new QueryWrapper<>(contractQuotationDetails);
        return this.baseMapper.selectList(condition);
    }
}
