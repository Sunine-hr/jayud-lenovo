package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryPromoteCompanyForm;
import com.jayud.mall.model.bo.SavePromoteCompanyForm;
import com.jayud.mall.model.po.PromoteCompany;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.PromoteCompanyVO;

import java.util.List;

/**
 * <p>
 * 推广公司表 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-20
 */
public interface IPromoteCompanyService extends IService<PromoteCompany> {

    IPage<PromoteCompanyVO> findPromoteCompanyByPage(QueryPromoteCompanyForm form);

    void savePromoteCompany(SavePromoteCompanyForm form);

    List<PromoteCompanyVO> findPromoteCompanyByParentId(Integer parentId);

    PromoteCompanyVO findPromoteCompanyByCompanyId(Integer companyId);
}
