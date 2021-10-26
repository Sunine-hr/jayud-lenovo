package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.QueryElectronicFenceForm;
import com.jayud.oms.model.po.ElectronicFence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.oms.model.vo.ElectronicFenceVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 电子围栏 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-10-25
 */
public interface ElectronicFenceMapper extends BaseMapper<ElectronicFence> {

    IPage<ElectronicFenceVO> findByPage(@Param("page") Page<ElectronicFence> page,
                                        @Param("form") QueryElectronicFenceForm form);
}
