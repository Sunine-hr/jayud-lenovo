package com.jayud.scm.mapper;

import com.jayud.scm.model.po.CustomerEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface CustomerEntryMapper extends BaseMapper<CustomerEntry> {

}
