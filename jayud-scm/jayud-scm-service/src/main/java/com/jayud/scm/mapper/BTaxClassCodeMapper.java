package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.BTaxClassCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BTaxClassCodeVO;
import com.jayud.scm.model.vo.HsCodeFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.ManagedBean;

/**
 * <p>
 * 税务分类表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-29
 */
@Mapper
public interface BTaxClassCodeMapper extends BaseMapper<BTaxClassCode> {

    IPage<BTaxClassCodeVO> findByPage(@Param("page") Page<BTaxClassCodeVO> page, @Param("form") QueryForm form);
}
