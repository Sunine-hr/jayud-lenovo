package com.jayud.auth.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.auth.model.bo.QueryForm;
import com.jayud.auth.model.po.HsElements;
import com.jayud.auth.model.vo.CodeElementsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 申报要素表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Mapper
public interface HsElementsMapper extends BaseMapper<HsElements> {

    IPage<CodeElementsVO> findElements(@Param("form") QueryForm form, @Param("page") Page<CodeElementsVO> page);
}
