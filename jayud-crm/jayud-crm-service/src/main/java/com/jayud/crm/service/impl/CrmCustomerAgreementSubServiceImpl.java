package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.constant.SysTips;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.crm.feign.AuthClient;
import com.jayud.crm.feign.FileClient;
import com.jayud.crm.model.bo.AddCrmCustomerAgreementSubForm;
import com.jayud.crm.model.constant.CrmDictCode;
import com.jayud.crm.model.enums.FileModuleEnum;
import com.jayud.crm.model.po.CrmContractQuotation;
import com.jayud.crm.model.po.CrmCustomerAgreement;
import com.jayud.crm.model.po.CrmFile;
import com.jayud.crm.model.vo.CrmCustomerAgreementSubVO;
import com.jayud.crm.model.vo.CrmCustomerAgreementVO;
import com.jayud.crm.service.ICrmCustomerAgreementService;
import com.jayud.crm.service.ICrmFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmCustomerAgreementSub;
import com.jayud.crm.mapper.CrmCustomerAgreementSubMapper;
import com.jayud.crm.service.ICrmCustomerAgreementSubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 基本档案_协议管理_子协议(crm_customer_agreement_sub) 服务实现类
 *
 * @author jayud
 * @since 2022-03-03
 */
@Slf4j
@Service
public class CrmCustomerAgreementSubServiceImpl extends ServiceImpl<CrmCustomerAgreementSubMapper, CrmCustomerAgreementSub> implements ICrmCustomerAgreementSubService {


    @Autowired
    private CrmCustomerAgreementSubMapper crmCustomerAgreementSubMapper;
    @Autowired
    private ICrmCustomerAgreementService crmCustomerAgreementService;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private ICrmFileService crmFileService;
    @Autowired
    private FileClient fileClient;

    @Override
    public IPage<CrmCustomerAgreementSubVO> selectPage(CrmCustomerAgreementSub crmCustomerAgreementSub,
                                                       Integer currentPage,
                                                       Integer pageSize,
                                                       HttpServletRequest req) {

        Page<CrmCustomerAgreementSub> page = new Page<CrmCustomerAgreementSub>(currentPage, pageSize);
        IPage<CrmCustomerAgreementSubVO> pageList = crmCustomerAgreementSubMapper.pageList(page, crmCustomerAgreementSub);
        Object url = this.fileClient.getBaseUrl().getData();
        for (CrmCustomerAgreementSubVO record : pageList.getRecords()) {
            List<CrmFile> files = this.crmFileService.list(new QueryWrapper<>(new CrmFile().setIsDeleted(false).setBusinessId(record.getId()).setCode(FileModuleEnum.SUB_CA.getCode())));
            files.forEach(e -> {
                e.setUploadFileUrl(url + e.getUploadFileUrl());
            });
            record.setEffectiveTime(record.getBeginDate() + "-" + record.getEndDate());
            record.setFiles(files);
        }

        return pageList;
    }

    @Override
    public List<CrmCustomerAgreementSub> selectList(CrmCustomerAgreementSub crmCustomerAgreementSub) {
        return crmCustomerAgreementSubMapper.list(crmCustomerAgreementSub);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmCustomerAgreementSubMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmCustomerAgreementSubMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmCustomerAgreementSubForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmCustomerAgreementSubForExcel(paramMap);
    }

    @Override
    public Map<String, Object> autoGenerateNum(Long caId) {
        CrmCustomerAgreement tmp = this.crmCustomerAgreementService.getById(caId);
        List<CrmCustomerAgreementSub> list = this.baseMapper.list(new CrmCustomerAgreementSub().setPId(caId));

        Object result = this.authClient.getOrderFeign(CrmDictCode.SUB_CONTRACT_AGREEMENT_NUM_CODE, new Date()).getResult();
        JSONObject jsonObject = new JSONObject(result);
        Map<String, Object> map = new HashMap<>(16);
        map.put("fLevel", jsonObject.getInt("fLevel"));
        map.put("fStep", jsonObject.getInt("fStep"));
        map.put("checkStateFlag", jsonObject.getInt("checkStateFlag"));
        map.put("order", tmp.getAgreementCode() + "-" + list.size() + 1);
        return map;
    }

    @Override
    public void saveOrUpdate(AddCrmCustomerAgreementSubForm form) {
        CrmCustomerAgreementSub tmp = ConvertUtil.convert(form, CrmCustomerAgreementSub.class);
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
        this.crmFileService.doFileProcessing(form.getFiles(), form.getId(), FileModuleEnum.SUB_CA.getCode());
    }

    @Override
    public boolean exitNumber(String agreementCode) {
        QueryWrapper<CrmCustomerAgreementSub> condition = new QueryWrapper<>(new CrmCustomerAgreementSub().setAgreementCode(agreementCode));
        return this.count(condition) > 0;
    }

}
