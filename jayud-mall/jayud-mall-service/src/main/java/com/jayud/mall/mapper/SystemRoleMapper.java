package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.po.SystemRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

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

}
