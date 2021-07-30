package com.jayud.scm.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.scm.model.po.Company;
import com.jayud.scm.model.vo.CompanyVO;


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
