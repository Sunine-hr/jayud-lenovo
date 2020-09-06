package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.bo.AddDepartmentForm;
import com.jayud.model.po.Department;
import com.jayud.model.vo.DepartmentVO;
import com.jayud.model.vo.QueryOrgStructureVO;
import com.jayud.oauth.mapper.SystemDepartmentMapper;
import com.jayud.oauth.service.ISystemDepartmentService;
import com.jayud.oauth.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-21
 */
@Service
public class SystemDepartmentServiceImpl extends ServiceImpl<SystemDepartmentMapper, Department> implements ISystemDepartmentService {

    @Autowired
    private ISystemUserService userService;

    @Override
    public List<DepartmentVO> findDepartment(Long id) {
        QueryWrapper queryWrapper = null;
        if(id != null) {
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("id", id);
        }
        return ConvertUtil.convertList(baseMapper.selectList(queryWrapper),DepartmentVO.class);
    }

    @Override
    public List<QueryOrgStructureVO> findDepartmentByfId(Long fId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("f_id", fId);
        List<Department> departments = baseMapper.selectList(queryWrapper);
        return ConvertUtil.convertList(departments,QueryOrgStructureVO.class);
    }

    @Override
    public void saveOrUpdateDepartment(Long departmentId, AddDepartmentForm form) {
        Department department = new Department();
        String loginUser = userService.getLoginUser().getName();
        if(departmentId != null) {
            department.setUpdatedUser(loginUser);
            department.setId(departmentId);
        }else {
            department.setCreatedUser(loginUser);
            department.setFId(0L);
        }
        department.setName(form.getName());
        saveOrUpdate(department);
    }

}