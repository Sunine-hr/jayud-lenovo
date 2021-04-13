package com.jayud.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.oms.model.bo.OrderCostTemplateDTO;
import com.jayud.oms.model.bo.QueryCostTemplateForm;
import com.jayud.oms.model.po.OrderCostTemplate;
import com.jayud.oms.model.vo.InitComboxStrVO;

import java.util.List;

/**
 * <p>
 * 费用模板 服务类
 * </p>
 *
 * @author LDR
 * @since 2021-04-08
 */
public interface IOrderCostTemplateService extends IService<OrderCostTemplate> {

    /**
     * 添加/编辑
     *
     * @param orderCostTemplateDTO
     */
    public void saveOrUpdateInfo(OrderCostTemplateDTO orderCostTemplateDTO);

    IPage<OrderCostTemplateDTO> findByPage(QueryCostTemplateForm orderCostTemplateDTO);

    boolean enableOrDisableCostInfo(Long id);

    /**
     * 校验唯一
     *
     * @param tmp
     * @return
     */
    boolean checkUnique(OrderCostTemplate tmp);

    /**
     * 获取费用模板详情
     *
     * @param id
     * @return
     */
    OrderCostTemplateDTO getCostTemplateInfo(Long id);

    List<InitComboxStrVO> initCostTemplate();
}
