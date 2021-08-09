package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.BDataDicEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BDataDicEntryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 数据字典明细表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface BDataDicEntryMapper extends BaseMapper<BDataDicEntry> {

    IPage<BDataDicEntryVO> findByPage(@Param("page") Page<BDataDicEntryVO> page, @Param("form")QueryCommonForm form);
}
