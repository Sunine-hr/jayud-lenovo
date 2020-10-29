package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryUserForm;
import com.jayud.mall.model.bo.ResetUserPwdForm;
import com.jayud.mall.model.bo.SaveUserForm;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
@Mapper
@Component
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    /**
     * 查询登录用户名是否存在（能否查询到用户）
     * @param loginname
     * @return
     */
    SystemUserVO findSystemUserByLoginname(@Param(value = "loginname") String loginname);

    /**
     * 查询所有用户list
     * @return
     */
    List<SystemUserVO> getUserList();

    /**
     * 新增用户
     * @param user
     */
    void insertUser(SaveUserForm user);

    /**
     * 修改用户
     * @param user
     */
    void updateUser(SaveUserForm user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 获取根据Id，获取用户
     * @param id
     * @return
     */
    SystemUserVO getUser(Long id);

    /**
     * 禁用用户
     * @param id
     */
    void disableUser(Long id);

    /**
     * 启用用户
     * @param id
     */
    void enableUser(Long id);

    /**
     * 重置用户密码
     * @param id
     */
    void resetPassword(ResetUserPwdForm resetUserPwdForm);

    /**
     * 查询用户分页
     * @param page
     * @param form
     * @return
     */
    IPage<SystemUserVO> findUserByPage(Page<SystemUserVO> page, QueryUserForm form);



}
