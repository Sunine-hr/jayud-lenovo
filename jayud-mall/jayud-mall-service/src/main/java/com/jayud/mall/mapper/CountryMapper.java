package com.jayud.mall.mapper;

import com.jayud.mall.model.po.Country;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 国家基础信息表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-19
 */
@Mapper
@Component
public interface CountryMapper extends BaseMapper<Country> {

}