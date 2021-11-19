package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.enums.ContractQuotationProStatusEnum;
import com.jayud.oms.model.po.ContractQuotation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.vo.ContractQuotationVO;

import java.util.List;

/**
 * <p>
 * 合同报价 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
public interface IContractQuotationService extends IService<ContractQuotation> {

    void saveOrUpdate(AddContractQuotationForm form);

    boolean exitNumber(String number);

    boolean exitName(Long id, String name);

    IPage<ContractQuotationVO> findByPage(QueryContractQuotationForm form);

    ContractQuotationVO getEditInfoById(Long id);

    String autoGenerateNum();

    void auditOpt(Long id, String reasonsFailure, ContractQuotationProStatusEnum statusEnum);

    List<ContractQuotation> getByCondition(ContractQuotation contractQuotation);
}
