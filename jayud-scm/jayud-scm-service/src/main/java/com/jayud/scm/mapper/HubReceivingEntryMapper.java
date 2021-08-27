package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HubReceivingEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HubReceivingEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 入库明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-17
 */
@Mapper
public interface HubReceivingEntryMapper extends BaseMapper<HubReceivingEntry> {

    IPage<HubReceivingEntryVO> findByPage(@Param("page") Page<HubReceivingEntryVO> page, @Param("form")QueryCommonForm form);

    IPage<HubReceivingEntryVO> findByPageByBillId(@Param("page")Page<HubReceivingEntryVO> page, @Param("form")QueryCommonForm form);
}
