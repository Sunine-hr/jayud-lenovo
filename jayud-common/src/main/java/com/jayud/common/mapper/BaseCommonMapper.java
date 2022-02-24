package com.jayud.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.common.dto.LogDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ciro
 * @date 2022/2/24 14:24
 * @description:    common接口实现类
 */
@Mapper
public interface BaseCommonMapper extends BaseMapper {

    /**
     * 保存日志
     *
     * @param dto
     */
    void saveLog(@Param("dto") LogDTO dto);
}
