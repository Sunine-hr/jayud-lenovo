package com.jayud.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.ContractQuotationModeEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.enums.TrackingInfoBisTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ComparisonOptUtil;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.crm.feign.OmsClient;
import com.jayud.crm.model.bo.AddCrmContractQuotationDetailsForm;
import com.jayud.crm.model.bo.AddCrmContractQuotationForm;
import com.jayud.crm.model.po.CrmContractQuotationDetails;
import com.jayud.crm.service.ICrmContractQuotationDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.jayud.common.utils.CurrentUserUtil;
import com.jayud.crm.model.po.CrmContractQuotation;
import com.jayud.crm.mapper.CrmContractQuotationMapper;
import com.jayud.crm.service.ICrmContractQuotationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合同报价 服务实现类
 *
 * @author jayud
 * @since 2022-03-01
 */
@Slf4j
@Service
public class CrmContractQuotationServiceImpl extends ServiceImpl<CrmContractQuotationMapper, CrmContractQuotation> implements ICrmContractQuotationService {


    @Autowired
    private CrmContractQuotationMapper crmContractQuotationMapper;
    @Autowired
    private ICrmContractQuotationDetailsService crmContractQuotationDetailsService;
    @Autowired
    private OmsClient omsClient;

    @Override
    public IPage<CrmContractQuotation> selectPage(CrmContractQuotation crmContractQuotation,
                                                  Integer currentPage,
                                                  Integer pageSize,
                                                  HttpServletRequest req) {

        Page<CrmContractQuotation> page = new Page<CrmContractQuotation>(currentPage, pageSize);
        IPage<CrmContractQuotation> pageList = crmContractQuotationMapper.pageList(page, crmContractQuotation);
        return pageList;
    }

    @Override
    public List<CrmContractQuotation> selectList(CrmContractQuotation crmContractQuotation) {
        return crmContractQuotationMapper.list(crmContractQuotation);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void phyDelById(Long id) {
        crmContractQuotationMapper.phyDelById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDel(Long id) {
        crmContractQuotationMapper.logicDel(id, CurrentUserUtil.getUsername());
    }


    @Override
    public List<LinkedHashMap<String, Object>> queryCrmContractQuotationForExcel(Map<String, Object> paramMap) {
        return this.baseMapper.queryCrmContractQuotationForExcel(paramMap);
    }

    @Override
    public Long saveOrUpdate(AddCrmContractQuotationForm form) {
        CrmContractQuotation contractQuotation = ConvertUtil.convert(form, CrmContractQuotation.class);
//        contractQuotation.setFile(StringUtils.getFileStr(form.getFiles())).setFileName(StringUtils.getFileNameStr(form.getFiles()));
        if (form.getId() == null) {
            if (this.exitNumber(form.getNumber())) {
                throw new JayudBizException(400, "该报价编号已存在");
            }
            contractQuotation.setTenantCode(CurrentUserUtil.getUserTenantCode());
        } else {
//
            contractQuotation.setUpdateBy(CurrentUserUtil.getUsername());
        }
        this.saveOrUpdate(contractQuotation);
        form.setId(contractQuotation.getId());
        this.doQuotationProcessing(form);
        return contractQuotation.getId();
    }

    /**
     * 执行合同报价处理
     *
     * @param form
     */
    private void doQuotationProcessing(AddCrmContractQuotationForm form) {
        List<AddCrmContractQuotationDetailsForm> list = new ArrayList<>();
        list.addAll(form.getTmsDetails());
        list.addAll(form.getBgDetails());
        list.addAll(form.getXgDetails());
        List<CrmContractQuotationDetails> details = new ArrayList<>();
        List<CrmContractQuotationDetails> oldTmp = this.crmContractQuotationDetailsService.getBaseMapper().selectList(new QueryWrapper<>(new CrmContractQuotationDetails()
                .setContractQuotationId(form.getId()).setStatus(StatusEnum.ENABLE.getCode())));
        Map<Long, CrmContractQuotationDetails> oldMap = oldTmp.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

//        Map<String, String> costInfoMap = this.omsClient.getCostInfos().getData().stream().collect(Collectors.toMap(e -> MapUtil.getStr(e, "idCode"), e -> MapUtil.getStr(e, "name")));
//        Map<String, String> costTypeMap = this.omsClient.getCostTypes().getData().stream().collect(Collectors.toMap(e -> MapUtil.getStr(e, "id"), e -> MapUtil.getStr(e, "codeName")));
//        Map<String, String> currencyInfoMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
//        Map<String, String> map = new HashMap<>();
        //对比操作
        list.forEach(e -> {
            CrmContractQuotationDetails convert = ConvertUtil.convert(e, CrmContractQuotationDetails.class);
            CrmContractQuotationDetails remove = oldMap.remove(e.getId());
            if (e.getId() == null) {
                convert.setContractQuotationId(form.getId()).setTenantCode(CurrentUserUtil.getUserTenantCode());
            } else {
//                String value = ComparisonOptUtil.getDifferentValuesStr(remove, e);
//                this.mappingOpt(value, map, convert, remove, costInfoMap, costTypeMap, currencyInfoMap);
                convert.setContractQuotationId(form.getId());
            }
            details.add(convert);

        });
//        List<TrackingInfo> trackingInfos = new ArrayList<>();
//        map.forEach((k, v) -> {
//            TrackingInfo t = new TrackingInfo();
//            t.setType(ContractQuotationModeEnum.getEnum(k).getDesc());
//            t.setContent(v);
//            t.setBusinessId(form.getId());
//            t.setBusinessType(TrackingInfoBisTypeEnum.ONE.getCode());
//            t.setCreateTime(LocalDateTime.now());
//            t.setCreateUser(UserOperator.getToken());
//            t.setOptType(1);
//            trackingInfos.add(t);
//        });
//        CrmContractQuotationDetails tmp = new CrmContractQuotationDetails();
//        Map<String, String> deleteMap = new HashMap<>();
        oldMap.forEach((k, v) -> {
            CrmContractQuotationDetails delete = new CrmContractQuotationDetails();
            AddCrmContractQuotationDetailsForm convert = ConvertUtil.convert(v, AddCrmContractQuotationDetailsForm.class);
            delete.setIsDeleted(true).setId(k);
//            String value = ComparisonOptUtil.getDifferentValuesStr(tmp, convert);
//            this.mappingOpt(value, deleteMap, v, tmp, costInfoMap, costTypeMap, currencyInfoMap);
            details.add(delete);
        });
//        deleteMap.forEach((k, v) -> {
//            TrackingInfo t = new TrackingInfo();
//            t.setType(ContractQuotationModeEnum.getEnum(k).getDesc());
//            t.setContent(v);
//            t.setBusinessId(form.getId());
//            t.setBusinessType(TrackingInfoBisTypeEnum.ONE.getCode());
//            t.setCreateTime(LocalDateTime.now());
//            t.setCreateUser(UserOperator.getToken());
//            t.setOptType(2);
//            trackingInfos.add(t);
//        });
        this.crmContractQuotationDetailsService.saveOrUpdateBatch(details);
//        this.trackingInfoService.saveBatch(trackingInfos);
    }

    @Override
    public boolean exitNumber(String number) {
        QueryWrapper<CrmContractQuotation> condition = new QueryWrapper<>(new CrmContractQuotation().setNumber(number));
        return this.count(condition) > 0;
    }

}
