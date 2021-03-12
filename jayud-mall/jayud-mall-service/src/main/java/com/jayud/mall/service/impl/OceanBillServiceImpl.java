package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 提单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Service
public class OceanBillServiceImpl extends ServiceImpl<OceanBillMapper, OceanBill> implements IOceanBillService {

    @Autowired
    OceanBillMapper oceanBillMapper;

    @Autowired
    OceanCounterMapper oceanCounterMapper;

    @Autowired
    BillCopePayMapper billCopePayMapper;

    @Autowired
    OrderCopeReceivableMapper orderCopeReceivableMapper;

    @Autowired
    OrderCopeWithMapper orderCopeWithMapper;

    @Autowired
    BillTaskRelevanceMapper billTaskRelevanceMapper;

    @Autowired
    CounterCaseMapper counterCaseMapper;

    @Autowired
    IOceanCounterService oceanCounterService;

    @Autowired
    IBillTaskRelevanceService billTaskRelevanceService;

    @Autowired
    IBillCopePayService billCopePayService;

    @Autowired
    IOrderCopeReceivableService orderCopeReceivableService;

    @Autowired
    IOrderCopeWithService orderCopeWithService;

    @Autowired
    BaseService baseService;

    @Override
    public IPage<OceanBillVO> findOceanBillByPage(QueryOceanBillForm form) {
        //定义分页参数
        Page<OceanBillVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OceanBillVO> pageInfo = oceanBillMapper.findOceanBillByPage(page, form);

        //设置柜号list
        List<OceanBillVO> records = pageInfo.getRecords();
        records.forEach(oceanBillVO -> {
            Long obId = oceanBillVO.getId();
            QueryWrapper<OceanCounter> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ob_id", obId);
            List<OceanCounter> oceanCounters = oceanCounterMapper.selectList(queryWrapper);
            List<OceanCounterVO> oceanCounterVOList = ConvertUtil.convertList(oceanCounters, OceanCounterVO.class);
            oceanBillVO.setOceanCounterVOList(oceanCounterVOList);
        });
        return pageInfo;
    }

    /**
     * <p>保存提单信息</p>
     * <p>1个提单对应1个柜子</p>
     * <p>1个柜子对应N个运单箱号(订单)</p>
     * <p>1个运单对应N个箱号</p>
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OceanBillVO> saveOceanBill(OceanBillForm form) {
        //1.保存提单
        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        Long id = oceanBill.getId();
        if (ObjectUtil.isEmpty(id)){
            AuthUser user = baseService.getUser();
            oceanBill.setUserId(user.getId().intValue());
            oceanBill.setUserName(user.getName());
            oceanBill.setCreateTime(LocalDateTime.now());
        }

        this.saveOrUpdate(oceanBill);
        Long obId = oceanBill.getId();//提单id
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();
        List<OceanCounter> oceanCounterList = new ArrayList<>();
        oceanCounterForms.forEach(oceanCounterForm -> {
            OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
            oceanCounter.setObId(obId);
            oceanCounter.setStatus("1");//状态(0无效 1有效)
            oceanCounter.setCreateTime(LocalDateTime.now());
            oceanCounterList.add(oceanCounter);
        });
        //2.保存提单对应的柜子
        QueryWrapper<OceanCounter> oceanCounterQueryWrapper = new QueryWrapper<>();
        oceanCounterQueryWrapper.eq("ob_id", obId);
        oceanCounterService.remove(oceanCounterQueryWrapper);
        oceanCounterService.saveOrUpdateBatch(oceanCounterList);
        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //3.保存提单关联任务
        List<BillTaskRelevanceVO> billTaskRelevanceVOS =
                billTaskRelevanceService.savebillTaskRelevance(oceanBill);

        return CommonResult.success(oceanBillVO);
    }

    /**
     * <p>查看提单</p>
     * <p>1个提单对应1(N)个柜子</p>
     * <p>1个柜子对应N个运单箱号</p>
     * @param id 提单id
     * @return
     */
    @Override
    public CommonResult<OceanBillVO> lookOceanBill(Long id) {
        //提单信息
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(id);
        if(ObjectUtil.isEmpty(oceanBillVO)){
            return CommonResult.error(-1, "没有找到提单信息");
        }

        Long obId = oceanBillVO.getId();
        //1个提单对应1(N)个柜子
        List<OceanCounterVO> oceanCounterVOS = oceanCounterMapper.findOceanCounterVOByObId(obId);
        oceanBillVO.setOceanCounterVOList(oceanCounterVOS);

        //提单的装柜信息
        OceanCounterCaseVO oceanCounterCaseVO = new OceanCounterCaseVO();
        List<CounterCaseVO> counterCaseVOS = counterCaseMapper.findCounterCaseByObId(obId);
        oceanCounterCaseVO.setCounterCaseVOS(counterCaseVOS);
        BigDecimal counterCaseVolumeTotal = counterCaseMapper.findCounterCaseVolumeTotalByObId(obId);
        oceanCounterCaseVO.setCounterCaseVolumeTotal(counterCaseVolumeTotal);
        oceanBillVO.setOceanCounterCaseVO(oceanCounterCaseVO);

        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<OceanBillVO> billLadingCost(Long id) {
        OceanBillVO oceanBillVO = oceanBillMapper.billLadingCost(id);
        if(oceanBillVO == null){
            return CommonResult.error(-1, "提单不存在");
        }
        Long billId = oceanBillVO.getId();
        //提单费用信息
        //根据提单id，查询提单费用（提单应收费用）
        List<BillCopePayVO> billCopePayVOS = billCopePayMapper.findBillCopePayByBillId(billId);

        BillCostInfoVO billCostInfoVO = new BillCostInfoVO();
        billCostInfoVO.setId(oceanBillVO.getId());
        billCostInfoVO.setSupplierId(oceanBillVO.getSupplierId());
        if(billCopePayVOS != null && billCopePayVOS.size() > 0){
            BigDecimal amountTotal = new BigDecimal("0");
            for (int i=0; i<billCopePayVOS.size(); i++){
                BillCopePayVO billCopePayVO = billCopePayVOS.get(i);
                BigDecimal amount = billCopePayVO.getAmount();
                amountTotal = amountTotal.add(amount);
            }
            String currencyName = billCopePayVOS.get(0).getCurrencyName();
            String billCopePayTotal = amountTotal.toString()+" "+currencyName;
            billCostInfoVO.setBillCopePayForms(billCopePayVOS);
            billCostInfoVO.setBillCopePayTotal(billCopePayTotal);
        }else{
            billCostInfoVO.setBillCopePayForms(billCopePayVOS);
        }
        oceanBillVO.setBillCostInfoVO(billCostInfoVO);

        //提单对应的订单 以及 费用信息 TODO
        List<BillOrderCostInfoVO> billOrderCostInfoVOS = oceanBillMapper.findBillOrderCostInfo(id);
        billOrderCostInfoVOS.forEach(billOrderCostInfoVO -> {
            Long orderId = billOrderCostInfoVO.getId();
            //应收
            List<OrderCopeReceivableVO> orderCopeReceivableVOS = orderCopeReceivableMapper.findOrderCopeReceivableByOrderIdAndBillId(orderId, billId);
            billOrderCostInfoVO.setOrderCopeReceivableVOS(orderCopeReceivableVOS);
            //应付
            List<OrderCopeWithVO> orderCopeWithVOS = orderCopeWithMapper.findOrderCopeWithByOrderIdAndBillId(orderId, billId);
            billOrderCostInfoVO.setOrderCopeWithVOS(orderCopeWithVOS);
        });
        oceanBillVO.setBillOrderCostInfoVOS(billOrderCostInfoVOS);

        return CommonResult.success(oceanBillVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<BillCostInfoVO> saveBillCostInfo(BillCostInfoForm form) {
        Long billId = form.getId();//提单id
        OceanBillVO oceanBillVO = oceanBillMapper.billLadingCost(billId);
        if(oceanBillVO == null){
            return CommonResult.error(-1, "提单不存在");
        }
        Integer supplierId = form.getSupplierId();//供应商id
        List<BillCopePayForm> billCopePayForms = form.getBillCopePayForms();

        List<BillCopePay> billCopePays = new ArrayList<>();
        billCopePayForms.forEach(billCopePayForm -> {
            BillCopePay billCopePay = ConvertUtil.convert(billCopePayForm, BillCopePay.class);
            billCopePay.setBillId(billId);
            billCopePay.setSupplierId(supplierId);
            billCopePays.add(billCopePay);
        });
        //批量保存-提单费用
        billCopePayService.saveOrUpdateBatch(billCopePays);

        BillCostInfoVO billCostInfoVO = ConvertUtil.convert(form, BillCostInfoVO.class);
        List<BillCopePayVO> billCopePayVOS = ConvertUtil.convertList(billCopePays, BillCopePayVO.class);
        billCostInfoVO.setBillCopePayForms(billCopePayVOS);

        return CommonResult.success(billCostInfoVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<OceanBillVO> shareEqually(BillCostInfoForm form) {
        Long billId = form.getId();//提单id
        //提单信息
        OceanBillVO oceanBillVO = oceanBillMapper.billLadingCost(billId);
        if(oceanBillVO == null){
            return CommonResult.error(-1, "提单不存在");
        }
        //提单费用信息
        //根据提单id，查询提单费用（提单应收费用）
        List<BillCopePayVO> billCopePayVOS = billCopePayMapper.findBillCopePayByBillId(billId);
        if(billCopePayVOS == null || billCopePayVOS.size() == 0){
            return CommonResult.error(-1, "提单费用不存在，请先保存提单费用，在一键均摊");
        }
        //提单对应的订单 以及 费用信息 TODO
        List<BillOrderCostInfoVO> billOrderCostInfoVOS = oceanBillMapper.findBillOrderCostInfo(billId);
        //分摊基数 base 等于 订单计费重的累加
        BigDecimal base = new BigDecimal("0");
        BigDecimal zero = new BigDecimal("0");
        for(int i=0; i<billOrderCostInfoVOS.size(); i++){
            BillOrderCostInfoVO billOrderCostInfoVO = billOrderCostInfoVOS.get(i);
            BigDecimal chargeWeight = billOrderCostInfoVO.getChargeWeight() == null ? new BigDecimal("0") : billOrderCostInfoVO.getChargeWeight();
            if (chargeWeight.compareTo(zero) != 1){
                // chargeWeight > zero ,返回1
                String orderNo = billOrderCostInfoVO.getOrderNo();
                return CommonResult.error(-1, "订单号["+orderNo+"],计费重不能小于或等于0");
            }
            base = base.add(chargeWeight);
        }
        //订单
        for (int i = 0; i<billOrderCostInfoVOS.size(); i++) {
            BillOrderCostInfoVO billOrderCostInfoVO = billOrderCostInfoVOS.get(i);
            Long orderId = billOrderCostInfoVO.getId();//订单id
            BigDecimal chargeWeight = billOrderCostInfoVO.getChargeWeight();//计费重
            //构造 订单对应应收费用明细
            List<OrderCopeReceivable> orderCopeReceivables = new ArrayList<>();
            //构造 订单对应应付费用明细
            List<OrderCopeWith> orderCopeWiths = new ArrayList<>();
            //提单费用
            for (int j=0; j<billCopePayVOS.size(); j++){
                BillCopePayVO billCopePayVO = billCopePayVOS.get(j);
                String costName = billCopePayVO.getCostName();//费用名称(cost_item cost_name)
                //金额  提单费用初始金额
                BigDecimal initAmount = billCopePayVO.getAmount() == null ? new BigDecimal("0") : billCopePayVO.getAmount();
                if (initAmount.compareTo(zero) != 1) {
                    // chargeWeight > zero ,返回1
                    return CommonResult.error(-1, "提单费用[" + costName + "],金额不能小于或等于0");
                }
                //订单费用明细金额 = 计费重 / 分摊基数 * 提单费用初始金额
                //amount = chargeWeight / base * initAmount
                BigDecimal amount = chargeWeight.divide(base).multiply(initAmount);

                //订单对应应收费用明细 OrderCopeReceivable
                OrderCopeReceivable orderCopeReceivable = ConvertUtil.convert(billCopePayVO, OrderCopeReceivable.class);
                orderCopeReceivable.setId(null);
                orderCopeReceivable.setOrderId(orderId);
                orderCopeReceivable.setAmount(amount);
                orderCopeReceivables.add(orderCopeReceivable);

                //订单对应应付费用明细 OrderCopeWith
                OrderCopeWith orderCopeWith = ConvertUtil.convert(billCopePayVO, OrderCopeWith.class);
                orderCopeWith.setId(null);
                orderCopeWith.setOrderId(orderId);
                orderCopeWith.setAmount(amount);
                orderCopeWiths.add(orderCopeWith);
            }
            QueryWrapper<OrderCopeReceivable> orderCopeReceivableQueryWrapper = new QueryWrapper<>();
            orderCopeReceivableQueryWrapper.eq("order_id", orderId);
            orderCopeReceivableQueryWrapper.eq("bill_id", billId);
            orderCopeReceivableService.remove(orderCopeReceivableQueryWrapper);
            orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);
            QueryWrapper<OrderCopeWith> orderCopeWithQueryWrapper = new QueryWrapper<>();
            orderCopeWithQueryWrapper.eq("order_id", orderId);
            orderCopeWithQueryWrapper.eq("bill_id", billId);
            orderCopeWithService.remove(orderCopeWithQueryWrapper);
            orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);
        }

        //提单费用信息
        BillCostInfoVO billCostInfoVO = new BillCostInfoVO();
        billCostInfoVO.setId(oceanBillVO.getId());
        billCostInfoVO.setSupplierId(oceanBillVO.getSupplierId());
        if(billCopePayVOS != null && billCopePayVOS.size() > 0){
            BigDecimal amountTotal = new BigDecimal("0");
            for (int i=0; i<billCopePayVOS.size(); i++){
                BillCopePayVO billCopePayVO = billCopePayVOS.get(i);
                BigDecimal amount = billCopePayVO.getAmount();
                amountTotal = amountTotal.add(amount);
            }
            String currencyName = billCopePayVOS.get(0).getCurrencyName();
            String billCopePayTotal = amountTotal.toString()+" "+currencyName;
            billCostInfoVO.setBillCopePayForms(billCopePayVOS);
            billCostInfoVO.setBillCopePayTotal(billCopePayTotal);
        }
        oceanBillVO.setBillCostInfoVO(billCostInfoVO);

        //提单对应的订单 以及 费用信息 TODO
        billOrderCostInfoVOS.forEach(billOrderCostInfoVO -> {
            Long orderId = billOrderCostInfoVO.getId();
            //应收
            List<OrderCopeReceivableVO> orderCopeReceivableVOS = orderCopeReceivableMapper.findOrderCopeReceivableByOrderIdAndBillId(orderId, billId);
            billOrderCostInfoVO.setOrderCopeReceivableVOS(orderCopeReceivableVOS);
            //应付
            List<OrderCopeWithVO> orderCopeWithVOS = orderCopeWithMapper.findOrderCopeWithByOrderIdAndBillId(orderId, billId);
            billOrderCostInfoVO.setOrderCopeWithVOS(orderCopeWithVOS);
        });
        oceanBillVO.setBillOrderCostInfoVOS(billOrderCostInfoVOS);

        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<OceanBillVO> lookOceanBillTask(Long obId) {
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(obId);
        if(ObjectUtil.isEmpty(oceanBillVO)){
            return CommonResult.error(-1, "没有找到提单");
        }

        //根据提单id，查询提单关联的任务，查看完成情况
        List<BillTaskRelevanceVO> billTaskRelevanceVOS = oceanBillMapper.findBillTaskRelevanceByObId(obId);
        oceanBillVO.setBillTaskRelevanceVOS(billTaskRelevanceVOS);
        return CommonResult.success(oceanBillVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<BillTaskRelevanceVO> confirmCompleted(Long id) {
        AuthUser user = baseService.getUser();
        BillTaskRelevance billTaskRelevance = billTaskRelevanceMapper.selectById(id);
        if(ObjectUtil.isEmpty(billTaskRelevance)){
            return CommonResult.error(-1, "没有找到此任务");
        }
        Integer loginUserId = user.getId().intValue();
        Integer taskUserId = billTaskRelevance.getUserId();
        if(!loginUserId.equals(taskUserId)){
            return CommonResult.error(-1, "只有本人才能点击完成");
        }
        billTaskRelevance.setStatus("3");//状态(0未激活 1已激活 2异常 3已完成)
        billTaskRelevance.setUpTime(LocalDateTime.now());
        billTaskRelevanceService.saveOrUpdate(billTaskRelevance);
        BillTaskRelevanceVO billTaskRelevanceVO = ConvertUtil.convert(billTaskRelevance, BillTaskRelevanceVO.class);
        return CommonResult.success(billTaskRelevanceVO);
    }

    @Override
    public CommonResult<List<BillTaskRelevanceVO>> lookOperateLog(Long id) {
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(id);
        if(ObjectUtil.isEmpty(oceanBillVO)){
            return CommonResult.error(-1, "订单不存在");
        }
        List<BillTaskRelevanceVO> billTaskRelevanceVOS = oceanBillMapper.lookOperateLog(id);
        return CommonResult.success(billTaskRelevanceVOS);
    }
}
