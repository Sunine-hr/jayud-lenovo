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

    @Override
    public List<DepartmentVO> findDepartment() {
        return ConvertUtil.convertList(baseMapper.selectList(null),DepartmentVO.class);
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
        department.setId(departmentId);
        department.setName(form.getName());
        saveOrUpdate(department);
    }

}
