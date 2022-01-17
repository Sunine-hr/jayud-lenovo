package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemRoleAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.SystemRoleActionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色权限设置（按钮） Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Mapper
public interface SystemRoleActionMapper extends BaseMapper<SystemRoleAction> {

    IPage<SystemRoleActionVO> findByPage(@Param("page") Page<SystemRoleActionVO> page, @Param("form") QueryForm form);

    /**
     * 根据角色id批量删除数据
     * @param roleIds
     * @return
     */
    boolean removeSystemRoleActionByRoleId(List<Long> roleIds);
}
