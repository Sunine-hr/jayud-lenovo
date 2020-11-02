package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryQuotationTemplateFrom;
import com.jayud.mall.model.po.QuotationTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.QuotationTemplateVO;

/**
 * <p>
 * 报价模板 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
public interface QuotationTemplateMapper extends BaseMapper<QuotationTemplate> {

    IPage<QuotationTemplateVO> findQuotationTemplateByPage(Page<QuotationTemplateVO> page, QueryQuotationTemplateFrom form);
}
