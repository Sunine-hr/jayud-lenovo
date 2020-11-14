package com.jayud.mall.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.mall.model.bo.QueryQuotationTemplateForm;
import com.jayud.mall.model.po.QuotationTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jayud.mall.model.vo.QuotationTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 报价模板 Mapper 接口
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
@Mapper
@Component
public interface QuotationTemplateMapper extends BaseMapper<QuotationTemplate> {

    /**
     * 分页查看
     * @param page
     * @param form
     * @return
     */
    IPage<QuotationTemplateVO> findQuotationTemplateByPage(Page<QuotationTemplateVO> page, QueryQuotationTemplateForm form);

    /**
     * 查看报价模板
     * @param id
     * @return
     */
    QuotationTemplateVO lookQuotationTemplate(Long id);
}
