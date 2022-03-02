package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmFile;
import com.jayud.crm.mapper.CrmFileMapper;
import com.jayud.crm.service.ICrmFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_文件(crm_file) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmFileServiceImpl extends ServiceImpl<CrmFileMapper, CrmFile> implements ICrmFileService {


    @Autowired
    private CrmFileMapper crmFileMapper;

    @Override
    public IPage<CrmFile> selectPage(CrmFile crmFile,
                                        Integer currentPage,
                                        Integer pageSize,
                                        HttpServletRequest req){

        Page<CrmFile> page=new Page<CrmFile>(currentPage,pageSize);
        IPage<CrmFile> pageList= crmFileMapper.pageList(page, crmFile);
        return pageList;
    }

    @Override
    public List<CrmFile> selectList(CrmFile crmFile){
        return crmFileMapper.list(crmFile);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id){
        crmFileMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id){
        crmFileMapper.logicDel(id,CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmFileForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmFileForExcel(paramMap);
    }

}
