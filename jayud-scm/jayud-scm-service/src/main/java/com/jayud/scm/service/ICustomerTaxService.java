package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerTaxForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerTax;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerTaxVO;

/**
 * <p>
 * 客户开票资料 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerTaxService extends IService<CustomerTax> {

    CustomerTax getCustomerTaxByCustomerId(Integer id);

    IPage<CustomerTaxVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerTax(AddCustomerTaxForm form);

    boolean modifyDefaultValues(AddCustomerTaxForm form);

    CustomerTaxVO getCustomerTaxById(Integer id);
}
