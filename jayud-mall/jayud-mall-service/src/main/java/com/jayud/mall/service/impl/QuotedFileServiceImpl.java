package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.mall.model.bo.QuotedFileForm;
import com.jayud.mall.model.po.QuotedFile;
import com.jayud.mall.mapper.QuotedFileMapper;
import com.jayud.mall.service.IQuotedFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报价对应的文件表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Service
public class QuotedFileServiceImpl extends ServiceImpl<QuotedFileMapper, QuotedFile> implements IQuotedFileService {

    @Autowired
    QuotedFileMapper quotedFileMapper;

    @Override
    public List<QuotedFile> findQuotedFile(QuotedFileForm form) {
        Long id = form.getId();
        String groupCode = form.getGroupCode();
        String idCode = form.getIdCode();
        String fileName = form.getFileName();
        Integer options = form.getOptions();
        Integer isCheck = form.getIsCheck();
        String describe = form.getDescribe();
        String status = form.getStatus();
        QueryWrapper<QuotedFile> queryWrapper = new QueryWrapper<>();
        if(id != null){
            queryWrapper.eq("id", id);
        }
        if(groupCode != null && groupCode != ""){
            queryWrapper.like("group_code", groupCode);
        }
        if(idCode != null && idCode != ""){
            queryWrapper.like("id_code", idCode);
        }
        if(fileName != null && fileName != ""){
            queryWrapper.like("file_name", fileName);
        }
        if(options != null){
            queryWrapper.eq("options", options);
        }
        if(isCheck != null){
            queryWrapper.eq("is_check", isCheck);
        }
        if(describe != null && describe != ""){
            queryWrapper.like("describe", describe);
        }
        if(status != null && status != ""){
            queryWrapper.eq("status", queryWrapper);
        }
        List<QuotedFile> list = quotedFileMapper.selectList(queryWrapper);
        return list;
    }
}
