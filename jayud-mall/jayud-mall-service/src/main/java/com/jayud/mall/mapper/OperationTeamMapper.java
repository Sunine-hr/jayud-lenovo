package com.jayud.mall.mapper;

import com.jayud.mall.model.po.OperationTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 运营(服务)小组 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Mapper
@Component
public interface OperationTeamMapper extends BaseMapper<OperationTeam> {

}
