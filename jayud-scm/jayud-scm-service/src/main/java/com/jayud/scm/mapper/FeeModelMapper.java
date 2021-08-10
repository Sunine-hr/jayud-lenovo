package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.FeeModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.FeeModelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 结算方案 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Mapper
public interface FeeModelMapper extends BaseMapper<FeeModel> {

    IPage<FeeModelVO> findByPage(@Param("page") Page<FeeModelVO> page,@Param("form") QueryCommonForm form);
}
