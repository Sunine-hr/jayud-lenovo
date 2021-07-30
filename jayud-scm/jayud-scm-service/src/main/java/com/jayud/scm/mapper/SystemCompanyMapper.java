package com.jayud.scm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.po.Company;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-03
 */
@Mapper
public interface SystemCompanyMapper extends BaseMapper<Company> {


}
