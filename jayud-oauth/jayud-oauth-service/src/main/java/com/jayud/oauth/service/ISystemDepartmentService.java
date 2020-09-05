package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.bo.AddDepartmentForm;
import com.jayud.model.po.Department;
import com.jayud.model.vo.DepartmentVO;
import com.jayud.model.vo.QueryOrgStructureVO;

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
    List<DepartmentVO> findDepartment();


    /**
     * 根据父级部门查子部门
     * @return
     */
    List<QueryOrgStructureVO> findDepartmentByfId(Long fId);

    /**
     * 新增部门/编辑
     * @param departmentId
     */
    void saveOrUpdateDepartment(Long departmentId, AddDepartmentForm form);

}
