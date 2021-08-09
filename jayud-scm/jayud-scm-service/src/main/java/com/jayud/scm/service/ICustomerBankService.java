package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerBankForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerBankVO;

/**
 * <p>
 * 客户银行表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerBankService extends IService<CustomerBank> {

    IPage<CustomerBankVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerBank(AddCustomerBankForm form);

    boolean modifyDefaultValues(AddCustomerBankForm form);

    CustomerBankVO getCustomerBankById(Integer id);
}
