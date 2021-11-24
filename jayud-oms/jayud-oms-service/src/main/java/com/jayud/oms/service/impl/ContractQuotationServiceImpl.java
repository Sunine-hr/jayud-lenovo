package com.jayud.oms.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.*;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.mapper.ContractQuotationMapper;
import com.jayud.oms.model.bo.AddContractQuotationDetailsForm;
import com.jayud.oms.model.bo.AddContractQuotationForm;
import com.jayud.oms.model.bo.GetOrderDetailForm;
import com.jayud.oms.model.bo.QueryContractQuotationForm;
import com.jayud.oms.model.enums.AuditTypeDescEnum;
import com.jayud.oms.model.enums.ContractQuotationProStatusEnum;
import com.jayud.oms.model.po.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
    @Autowired
    private ICostTypeService costTypeService;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private ITrackingInfoService trackingInfoService;
    @Autowired
    private IAuditInfoService auditInfoService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private ICostGenreService costGenreService;
    @Autowired
    private IProductBizService productBizService;


    @Override
    @Transactional
    public void saveOrUpdate(AddContractQuotationForm form) {
        ContractQuotation contractQuotation = ConvertUtil.convert(form, ContractQuotation.class);
        contractQuotation.setFile(StringUtils.getFileStr(form.getFiles())).setFileName(StringUtils.getFileNameStr(form.getFiles()));
//        contractQuotation.setCustomerCode(customerInfoService.getById(form.getCustomerId()).getIdCode());
        if (form.getId() == null) {
            if (this.exitNumber(form.getNumber())) {
                throw new JayudBizException(400, "该报价编号已存在");
            }
            contractQuotation.setOptStatus(ContractQuotationProStatusEnum.ONE.getCode()).setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
//            if (form.getAuditStatus().equals(1)) {
//                throw new JayudBizException(400, "已审核的信息无法修改,请进行反审核");
//            }
            contractQuotation.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(contractQuotation);
        form.setId(contractQuotation.getId());
        this.doQuotationProcessing(form);
    }

    private void doQuotationProcessing(AddContractQuotationForm form) {
        List<AddContractQuotationDetailsForm> list = new ArrayList<>();
        list.addAll(form.getTmsDetails());
        list.addAll(form.getBgDetails());
        List<ContractQuotationDetails> details = new ArrayList<>();
        List<ContractQuotationDetails> oldTmp = this.contractQuotationDetailsService.getByCondition(new ContractQuotationDetails()
                .setContractQuotationId(form.getId()).setStatus(StatusEnum.ENABLE.getCode()));
        Map<Long, ContractQuotationDetails> oldMap = oldTmp.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        Map<String, String> costInfoMap = this.costInfoService.list().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
        Map<Long, String> costTypeMap = costTypeService.list().stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getCodeName()));
        Map<String, String> currencyInfoMap = this.currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        Map<String, String> map = new HashMap<>();
        list.forEach(e -> {
            ContractQuotationDetails convert = ConvertUtil.convert(e, ContractQuotationDetails.class);
            ContractQuotationDetails remove = oldMap.remove(e.getId());
            if (e.getId() == null) {
                convert.setContractQuotationId(form.getId()).setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
            } else {
                String value = ComparisonOptUtil.getDifferentValuesStr(remove, e);
                this.mappingOpt(value, map, convert, remove, costInfoMap, costTypeMap, currencyInfoMap);
                convert.setContractQuotationId(form.getId()).setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
            }
            details.add(convert);

        });
        List<TrackingInfo> trackingInfos = new ArrayList<>();
        map.forEach((k, v) -> {
            TrackingInfo t = new TrackingInfo();
            t.setType(SubOrderSignEnum.getEnum(k).getDesc());
            t.setContent(v);
            t.setBusinessId(form.getId());
            t.setBusinessType(TrackingInfoBisTypeEnum.ONE.getCode());
            t.setCreateTime(LocalDateTime.now());
            t.setCreateUser(UserOperator.getToken());
            t.setOptType(1);
            trackingInfos.add(t);
        });
        ContractQuotationDetails tmp = new ContractQuotationDetails();
        Map<String, String> deleteMap = new HashMap<>();
        oldMap.forEach((k, v) -> {
            ContractQuotationDetails delete = new ContractQuotationDetails();
            AddContractQuotationDetailsForm convert = ConvertUtil.convert(v, AddContractQuotationDetailsForm.class);
            delete.setId(k).setStatus(StatusEnum.DELETE.getCode());
            String value = ComparisonOptUtil.getDifferentValuesStr(tmp, convert);
            this.mappingOpt(value, deleteMap, v, tmp, costInfoMap, costTypeMap, currencyInfoMap);
            details.add(delete);
        });
        deleteMap.forEach((k, v) -> {
            TrackingInfo t = new TrackingInfo();
            t.setType(SubOrderSignEnum.getEnum(k).getDesc());
            t.setContent(v);
            t.setBusinessId(form.getId());
            t.setBusinessType(TrackingInfoBisTypeEnum.ONE.getCode());
            t.setCreateTime(LocalDateTime.now());
            t.setCreateUser(UserOperator.getToken());
            t.setOptType(2);
            trackingInfos.add(t);
        });
        this.contractQuotationDetailsService.saveOrUpdateBatch(details);
        this.trackingInfoService.saveBatch(trackingInfos);
    }

    private void mappingOpt(String value, Map<String, String> map,
                            ContractQuotationDetails newData, ContractQuotationDetails oldData,
                            Map<String, String> costInfoMap, Map<Long, String> costTypeMap,
                            Map<String, String> currencyInfoMap) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replace(newData.getCostCode(), costInfoMap.get(newData.getCostCode()));
            value = value.replace(oldData.getCostCode() + "", MapUtil.getStr(costInfoMap, oldData.getCostCode(), ""));
            value = value.replace(newData.getCostTypeId() + "", costTypeMap.get(newData.getCostTypeId()));
            value = value.replace(oldData.getCostTypeId() + "", MapUtil.getStr(costTypeMap, oldData.getCostTypeId(), ""));
            value = value.replace(newData.getCurrencyCode(), currencyInfoMap.get(newData.getCurrencyCode()));
            value = value.replace(oldData.getCurrencyCode() + "", MapUtil.getStr(currencyInfoMap, oldData.getCurrencyCode(), ""));

            String msg = map.get(newData.getSubType());
            if (StringUtils.isEmpty(msg)) {
                map.put(newData.getSubType(), value);
            } else {
                map.put(newData.getSubType(), msg + "\n" + value);
            }
        }

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
        Object url = this.fileClient.getBaseUrl().getData();
        tmp.setFiles(StringUtils.getFileViews(contractQuotation.getFile(), contractQuotation.getFileName(), url.toString()));
        if (ContractQuotationProStatusEnum.SIX.getCode().equals(contractQuotation.getOptStatus())
                || ContractQuotationProStatusEnum.SEVEN.getCode().equals(contractQuotation.getOptStatus())) {
            tmp.setSignContractFiles(StringUtils.getFileViews(contractQuotation.getSignContractFile(), contractQuotation.getSignContractFileName(), url.toString()));
            tmp.setSignOfferFiles(StringUtils.getFileViews(contractQuotation.getSignOfferFile(), contractQuotation.getSignOfferFileName(), url.toString()));
        }

        CustomerInfo customerInfo = customerInfoService.getByCode(tmp.getCustomerCode());
        tmp.setCustomerId(customerInfo.getId());
        List<ContractQuotationDetails> details = this.contractQuotationDetailsService.getByCondition(new ContractQuotationDetails().setStatus(StatusEnum.ENABLE.getCode()).setContractQuotationId(id));
        Map<String, List<InitComboxVO>> costType = this.costInfoService.initCostTypeByCostInfoCode();
        tmp.assembleDetails(details, costType);
        tmp.assembleReviewer();
        return tmp;
    }

    @Override
    public String autoGenerateNum() {
        int count = this.baseMapper.countByTime(LocalDateTime.now());
        StringBuilder orderNo = new StringBuilder("JYD-HT-");
        orderNo.append(DateUtils.LocalDateTime2Str(LocalDateTime.now(), "yy")).append("-")
                .append(StringUtils.zeroComplement(4, count + 1));
        return orderNo.toString();
    }

    @Override
    public void auditOpt(Long id, String reasonsFailure, ContractQuotationProStatusEnum statusEnum) {
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setExtId(id).setExtDesc(AuditTypeDescEnum.FOUR.getTable())
                .setAuditTypeDesc(statusEnum.getDesc())
                .setAuditStatus(statusEnum.getCode().toString())
                .setAuditComment(reasonsFailure)
                .setAuditUser(UserOperator.getToken())
                .setCreatedUser(UserOperator.getToken())
                .setAuditTime(LocalDateTime.now()).setCreatedTime(LocalDateTime.now());

        auditInfoService.save(auditInfo);
        ContractQuotation contractQuotation = new ContractQuotation().setId(id).setReasonsFailure(reasonsFailure);
        Integer optStatus;
        if (ContractQuotationProStatusEnum.FIVE.getCode().equals(statusEnum.getCode())) {
            optStatus = statusEnum.getCode();
            contractQuotation.setLegalAudit("").setDepManagerReview("").setGeneralManagerReview("");
        } else {
            optStatus = statusEnum.getNextOpt();
            switch (statusEnum) {
                case TWO:
                    contractQuotation.setLegalAudit(UserOperator.getToken());
                    break;
                case THREE:
                    contractQuotation.setDepManagerReview(UserOperator.getToken());
                    break;
                case FOUR:
                    contractQuotation.setGeneralManagerReview(UserOperator.getToken());
                    break;
            }
        }
        this.updateById(contractQuotation.setOptStatus(optStatus));
    }

    @Override
    public List<ContractQuotation> getByCondition(ContractQuotation contractQuotation) {
        QueryWrapper<ContractQuotation> condition = new QueryWrapper<>(contractQuotation);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<ContractQuotationDetailsVO> importCost(Long mainOrderId, Long contractQuotationId) {

        OrderInfo orderInfo = this.orderInfoService.getById(mainOrderId);
        GetOrderDetailForm form = new GetOrderDetailForm();
        form.setMainOrderId(mainOrderId);
        form.setClassCode(orderInfo.getClassCode());
        InputOrderVO orderDetail = this.orderInfoService.getOrderDetail(form);

        QueryWrapper<ProductBiz> queryProductBiz = new QueryWrapper();
        queryProductBiz.eq(SqlConstant.ID_CODE, orderInfo.getBizCode());
        ProductBiz productBizs = productBizService.getOne(queryProductBiz);


        ContractQuotationVO tmp = this.getEditInfoById(contractQuotationId);
        List<ContractQuotationDetailsVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tmp.getTmsDetails())) {
            InputOrderTransportVO tms = orderDetail.getOrderTransportForm();
            if (tms != null) {
                List<String> startAddrs = tms.getOrderTakeAdrForms1().stream().map(InputOrderTakeAdrVO::getAddress).collect(Collectors.toList());
                List<String> endAddrs = tms.getOrderTakeAdrForms2().stream().map(InputOrderTakeAdrVO::getAddress).collect(Collectors.toList());
                List<ContractQuotationDetailsVO> tmsDetails = tmp.getTmsDetails();
                List<ContractQuotationDetailsVO> tmsList = tmsDetails.stream().filter(e -> e.pairingTmsRule(startAddrs, endAddrs, tms.getVehicleSize()))
                        .map(e -> e.setCostGenreId(productBizs.getCostGenreDefault()).setNumber(1)).collect(Collectors.toList());
                list.addAll(tmsList);
            }
        }
        if (!CollectionUtils.isEmpty(tmp.getBgDetails())) {
            InputOrderCustomsVO bg = orderDetail.getOrderCustomsForm();
            if (bg != null) {
                long count = bg.getSubOrders().stream().filter(e -> !OrderStatusEnum.CLOSE.getDesc().equals(e.getStatusDesc())).count();
                tmp.getBgDetails().forEach(e -> e.setNumber(count == 0 ? 1 : (int) count).setCostGenreId(productBizs.getCostGenreDefault()));
                list.addAll(tmp.getBgDetails());
            }
        }
        list.forEach(e -> e.setStatus(null).setCreateTime(null).setCreateUser(null).setUpdateUser(null).setUpdateTime(null));
        return list;
    }
}
