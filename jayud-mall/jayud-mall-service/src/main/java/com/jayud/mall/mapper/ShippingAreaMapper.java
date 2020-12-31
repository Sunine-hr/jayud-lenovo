package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryShippingAreaForm;
import com.jayud.mall.model.po.ShippingArea;
import com.jayud.mall.model.vo.ShippingAreaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 集货仓表 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Mapper
@Component
public interface ShippingAreaMapper extends BaseMapper<ShippingArea> {

    /**
     * 分页查询集货仓表
     * @param page
     * @param form
     * @return
     */
    IPage<ShippingAreaVO> findShippingAreaByPage(Page<ShippingAreaVO> page, @Param("form") QueryShippingAreaForm form);
}
