package com.jayud.crm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.constant.SysTips;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.crm.feign.FileClient;
import com.jayud.crm.model.bo.AddCrmCustomerAgreementForm;
import com.jayud.crm.model.enums.FileModuleEnum;
import com.jayud.crm.model.po.CrmContractQuotation;
import com.jayud.crm.model.po.CrmFile;
import com.jayud.crm.model.vo.CrmContractQuotationVO;
import com.jayud.crm.model.vo.CrmCustomerAgreementVO;
import com.jayud.crm.service.ICrmFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerAgreement;
import com.jayud.crm.mapper.CrmCustomerAgreementMapper;
import com.jayud.crm.service.ICrmCustomerAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本档案_协议管理(crm_customer_agreement) 服务实现类
 *
 * @author jayud
 * @since 2022-03-02
 */
@Slf4j
@Service
public class CrmCustomerAgreementServiceImpl extends ServiceImpl<CrmCustomerAgreementMapper, CrmCustomerAgreement> implements ICrmCustomerAgreementService {


    @Autowired
    private CrmCustomerAgreementMapper crmCustomerAgreementMapper;
    @Autowired
    private ICrmFileService crmFileService;
    @Autowired
    private FileClient fileClient;

    @Override
    public IPage<CrmCustomerAgreementVO> selectPage(CrmCustomerAgreement crmCustomerAgreement,
                                                    Integer currentPage,
                                                    Integer pageSize,
                                                    HttpServletRequest req) {

        Page<CrmCustomerAgreement> page = new Page<CrmCustomerAgreement>(currentPage, pageSize);
        IPage<CrmCustomerAgreementVO> pageList = crmCustomerAgreementMapper.pageList(page, crmCustomerAgreement);
        Object url = this.fileClient.getBaseUrl().getData();
        for (CrmCustomerAgreementVO record : pageList.getRecords()) {
            List<CrmFile> files = this.crmFileService.list(new QueryWrapper<>(new CrmFile().setIsDeleted(true).setBusinessId(record.getId()).setCode(FileModuleEnum.CQ.getCode())));
            files.forEach(e -> {
                e.setUploadFileUrl(url + e.getUploadFileUrl());
            });
            record.setEffectiveTime(record.getBeginDate() + "-" + record.getEndDate());
            record.setFiles(files);
        }
        return pageList;
    }

    @Override
    public List<CrmCustomerAgreementVO> selectList(CrmCustomerAgreement crmCustomerAgreement) {
        return crmCustomerAgreementMapper.list(crmCustomerAgreement);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCustomerAgreementMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCustomerAgreementMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerAgreementForExcel(paramMap);
    }

    @Override
    public void saveOrUpdate(AddCrmCustomerAgreementForm form) {
        CrmCustomerAgreement tmp = ConvertUtil.convert(form, CrmCustomerAgreement.class);
        if (form.getId() == null) {
            if (this.exitNumber(form.getAgreementCode())) {
                throw new JayudBizException(400, SysTips.NUM_ALREADY_EXISTS);
            }
            tmp.setCheckStateFlag("N0").setTenantCode(CurrentUserUtil.getUserTenantCode());
        } else {
            tmp.setUpdateBy(CurrentUserUtil.getUsername());
        }
        this.saveOrUpdate(tmp);
        //文件处理
        this.crmFileService.doFileProcessing(form.getFiles(), form.getId(), FileModuleEnum.CA.getCode());
    }

    @Override
    public boolean exitNumber(String agreementCode) {
        QueryWrapper<CrmCustomerAgreement> condition = new QueryWrapper<>(new CrmCustomerAgreement().setAgreementCode(agreementCode));
        return this.count(condition) > 0;
    }


}
