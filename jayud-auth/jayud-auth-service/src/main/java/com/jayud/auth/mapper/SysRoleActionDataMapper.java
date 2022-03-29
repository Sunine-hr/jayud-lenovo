package com.jayud.auth.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.SysRoleActionData;
import com.jayud.auth.model.vo.SysRoleActionDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色数据权限 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Mapper
public interface SysRoleActionDataMapper extends BaseMapper<SysRoleActionData> {

    IPage<SysRoleActionDataVO> findByPage(@Param("page") Page<SysRoleActionDataVO> page, @Param("form") QueryForm form);

    Integer getRoleData(@Param("actionCode")String actionCode,@Param("longs") List<Long> longs);
}
