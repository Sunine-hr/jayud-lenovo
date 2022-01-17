package com.jayud.scm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.po.Department;
import com.jayud.scm.model.vo.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemDepartmentMapper extends BaseMapper<Department> {

    List<DepartmentVO> getDepartment();

}
