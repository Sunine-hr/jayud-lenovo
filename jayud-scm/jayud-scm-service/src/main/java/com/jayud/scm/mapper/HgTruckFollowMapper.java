package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgTruckFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HgTruckFollowVO;
import com.jayud.scm.model.vo.HubShippingFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 港车跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Mapper
public interface HgTruckFollowMapper extends BaseMapper<HgTruckFollow> {

    IPage<HgTruckFollowVO> findByPage(@Param("page") Page<HubShippingFollowVO> page, @Param("form") QueryCommonForm form);
}
