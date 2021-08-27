package com.jayud.scm.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.po.HgBillCloud;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.scm.model.vo.HgBillCloudVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报关单一对接跟踪记录表 Mapper 接口
 * </p>
 *
 * @author LLJ
 * @since 2021-08-26
 */
@Mapper
public interface HgBillCloudMapper extends BaseMapper<HgBillCloud> {

    IPage<HgBillCloudVO> findByPage(@Param("page") Page<HgBillCloudVO> page, @Param("form")QueryCommonForm form);
}
