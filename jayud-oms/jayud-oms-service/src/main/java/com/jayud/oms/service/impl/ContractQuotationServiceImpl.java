package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.mapper.ContractQuotationMapper;
import com.jayud.oms.model.bo.AddContractQuotationDetailsForm;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.po.ContractQuotation;
import com.jayud.oms.model.po.ContractQuotationDetails;
import com.jayud.oms.model.po.CustomerInfo;
import com.jayud.oms.model.vo.ContractQuotationVO;
import com.jayud.oms.model.vo.InitComboxVO;
import com.jayud.oms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同报价 服务实现类
 * </p>
 *
 * @author LDR
 * @since 2021-10-26
 */
@Service
public class ContractQuotationServiceImpl extends ServiceImpl<ContractQuotationMapper, ContractQuotation> implements IContractQuotationService {

    @Autowired
    private ICustomerInfoService customerInfoService;
    @Autowired
    private IContractQuotationDetailsService contractQuotationDetailsService;
    @Autowired
    private ICostInfoService costInfoService;

    @Override
    @Transactional
    public void saveOrUpdate(AddContractQuotationForm form) {
        ContractQuotation contractQuotation = ConvertUtil.convert(form, ContractQuotation.class);
        contractQuotation.setCustomerCode(customerInfoService.getById(form.getCustomerId()).getIdCode());
        if (form.getId() == null) {
            if (this.exitNumber(form.getNumber())) {
                throw new JayudBizException(400, "该报价编号已存在");
            }
            contractQuotation.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());

        } else {
            if (form.getAuditStatus().equals(1)) {
                throw new JayudBizException(400, "已审核的信息无法修改,请进行反审核");
            }
            contractQuotation.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(contractQuotation);
        form.setId(contractQuotation.getId());
        this.doQuotationProcessing(form);
    }

    private void doQuotationProcessing(AddContractQuotationForm form) {
        List<AddContractQuotationDetailsForm> list = new ArrayList<>();
        list.addAll(form.getTmsDetails());
        List<ContractQuotationDetails> details = new ArrayList<>();
        List<ContractQuotationDetails> oldTmp = this.contractQuotationDetailsService.getByCondition(new ContractQuotationDetails()
                .setContractQuotationId(form.getId()).setStatus(StatusEnum.ENABLE.getCode()));
        Map<Long, ContractQuotationDetails> oldMap = oldTmp.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        list.forEach(e -> {
            ContractQuotationDetails convert = ConvertUtil.convert(e, ContractQuotationDetails.class);
            oldMap.remove(e.getId());
            if (e.getId() == null) {
                convert.setContractQuotationId(form.getId()).setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
            } else {
                convert.setContractQuotationId(form.getId()).setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
            }
            details.add(convert);
        });

        oldMap.forEach((k, v) -> {
            ContractQuotationDetails delete = new ContractQuotationDetails();
            delete.setId(k).setStatus(StatusEnum.DELETE.getCode());
            details.add(delete);
        });
        this.contractQuotationDetailsService.saveOrUpdateBatch(details);
    }

    @Override
    public boolean exitNumber(String number) {
        QueryWrapper<ContractQuotation> condition = new QueryWrapper<>(new ContractQuotation().setNumber(number));
        return this.count(condition) > 0;
    }

    @Override
    public boolean exitName(Long id, String name) {
        QueryWrapper<ContractQuotation> condition = new QueryWrapper<>(new ContractQuotation().setName(name));
        ContractQuotation contractQuotation = this.getOne(condition);
        if (id == null) {
            return contractQuotation != null;
        } else {
            return contractQuotation != null && !contractQuotation.getId().equals(id);
        }
    }

    @Override
    public IPage<ContractQuotationVO> findByPage(QueryContractQuotationForm form) {
        Page<ContractQuotation> page = new Page<>(form.getPageNum(), form.getPageSize());
        return this.baseMapper.findByPage(page, form);
    }

    @Override
    public ContractQuotationVO getEditInfoById(Long id) {
        ContractQuotation contractQuotation = this.getById(id);
        ContractQuotationVO tmp = ConvertUtil.convert(contractQuotation, ContractQuotationVO.class);
        CustomerInfo customerInfo = customerInfoService.getByCode(tmp.getCustomerCode());
        tmp.setCustomerId(customerInfo.getId());
        List<ContractQuotationDetails> details = this.contractQuotationDetailsService.getByCondition(new ContractQuotationDetails().setStatus(StatusEnum.ENABLE.getCode()).setContractQuotationId(id));
        Map<String, List<InitComboxVO>> costType = this.costInfoService.initCostTypeByCostInfoCode();
        tmp.assembleDetails(details, costType);
        return tmp;
    }
}
