package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerGuaranteeForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerGuarantee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerGuaranteeVO;

/**
 * <p>
 * 担保合同 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
public interface ICustomerGuaranteeService extends IService<CustomerGuarantee> {

    IPage<CustomerGuaranteeVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerGuarantee(AddCustomerGuaranteeForm form);

    CustomerGuaranteeVO getCustomerGuaranteeById(Integer id);
}
