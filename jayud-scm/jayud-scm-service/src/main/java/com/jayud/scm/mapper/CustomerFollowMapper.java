package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CustomerFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.BCountryVO;
import com.jayud.scm.model.vo.CustomerFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-04
 */
@Mapper
public interface CustomerFollowMapper extends BaseMapper<CustomerFollow> {

    IPage<CustomerFollowVO> findListByCustomerId(@Param("form") QueryCommonForm form, @Param("page")Page<CustomerFollowVO> page);
}
