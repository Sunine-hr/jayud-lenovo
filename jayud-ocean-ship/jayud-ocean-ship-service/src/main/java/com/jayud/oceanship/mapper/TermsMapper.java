package com.jayud.oceanship.mapper;

import com.jayud.oceanship.po.Terms;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@Mapper
public interface TermsMapper extends BaseMapper<Terms> {

    List<Terms> list();
}
