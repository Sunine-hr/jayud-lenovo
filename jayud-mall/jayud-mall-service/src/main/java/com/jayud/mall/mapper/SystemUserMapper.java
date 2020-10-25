package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    List<SystemUserVO> getUserList();

    void insertUser(SaveUserForm user);

    void updateUser(SaveUserForm user);

    void deleteUser(int userId);

    SystemUserVO getUser(int id);


}
