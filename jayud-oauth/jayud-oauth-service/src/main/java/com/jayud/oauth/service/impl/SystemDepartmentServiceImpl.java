package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oauth.model.bo.AddCompanyForm;
import com.jayud.oauth.model.bo.AddDepartmentForm;
import com.jayud.oauth.model.po.Department;
import com.jayud.oauth.model.vo.DepartmentVO;
import com.jayud.oauth.model.vo.QueryOrgStructureVO;
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
    public void saveOrUpdateDepartment(AddDepartmentForm form) {
        Department department = new Department();
        String loginUser = userService.getLoginUser().getName();
        if(form.getId() != null) {
            department.setUpdatedUser(loginUser);
            department.setId(form.getId());
        }else {
            department.setCreatedUser(loginUser);
            if(form.getFId() != null && !"".equals(form.getFId())){
                department.setFId(form.getFId());
            }
            department.setName(form.getName());
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",form.getFId());
        Department department1 = baseMapper.selectOne(queryWrapper);
        department.setName(form.getName());
        department.setLegalId(department1.getLegalId());
        saveOrUpdate(department);
    }

    @Override
    public Department getByDeptName(String deptName) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name",deptName);
        Department department = baseMapper.selectOne(queryWrapper);
        return department;
    }

    @Override
    public void saveOrUpdateCompany(AddCompanyForm form) {
        Department department = new Department();
        String loginUser = userService.getLoginUser().getName();
        if(form.getId() != null) {
            department.setUpdatedUser(loginUser);
            department.setId(form.getId());
        }else {
            department.setCreatedUser(loginUser);
            if(form.getLegalId() != null && !"".equals(form.getLegalId())){
                department.setLegalId(form.getLegalId());
            }
            department.setName(form.getName());
        }
        department.setFId(0l);
        department.setName(form.getName());
        saveOrUpdate(department);
    }


}
