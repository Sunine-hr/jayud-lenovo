package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oauth.model.bo.AddCompanyForm;
import com.jayud.oauth.model.bo.AddDepartmentForm;
import com.jayud.oauth.model.po.Department;
import com.jayud.oauth.model.vo.DepartmentVO;
import com.jayud.oauth.model.vo.QueryOrgStructureVO;

import java.util.List;

/**
 * <p>
 * 后台部门表 服务类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
public interface ISystemDepartmentService extends IService<Department> {


    /**
     * 获取部门
     * @return
     */
    List<DepartmentVO> findDepartment(Long id);

    /**
     * 部门
     * @param id
     * @return
     */
    public List<DepartmentVO> getDepartment(Long id);


    /**
     * 根据父级部门查子部门
     * @return
     */
    List<QueryOrgStructureVO> findDepartmentByfId(Long fId);

    /**
     * 新增部门/编辑
     * @param form
     */
    void saveOrUpdateDepartment(AddDepartmentForm form);

    /**
     * 获取部门id
     * @param deptName
     */
    Department getByDeptName(String deptName);

    /**
     * 新增主体简称/编辑
     * @param form
     */
    void saveOrUpdateCompany(AddCompanyForm form);
}
