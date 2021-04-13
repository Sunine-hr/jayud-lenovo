package com.jayud.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.oms.model.bo.OrderCostTemplateDTO;
import com.jayud.oms.model.bo.QueryCostTemplateForm;
import com.jayud.oms.model.po.OrderCostTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 费用模板 Mapper 接口
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
@Mapper
public interface OrderCostTemplateMapper extends BaseMapper<OrderCostTemplate> {

    IPage<OrderCostTemplateDTO> findByPage(Page<OrderCostTemplateDTO> page, @Param("form") QueryCostTemplateForm form);

    OrderCostTemplateDTO getCostTemplateInfo(Long id);
}
