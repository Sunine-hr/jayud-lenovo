package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.model.bo.QuotedFileForm;
import com.jayud.mall.model.po.QuotedFile;
import com.jayud.mall.mapper.QuotedFileMapper;
import com.jayud.mall.model.vo.QuotedFileReturnVO;
import com.jayud.mall.service.IQuotedFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            queryWrapper.eq("`options`", options);
        }
        if(isCheck != null){
            queryWrapper.eq("is_check", isCheck);
        }
        if(describe != null && describe != ""){
            queryWrapper.like("`describe`", describe);
        }
        if(status != null && status != ""){
            queryWrapper.eq("`status`", status);
        }
        List<QuotedFile> list = quotedFileMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<QuotedFileReturnVO> findQuotedFileBy(QuotedFileForm form) {
        List<QuotedFileReturnVO> quotedFileReturnVOS = quotedFileMapper.findQuotedFileBy(form);
        return quotedFileReturnVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuotedFile(QuotedFileForm form) {
        QuotedFile quotedFile = ConvertUtil.convert(form, QuotedFile.class);
        Long id = quotedFile.getId();
        if(ObjectUtil.isNotEmpty(id)){
            QuotedFile quotedFileById = quotedFileMapper.findQuotedFileById(id);
            if(ObjectUtil.isEmpty(quotedFileById)){
                Asserts.fail(ResultEnum.UNKNOWN_ERROR, "没有找到对应的文件");
            }
        }
        this.saveOrUpdate(quotedFile);
    }

    @Override
    public QuotedFile findQuotedFileById(Long id) {
        return quotedFileMapper.findQuotedFileById(id);
    }
}
