package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.CheckOrderFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.CheckOrderFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 提验货单跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Mapper
public interface CheckOrderFollowMapper extends BaseMapper<CheckOrderFollow> {

    IPage<CheckOrderFollowVO> findListByCheckOrderId(@Param("form") QueryCommonForm form, @Param("page")Page<CheckOrderFollowVO> page);
}
