package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.SystemUser;
import com.jayud.mall.model.vo.SystemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
@Mapper
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    SystemUserVO findSystemUserByLoginname(@Param(value = "loginname") String loginname);

}
