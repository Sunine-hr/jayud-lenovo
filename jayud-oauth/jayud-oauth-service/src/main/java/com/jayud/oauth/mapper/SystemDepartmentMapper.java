package com.jayud.oauth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oauth.model.po.Department;
import com.jayud.oauth.model.vo.DepartmentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemDepartmentMapper extends BaseMapper<Department> {


    List<DepartmentVO> getDepartment();

}
