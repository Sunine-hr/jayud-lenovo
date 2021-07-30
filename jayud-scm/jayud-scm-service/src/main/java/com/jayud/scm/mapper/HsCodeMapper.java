package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.HsCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HsCodeFormVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 海关编码表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-07-26
 */
@Mapper
public interface HsCodeMapper extends BaseMapper<HsCode> {

    /**
     * 获取海关编码分页列表
     * @param page
     * @param form
     * @return
     */
    IPage<HsCodeFormVO> findByPage(@Param("page") Page<HsCodeFormVO> page, @Param("form") QueryForm form);

    List<String> findHsCode(@Param("codeNo")String codeNo);
}
