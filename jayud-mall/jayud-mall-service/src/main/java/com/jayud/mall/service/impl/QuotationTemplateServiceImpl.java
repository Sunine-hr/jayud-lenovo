package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.QuotationTemplateMapper;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.QuotationTemplate;
import com.jayud.mall.model.po.TemplateCopeReceivable;
import com.jayud.mall.model.po.TemplateCopeWith;
import com.jayud.mall.model.po.TemplateFile;
import com.jayud.mall.model.vo.QuotationTemplateVO;
import com.jayud.mall.service.IQuotationTemplateService;
import com.jayud.mall.service.ITemplateCopeReceivableService;
import com.jayud.mall.service.ITemplateCopeWithService;
import com.jayud.mall.service.ITemplateFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    ITemplateCopeReceivableService templateCopeReceivableService;

    @Autowired
    ITemplateCopeWithService templateCopeWithService;

    @Autowired
    ITemplateFileService templateFileService;

    @Override
    public IPage<QuotationTemplateVO> findQuotationTemplateByPage(QueryQuotationTemplateForm form) {
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
        IPage<QuotationTemplateVO> pageInfo = quotationTemplateMapper.findQuotationTemplateByPage(page, form);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuotationTemplateFull(QuotationTemplateForm form) {
        QuotationTemplate quotationTemplate = ConvertUtil.convert(form, QuotationTemplate.class);
        this.saveOrUpdate(quotationTemplate);
        //报价模板Id
        Long id = quotationTemplate.getId();
        System.out.println(id);

        /*应收费用明细List*/
        List<TemplateCopeReceivableForm> templateCopeReceivableFormList = form.getTemplateCopeReceivableFormList();
        if(templateCopeReceivableFormList.size() > 0){
            List<TemplateCopeReceivable> list = new ArrayList<>();
            templateCopeReceivableFormList.forEach(templateCopeReceivableForm -> {
                TemplateCopeReceivable templateCopeReceivable = ConvertUtil.convert(templateCopeReceivableForm, TemplateCopeReceivable.class);
                templateCopeReceivable.setQie(id.intValue());
                list.add(templateCopeReceivable);
            });
            templateCopeReceivableService.saveOrUpdateBatch(list);
        }

        /*应付费用明细list*/
        List<TemplateCopeWithForm> templateCopeWithFormList = form.getTemplateCopeWithFormList();
        if(templateCopeWithFormList.size() > 0){
            List<TemplateCopeWith> list = new ArrayList<>();
            templateCopeWithFormList.forEach(templateCopeWithForm -> {
                TemplateCopeWith templateCopeWith = ConvertUtil.convert(templateCopeWithForm, TemplateCopeWith.class);
                templateCopeWith.setQie(id.intValue());
                list.add(templateCopeWith);
            });
            templateCopeWithService.saveOrUpdateBatch(list);
        }

        /*文件信息明细list*/
        List<TemplateFileForm> templateFileFormList = form.getTemplateFileFormList();
        if(templateFileFormList.size() > 0){
            List<TemplateFile> list = new ArrayList<>();
            templateFileFormList.forEach(templateFileForm -> {
                TemplateFile templateFile = ConvertUtil.convert(templateFileForm, TemplateFile.class);
                templateFile.setQie(id.intValue());
                list.add(templateFile);
            });
            templateFileService.saveOrUpdateBatch(list);
        }

    }

}
