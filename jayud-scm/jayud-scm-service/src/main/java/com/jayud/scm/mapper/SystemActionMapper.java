package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.po.SystemAction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.SystemActionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 按钮权限设置表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-02
 */
@Mapper
public interface SystemActionMapper extends BaseMapper<SystemAction> {

    IPage<SystemActionVO> findByPage(@Param("page") Page<SystemActionVO> page, @Param("form") QueryForm form);
}
