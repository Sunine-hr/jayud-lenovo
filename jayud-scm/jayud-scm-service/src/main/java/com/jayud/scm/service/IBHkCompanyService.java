package com.jayud.scm.service;

import com.jayud.scm.model.po.BHkCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 香港抬头表 服务类
 * </p>
 *
 * @author LLJ
 * @since 2021-08-18
 */
public interface IBHkCompanyService extends IService<BHkCompany> {

    /**
     * 查询香港抬头list
     * @return
     */
    List<BHkCompany> findBHkCompany();
}
