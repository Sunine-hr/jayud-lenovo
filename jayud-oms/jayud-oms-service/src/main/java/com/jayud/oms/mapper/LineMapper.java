package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryLineForm;
import com.jayud.oms.model.po.Line;
import com.jayud.oms.model.vo.LineDetailsVO;
import com.jayud.oms.model.vo.LineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  线路管理 Mapper 接口
 * </p>
 *
 * @author CYC
 * @since 2021-10-18
 */
@Mapper
public interface LineMapper extends BaseMapper<Line> {

    IPage<LineVO> findLineByPage(Page page, @Param("form") QueryLineForm form);

    LineDetailsVO getLineDetails(@Param("id") Long id);
}
