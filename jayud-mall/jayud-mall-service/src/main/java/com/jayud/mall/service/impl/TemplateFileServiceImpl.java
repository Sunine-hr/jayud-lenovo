package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.mall.model.bo.TemplateFileForm;
import com.jayud.mall.model.po.TemplateFile;
import com.jayud.mall.mapper.TemplateFileMapper;
import com.jayud.mall.service.ITemplateFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 模板对应模块信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class TemplateFileServiceImpl extends ServiceImpl<TemplateFileMapper, TemplateFile> implements ITemplateFileService {

    @Autowired
    TemplateFileMapper templateFileMapper;

    @Override
    public List<TemplateFile> findTemplateFile(TemplateFileForm form) {
        Long id = form.getId();
        Integer qie = form.getQie();
        String fileName = form.getFileName();
        Integer options = form.getOptions();
        String remarks = form.getRemarks();
        QueryWrapper<TemplateFile> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        if(qie != null){
            queryWrapper.eq("qie", qie);
        }
        if(fileName != null && fileName != ""){
            queryWrapper.like("file_name", fileName);
        }
        if(options != null){
            queryWrapper.eq("`options`", options);
        }
        if(remarks != null){
            queryWrapper.like("remarks", remarks);
        }
        List<TemplateFile> list = templateFileMapper.selectList(queryWrapper);
        return list;
    }
}
