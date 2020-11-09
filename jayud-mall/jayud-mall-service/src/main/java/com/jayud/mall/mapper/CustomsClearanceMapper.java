package com.jayud.mall.mapper;

import com.jayud.mall.model.po.CustomsClearance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 清关资料表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-09
 */
@Mapper
@Component
public interface CustomsClearanceMapper extends BaseMapper<CustomsClearance> {

}
