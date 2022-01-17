package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HgBillFollowVO;
import com.jayud.scm.model.vo.HgBillVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 入库单跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-25
 */
@Mapper
public interface HgBillFollowMapper extends BaseMapper<HgBillFollow> {

    IPage<HgBillFollowVO> findByPage(@Param("page") Page<HgBillFollowVO> page, @Param("form") QueryCommonForm form);
}
