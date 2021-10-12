package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.oms.feign.FileClient;
import com.jayud.oms.model.enums.EmploymentFeeStatusEnum;
import com.jayud.oms.model.po.CostType;
import com.jayud.oms.model.po.DriverEmploymentFee;
import com.jayud.oms.mapper.DriverEmploymentFeeMapper;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverEmploymentFeeVO;
import com.jayud.oms.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 司机录入费用表(小程序使用) 服务实现类
 * </p>
 *
 * @author 李达荣
 * @since 2020-11-13
 */
@Service
public class DriverEmploymentFeeServiceImpl extends ServiceImpl<DriverEmploymentFeeMapper, DriverEmploymentFee> implements IDriverEmploymentFeeService {

    @Autowired
    private IOrderPaymentCostService orderPaymentCostService;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private ICostInfoService costInfoService;
    @Autowired
    private ICostTypeService costTypeService;
    @Autowired
    private ICostGenreService costGenreService;

    /**
     * 根据订单编码查询录用费用明细
     *
     * @param orderNo
     * @return
     */
    @Override
    public List<DriverEmploymentFee> getEmploymentFee(String orderNo, Long driverId, String status) {
        QueryWrapper<DriverEmploymentFee> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverEmploymentFee::getOrderNo, orderNo);
        if (!StringUtils.isEmpty(status)) {
            condition.lambda().eq(DriverEmploymentFee::getStatus, status);
        }
        if (driverId != null) {
            condition.lambda().eq(DriverEmploymentFee::getDriverId, driverId);
        }
        return this.baseMapper.selectList(condition);
    }


    @Override
    public List<DriverEmploymentFee> getEmploymentFee(Long orderId, Long driverId, String status) {
        QueryWrapper<DriverEmploymentFee> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverEmploymentFee::getOrderId, orderId);
        if (!StringUtils.isEmpty(status)) {
            condition.lambda().eq(DriverEmploymentFee::getStatus, status);
        }
        if (driverId != null) {
            condition.lambda().eq(DriverEmploymentFee::getDriverId, driverId);
        }
        return this.baseMapper.selectList(condition);
    }

    /**
     * 费用是否已提交
     */
    @Override
    public boolean isExist(Long driverId, Long orderId, String status) {
        QueryWrapper<DriverEmploymentFee> condition = new QueryWrapper<>();
        condition.lambda().eq(DriverEmploymentFee::getDriverId, driverId)
                .eq(DriverEmploymentFee::getOrderId, orderId)
                .eq(DriverEmploymentFee::getStatus, status);
        return this.count(condition) > 0;
    }

    /**
     * 司机费用提交
     */
    @Override
    @Transactional
    public void feeSubmission(List<DriverEmploymentFee> driverEmploymentFees) {
        List<DriverEmploymentFee> tmp = new ArrayList<>(driverEmploymentFees.size());
        List<OrderPaymentCost> orderPaymentCosts = new ArrayList<>(driverEmploymentFees.size());
        LocalDateTime now = LocalDateTime.now();
        for (DriverEmploymentFee driverEmploymentFee : driverEmploymentFees) {
            OrderPaymentCost paymentCost = ConvertUtil.convert(driverEmploymentFee, OrderPaymentCost.class);
            List<CostType> costTypes = this.costTypeService.getByCondition(new CostType().setCodeName("陆运"));
            Long costGenreId = this.costGenreService.getIdByName("跨境陆运");
            Long costTypeId = null;
            if (!CollectionUtils.isEmpty(costTypes)) {
                costTypeId = costTypes.get(0).getId();
            }
            paymentCost.setCustomerCode(driverEmploymentFee.getSupplierCode())
                    .setCustomerName(driverEmploymentFee.getSupplierName())
                    .setSubType(SubOrderSignEnum.ZGYS.getSignOne())
                    .setStatus(Integer.valueOf(OrderStatusEnum.COST_1.getCode()))
                    .setDriverCostId(driverEmploymentFee.getId())
                    .setExchangeRate(driverEmploymentFee.getExchangeRate())
                    .setUnitPrice(driverEmploymentFee.getAmount())
                    .setUnit("PCS").setNumber(new BigDecimal(1))
                    .setCostTypeId(costTypeId)
                    .setCostGenreId(costGenreId);

            DriverEmploymentFee employmentFee = new DriverEmploymentFee()
                    .setId(driverEmploymentFee.getId())
                    .setStatus(EmploymentFeeStatusEnum.SUBMITTED.getCode());
            orderPaymentCosts.add(paymentCost.setCreatedTime(now));
            tmp.add(employmentFee);
        }
        //批量修改录用项状态
        this.updateBatchById(tmp);
        //批量录入费用
        orderPaymentCostService.saveBatch(orderPaymentCosts);
    }

    @Override
    public List<DriverEmploymentFeeVO> getEmploymentFeeInfo(String orderNo) {
        return this.baseMapper.getEmploymentFeeInfo(orderNo);
    }

    @Override
    public List<DriverEmploymentFee> getByCondition(DriverEmploymentFee driverEmploymentFee) {
        return this.baseMapper.selectList(new QueryWrapper<>(driverEmploymentFee));
    }

    @Override
    public List<DriverEmploymentFee> getByOrderNos(List<String> orderNos, String status) {
        QueryWrapper<DriverEmploymentFee> condition = new QueryWrapper<>();
        condition.lambda().in(DriverEmploymentFee::getOrderNo, orderNos);
        if (StringUtils.isNotEmpty(status)) {
            condition.lambda().eq(DriverEmploymentFee::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public List<DriverEmploymentFeeVO> getCost(Long orderId, Long driverId, String status) {

        List<DriverEmploymentFee> employmentFees = this.getEmploymentFee(orderId, driverId, status);
        List<DriverEmploymentFeeVO> employmentFeeVOS = new ArrayList<>();
        Map<String, String> currencyMap = currencyInfoService.initCurrencyInfo().stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        Map<String, String> costInfoMap = costInfoService.list().stream().collect(Collectors.toMap(e -> e.getIdCode(), e -> e.getName()));
        Object url = fileClient.getBaseUrl().getData();
        for (DriverEmploymentFee employmentFee : employmentFees) {
            DriverEmploymentFeeVO tmp = ConvertUtil.convert(employmentFee, DriverEmploymentFeeVO.class);
            tmp.setFileDetails(com.jayud.common.utils.StringUtils.getFileViews(tmp.getFiles(), tmp.getFileName(), url.toString()));
            tmp.setCurrency(currencyMap.get(tmp.getCurrencyCode()));
            tmp.setCostName(costInfoMap.get(tmp.getCostCode()));
            employmentFeeVOS.add(tmp);
        }

        return employmentFeeVOS;
    }


}
