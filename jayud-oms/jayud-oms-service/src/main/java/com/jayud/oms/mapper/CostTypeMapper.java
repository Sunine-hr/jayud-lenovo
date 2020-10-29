package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryCostTypeForm;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.vo.CostTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 费用类型，暂时废弃 Mapper 接口
 * </p>
 *
 * @author 李达荣
 * @since 2020-10-27
 */
@Mapper
public interface CostTypeMapper extends BaseMapper<CostType> {
    IPage<CostTypeVO> findCostTypeByPage(Page page, @Param("form")QueryCostTypeForm form);
}
