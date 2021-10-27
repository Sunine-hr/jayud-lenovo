package com.jayud.oms.service;

import com.jayud.oms.model.po.ContractQuotationDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 合同报价详情 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
public interface IContractQuotationDetailsService extends IService<ContractQuotationDetails> {

    List<ContractQuotationDetails> getByCondition(ContractQuotationDetails contractQuotationDetails);

}
