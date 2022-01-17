package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryAccountForm;
import com.jayud.scm.model.bo.QuerySystemUserForm;
import com.jayud.scm.model.po.SystemUser;
import com.jayud.scm.model.vo.DepartmentChargeVO;
import com.jayud.scm.model.vo.SystemUserVO;
import com.jayud.scm.model.vo.UpdateSystemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    /**
     * 获取用户列表分页
     *
     * @param page
     * @param form
     * @return
     */
    IPage<SystemUserVO> getPageList(Page page, @Param("form") QuerySystemUserForm form);

    /**
     * 获取部门负责人并统计该部门员工人数
     *
     * @param departmentId
     * @return
     */
    List<DepartmentChargeVO> findOrgStructureCharge(Long departmentId);

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UpdateSystemUserVO getSystemUser(Long id);

    /**
     * 分页查询各个模块中账户管理
     */
    IPage<SystemUserVO> findEachModuleAccountByPage(Page page, @Param("form") QueryAccountForm form);
}
