package com.jayud.mall.service;

import com.jayud.mall.model.bo.TemplateCopeReceivableForm;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 报价对应应收费用明细 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
public interface ITemplateCopeReceivableService extends IService<TemplateCopeReceivable> {

    /**
     * 查询报价对应应收费用明细list
     * @param form
     * @return
     */
    List<TemplateCopeReceivable> findTemplateCopeReceivable(TemplateCopeReceivableForm form);
}
