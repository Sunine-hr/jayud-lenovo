package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerRelationerForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerRelationer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerRelationerVO;

/**
 * <p>
 * 客户联系人表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerRelationerService extends IService<CustomerRelationer> {

    IPage<CustomerRelationerVO> findByPage(QueryCommonForm form);

    boolean saveOrUpdateCustomerRelationer(AddCustomerRelationerForm form);

    boolean modifyDefaultValues(AddCustomerRelationerForm form);

    CustomerRelationerVO getCustomerRelationerById(Integer id);
}
