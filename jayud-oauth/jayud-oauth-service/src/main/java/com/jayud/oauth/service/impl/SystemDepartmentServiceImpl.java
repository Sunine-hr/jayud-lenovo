package com.jayud.oauth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.model.po.Department;
import com.jayud.model.vo.DepartmentVO;
import com.jayud.oauth.mapper.SystemDepartmentMapper;
import com.jayud.oauth.service.ISystemDepartmentService;
import com.jayud.oauth.service.ISystemMenuService;
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
    ISystemMenuService menuService;

    @Override
    public List<DepartmentVO> findDepartment() {
        return ConvertUtil.convertList(baseMapper.selectList(null),DepartmentVO.class);
    }



}
