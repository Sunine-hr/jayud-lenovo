package com.jayud.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ciro
 * @date 2022/2/15 11:20
 * @description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
