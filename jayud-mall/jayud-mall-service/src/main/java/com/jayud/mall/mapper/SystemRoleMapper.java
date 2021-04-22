package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryRoleForm;
import com.jayud.mall.model.po.SystemRole;
import com.jayud.mall.model.vo.SystemRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Mapper
@Component
public interface SystemRoleMapper extends BaseMapper<SystemRole> {

    /**
     * 保存角色(新增/修改)
     * @param from
     * @return
     */
    Long saveRole(@Param("from") SystemRole from);

    /**
     * 删除角色
     * @param id
     */
    void deleteRole(@Param("id") Long id);

    /**
     * 根据id，获取角色信息
     * @param id
     * @return
     */
    SystemRoleVO getRole(@Param("id") Long id);

    /**
     * 查询角色分页
     * @param page
     * @param form
     * @return
     */
    IPage<SystemRoleVO> findRoleByPage(Page<SystemRoleVO> page, @Param("form") QueryRoleForm form);

    /**
     * 根据用户Id，查询用户角色
     * @param userId
     * @return
     */
    List<SystemRole> selectRolesByUserId(@Param("userId") Long userId);
}
