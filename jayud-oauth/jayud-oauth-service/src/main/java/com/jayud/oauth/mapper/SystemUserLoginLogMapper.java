package com.jayud.oauth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oauth.model.po.SystemUserLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 后台用户登录日志表 Mapper 接口
 * </p>
 *
 * @author bocong.zheng
 * @since 2020-07-20
 */
@Mapper
public interface SystemUserLoginLogMapper extends BaseMapper<SystemUserLoginLog> {

}
