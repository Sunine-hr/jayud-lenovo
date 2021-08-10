package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.Fee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.FeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 结算方案条款 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Mapper
public interface FeeMapper extends BaseMapper<Fee> {

    IPage<FeeVO> findByPage(@Param("page") Page<FeeVO> page,@Param("form")QueryCommonForm form);
}
