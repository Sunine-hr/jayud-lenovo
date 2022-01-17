package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRoleActionData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.SystemRoleActionDataVO;
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
public interface SystemRoleActionDataMapper extends BaseMapper<SystemRoleActionData> {

    IPage<SystemRoleActionDataVO> findByPage(@Param("page") Page<SystemRoleActionDataVO> page, @Param("form") QueryForm form);

    Integer getRoleData(@Param("actionCode")String actionCode,@Param("longs") List<Long> longs);
}
