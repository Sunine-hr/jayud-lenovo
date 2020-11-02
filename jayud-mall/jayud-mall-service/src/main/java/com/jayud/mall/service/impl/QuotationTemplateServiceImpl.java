package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.mall.mapper.QuotationTemplateMapper;
import com.jayud.mall.model.bo.QueryQuotationTemplateFrom;
import com.jayud.mall.model.po.QuotationTemplate;
import com.jayud.mall.model.vo.QuotationTemplateVO;
import com.jayud.mall.service.IQuotationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报价模板 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-02
 */
@Service
public class QuotationTemplateServiceImpl extends ServiceImpl<QuotationTemplateMapper, QuotationTemplate> implements IQuotationTemplateService {

    @Autowired
    QuotationTemplateMapper quotationTemplateMapper;

    @Override
    public IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateFrom form) {
        //处理时间区间
        if(form.getSailTime() != null){
            form.setSailTimeStart(form.getSailTime().toLocalDate().toString() + " 00:00:00");
            form.setSailTimeEnd(form.getSailTime().toLocalDate().toString() + " 23:23:59");
        }
        if(form.getCutOffTime() != null){
            form.setCutOffTimeStart(form.getSailTime().toLocalDate().toString() + " 00:00:00");
            form.setCutOffTimeEnd(form.getSailTime().toLocalDate().toString() + " 23:23:59");
        }
        //定义分页参数
        Page<QuotationTemplateVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        //page.addOrder(OrderItem.desc("oc.id"));
        IPage<QuotationTemplateVO> pageInfo = baseMapper.findQuotationTemplateByPage(page, form);
        return pageInfo;
    }

    @Override
    public void disabledQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("0");
        this.saveOrUpdate(quotationTemplate);
    }

    @Override
    public void enableQuotationTemplate(Long id) {
        QuotationTemplate quotationTemplate = quotationTemplateMapper.selectById(id);
        quotationTemplate.setStatus("1");
        this.saveOrUpdate(quotationTemplate);
    }
}
