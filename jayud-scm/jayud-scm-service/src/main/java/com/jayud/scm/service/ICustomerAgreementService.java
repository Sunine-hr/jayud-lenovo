package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerAgreementForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerAgreement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerAgreementVO;

/**
 * <p>
 * 客户协议表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerAgreementService extends IService<CustomerAgreement> {

    IPage<CustomerAgreementVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerAgreement(AddCustomerAgreementForm form);

    CustomerAgreementVO getCustomerAgreementById(Integer id);
}
