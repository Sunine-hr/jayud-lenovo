package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCostGenreForm;
import com.jayud.oms.model.po.CostGenre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.CostGenreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 基础数据费用类型 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-30
 */
@Mapper
public interface CostGenreMapper extends BaseMapper<CostGenre> {

    IPage<CostGenreVO> findCostGenreByPage(Page page, @Param("form") QueryCostGenreForm form);
}
