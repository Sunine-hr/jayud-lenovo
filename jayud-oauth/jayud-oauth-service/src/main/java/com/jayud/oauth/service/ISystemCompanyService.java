package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.Company;
import com.jayud.model.vo.CompanyVO;

import java.util.List;

/**
 * 公司表
 */
public interface ISystemCompanyService extends IService<Company> {

    /**
     * 获取公司
     * @return
     */
    List<CompanyVO> findCompany();




}
