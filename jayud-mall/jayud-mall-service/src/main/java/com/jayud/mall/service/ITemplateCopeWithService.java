package com.jayud.mall.service;

import com.jayud.mall.model.bo.TemplateCopeWithForm;
import com.jayud.mall.model.po.TemplateCopeWith;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报价对应应付费用明细 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ITemplateCopeWithService extends IService<TemplateCopeWith> {

    /**
     * 查询报价对应应付费用明细list
     * @param form
     * @return
     */
    List<TemplateCopeWith> findTemplateCopeWith(TemplateCopeWithForm form);
}
