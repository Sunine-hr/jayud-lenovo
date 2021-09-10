package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.AcctPayFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.AcctPayFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 付款跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Mapper
public interface AcctPayFollowMapper extends BaseMapper<AcctPayFollow> {

    IPage<AcctPayFollowVO> findListByAcctPayId(@Param("form") QueryCommonForm form, @Param("page") Page<AcctPayFollowVO> page);
}
