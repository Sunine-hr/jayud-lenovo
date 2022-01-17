package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CheckOrderEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 提验货单明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Mapper
public interface CheckOrderEntryMapper extends BaseMapper<CheckOrderEntry> {

    IPage<CheckOrderEntryVO> findByPage(@Param("page") Page<CheckOrderEntryVO> page, @Param("form") QueryCommonForm form);
}
