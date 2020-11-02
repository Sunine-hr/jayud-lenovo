package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.mall.model.bo.QueryQuotationTemplateFrom;
import com.jayud.mall.model.po.QuotationTemplate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.mall.model.vo.QuotationTemplateVO;

/**
 * <p>
 * 报价模板 服务类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
public interface IQuotationTemplateService extends IService<QuotationTemplate> {

    /**
     * 分页查询
     * @param form
     * @return
     */
    IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateFrom form);
}
