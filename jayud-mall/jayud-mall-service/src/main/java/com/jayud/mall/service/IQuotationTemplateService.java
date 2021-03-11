package com.jayud.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.QueryQuotationTemplateForm;
import com.jayud.mall.model.bo.QuotationTemplateForm;
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
    IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateForm form);

    /**
     * 禁用报价模板
     * @param id
     */
    void disabledQuotationTemplate(Long id);

    /**
     * 启用报价模板
     * @param id
     */
    void enableQuotationTemplate(Long id);

    /**
     * 保存报价模板
     * @param form
     */
    CommonResult saveQuotationTemplate(QuotationTemplateForm form);

    /**
     * 查看报价模板
     * @param id
     * @return
     */
    CommonResult<QuotationTemplateVO> lookQuotationTemplate(Long id);
}
