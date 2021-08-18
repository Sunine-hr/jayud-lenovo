package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubShippingEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HubShippingEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 出库单明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface HubShippingEntryMapper extends BaseMapper<HubShippingEntry> {

    IPage<HubShippingEntryVO> findByPage(@Param("page") Page<HubShippingEntryVO> page, @Param("form") QueryCommonForm form);
}
