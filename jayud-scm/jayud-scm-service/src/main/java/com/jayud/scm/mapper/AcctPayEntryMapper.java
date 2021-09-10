package com.jayud.scm.mapper;

import com.jayud.scm.model.po.AcctPayEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 应付款表（付款单明细表） Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Mapper
public interface AcctPayEntryMapper extends BaseMapper<AcctPayEntry> {

}
