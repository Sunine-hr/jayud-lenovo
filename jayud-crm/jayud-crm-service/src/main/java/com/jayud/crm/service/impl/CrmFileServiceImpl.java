package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.BaseResult;
import com.jayud.common.constant.SysTips;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.FileClient;
import com.jayud.crm.model.bo.CrmFileForm;
import com.jayud.crm.model.bo.QueryCrmFile;
import com.jayud.crm.model.constant.CodeNumber;
import com.jayud.crm.model.enums.FileModuleEnum;
import com.jayud.crm.model.po.CrmCustomerRelations;
import com.jayud.crm.model.po.CrmCustomerTax;
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
import java.util.*;

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
    @Autowired
    private FileClient fileClient;
    @Autowired
    private AuthClient authClient;

    @Override
    public IPage<CrmFile> selectPage(CrmFile crmFile,
                                     Integer currentPage,
                                     Integer pageSize,
                                     HttpServletRequest req) {

        Page<CrmFile> page = new Page<CrmFile>(currentPage, pageSize);
        IPage<CrmFile> pageList = crmFileMapper.pageList(page, crmFile);
        Object url = this.fileClient.getBaseUrl().getData();
//        Object url ="http://test.oms.jayud.com:9448";
        pageList.getRecords().stream().forEach(v -> {
            v.setUploadFileUrl(url + v.getUploadFileUrl());
        });
        return pageList;
    }

    @Override
    public List<CrmFile> selectList(CrmFile crmFile) {
        List<CrmFile> list = crmFileMapper.list(crmFile);
        Object url = this.fileClient.getBaseUrl().getData();
//        Object url ="http://test.oms.jayud.com:9448";
        list.stream().forEach(v->{
            v.setUploadFileUrl(url + v.getUploadFileUrl());

        });
        return list;
    }

    @Override
    public BaseResult saveOrUpdateCrmFile(QueryCrmFile queryCrmFile) {
        Boolean result = null;
        Object url = this.fileClient.getBaseUrl().getData();
//        CrmFile convert = ConvertUtil.convert(CrmFile, CrmFile.class);
        String nextCode = getNextCode(CodeNumber.CRM_FILE_CODE);

        if (queryCrmFile.getId() != null) {
            //这里面是修改
            //修改根据id先删除了当前条数据
            CrmFile crmFileOne = new CrmFile();
            crmFileOne.setId(queryCrmFile.getId());
            crmFileOne.setIsDeleted(true);
            this.crmFileMapper.insert(crmFileOne);

            List<CrmFileForm> crmFileForm = queryCrmFile.getCrmFileForm();

            for (int i = 0; i < crmFileForm.size(); i++) {
                CrmFile crmFile = new CrmFile();
                crmFile.setCode(FileModuleEnum.CRM_FILE.getCode());
                crmFile.setBusinessId(queryCrmFile.getBusinessId());
                crmFile.setCrmFileNumber(queryCrmFile.getCrmFileNumber());
                crmFile.setFileName(crmFileForm.get(i).getFileName());
                crmFile.setFileType(queryCrmFile.getFileType());
                crmFile.setUploadFileUrl(crmFileForm.get(i).getUploadFileUrl());
                crmFile.setCreateBy(CurrentUserUtil.getUsername());
                crmFile.setCreateTime(new Date());
                crmFile.setRemark(queryCrmFile.getRemark());

                result = this.saveOrUpdate(crmFile);
            }

        } else {
            //新增的
            List<CrmFileForm> crmFileForm = queryCrmFile.getCrmFileForm();

            for (int i = 0; i < crmFileForm.size(); i++) {
                CrmFile crmFile = new CrmFile();
                crmFile.setCode(FileModuleEnum.CRM_FILE.getCode());
                crmFile.setBusinessId(queryCrmFile.getBusinessId());
                crmFile.setCrmFileNumber(nextCode);
                crmFile.setFileName(crmFileForm.get(i).getFileName());
                crmFile.setFileType(queryCrmFile.getFileType());
                crmFile.setUploadFileUrl(crmFileForm.get(i).getUploadFileUrl());
                crmFile.setCreateBy(CurrentUserUtil.getUsername());
                crmFile.setCreateTime(new Date());
                crmFile.setRemark(queryCrmFile.getRemark());

                result = this.saveOrUpdate(crmFile);
            }

        }
        if (result) {
            log.warn("新增或修改成功");
            return BaseResult.ok(SysTips.ADD_SUCCESS);
        }
        return BaseResult.error(SysTips.EDIT_FAIL);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmFileMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(List<Long> ids) {
        List<CrmFile> crmFileList = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            CrmFile crmFile = new CrmFile();
            crmFile.setId(ids.get(i));
            crmFile.setIsDeleted(true);
            crmFileList.add(crmFile);
        }
        this.updateBatchById(crmFileList);
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmFileForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmFileForExcel(paramMap);
    }

    @Override
    public void doFileProcessing(List<CrmFile> files, Long businessId, String code) {
        if (!CollectionUtil.isEmpty(files)) {
            //清除原来数据
            this.baseMapper.update(new CrmFile().setIsDeleted(true), new QueryWrapper<>(new CrmFile().setBusinessId(businessId).setCode(code).setIsDeleted(false)));
            files.forEach(e -> {
                e.setBusinessId(businessId).setCode(code).setId(null);
            });
            this.saveOrUpdateBatch(files);
        }
    }

    @Override
    public List<CrmFile> getFiles(Long id, String code) {
        List<CrmFile> files = this.baseMapper.selectList(new QueryWrapper<>(new CrmFile().setBusinessId(id).setCode(code).setIsDeleted(false)));
        return files;
    }


    public String getNextCode(String code) {
        BaseResult baseResult = authClient.getOrderFeign(code, new Date());
        return ((HashMap) baseResult.getResult()).get("order").toString();
    }

}
