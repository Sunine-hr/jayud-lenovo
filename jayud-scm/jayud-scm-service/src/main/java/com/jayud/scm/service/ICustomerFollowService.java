package com.jayud.scm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.scm.model.bo.AddCustomerFollowForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.vo.CustomerFollowVO;

/**
 * <p>
 * 客户跟踪记录表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
public interface ICustomerFollowService extends IService<CustomerFollow> {

    IPage<CustomerFollowVO> findListByCustomerId(QueryCommonForm form);
}
