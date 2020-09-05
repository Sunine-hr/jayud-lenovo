package com.jayud.oauth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.model.po.Department;
import com.jayud.model.vo.DepartmentVO;

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



}
