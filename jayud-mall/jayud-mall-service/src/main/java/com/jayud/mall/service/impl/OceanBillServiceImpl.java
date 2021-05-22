package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
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
import java.math.RoundingMode;
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
    CostItemMapper costItemMapper;
    @Autowired
    OrderConfMapper orderConfMapper;
    @Autowired
    BillClearanceInfoMapper billClearanceInfoMapper;
    @Autowired
    BillCustomsInfoMapper billCustomsInfoMapper;
    @Autowired
    CounterListInfoMapper counterListInfoMapper;
    @Autowired
    BillOrderRelevanceMapper billOrderRelevanceMapper;

    @Autowired
    BaseService baseService;
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
    IOceanConfDetailService oceanConfDetailService;
    @Autowired
    IBillClearanceInfoService billClearanceInfoService;
    @Autowired
    IClearanceInfoCaseService clearanceInfoCaseService;
    @Autowired
    IBillCustomsInfoService billCustomsInfoService;
    @Autowired
    ICustomsInfoCaseService customsInfoCaseService;
    @Autowired
    ICounterListInfoService counterListInfoService;
    @Autowired
    ICounterCaseInfoService counterCaseInfoService;
    @Autowired
    IBillOrderRelevanceService billOrderRelevanceService;
    @Autowired
    IFeeCopeWithService feeCopeWithService;


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
    @Deprecated
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
        List<BillTaskRelevanceVO> billTaskRelevanceVOS = billTaskRelevanceService.savebillTaskRelevance(oceanBill);

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
            String costCode = billCopePay.getCostCode();
            CostItemVO costItemVO = costItemMapper.findCostItemByCostCode(costCode);
            String costName = costItemVO.getCostName();
            billCopePay.setCostName(costName);
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
                BigDecimal amount = chargeWeight.divide(base,4, RoundingMode.HALF_UP).multiply(initAmount);

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
        billTaskRelevance.setStatus("3");//状态(0未激活 1已激活,未完成 2已完成)
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

    @Override
    public CommonResult<OceanBillVO> saveOceanBillByConf(OceanBillForm form) {
        Long orderConfId = form.getOrderConfId();
        OrderConfVO orderConfVO = orderConfMapper.findOrderConfById(orderConfId);
        if(ObjectUtil.isEmpty(orderConfVO)){
            return CommonResult.error(-1, "配载id不存在");
        }


        OceanBill oceanBill = ConvertUtil.convert(form, OceanBill.class);
        Long id = oceanBill.getId();
        if (ObjectUtil.isEmpty(id)){
            //新增
            QueryWrapper<OceanBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", oceanBill.getOrderId());
            List<OceanBill> list = this.list(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "提单号重复");
            }

            AuthUser user = baseService.getUser();
            oceanBill.setUserId(user.getId().intValue());
            oceanBill.setUserName(user.getName());
            oceanBill.setCreateTime(LocalDateTime.now());
        }else{
            //修改
            QueryWrapper<OceanBill> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", oceanBill.getOrderId());
            queryWrapper.ne("id", id);
            List<OceanBill> list = this.list(queryWrapper);
            if(CollUtil.isNotEmpty(list)){
                return CommonResult.error(-1, "提单号重复");
            }
        }

        List<Integer> operationTeamIds = form.getOperationTeamId();
        String operationTeamId = "";
        if(CollUtil.isNotEmpty(operationTeamIds)){
            for (int i=0; i<operationTeamIds.size(); i++){
                Integer teamId = operationTeamIds.get(i);
                if(i == 0){
                    operationTeamId = teamId.toString();
                }else{
                    operationTeamId += ","+teamId.toString();
                }
            }
        }
        oceanBill.setOperationTeamId(operationTeamId);
        //1.保存提单
        this.saveOrUpdate(oceanBill);
        Long obId = oceanBill.getId();//提单id
        List<OceanCounterForm> oceanCounterForms = form.getOceanCounterForms();

        //2.保存提单对应-费用信息
        List<FeeCopeWithForm> feeCopeWithList = form.getFeeCopeWithList();
        //先刪除
        QueryWrapper<FeeCopeWith> qwFeeCopeWith = new QueryWrapper<>();
        qwFeeCopeWith.eq("qie", obId);
        qwFeeCopeWith.eq("business_type", 1);//业务类型(1提单费用 2柜子费用)
        feeCopeWithService.remove(qwFeeCopeWith);
        if(CollUtil.isNotEmpty(feeCopeWithList)){
            List<FeeCopeWith> feeCopeWiths = ConvertUtil.convertList(feeCopeWithList, FeeCopeWith.class);
            feeCopeWiths.forEach(feeCopeWith -> {
                feeCopeWith.setQie(obId.intValue());//提单id(ocean_bill id)/柜子id(ocean_counter id) 这里是提单id
                feeCopeWith.setBusinessType(1);//业务类型(1提单费用 2柜子费用)
                //计算 总金额=数量 * 单价
                Integer c = feeCopeWith.getCount() == null ? 0 : feeCopeWith.getCount();//数量
                BigDecimal count = new BigDecimal(c.toString());
                BigDecimal unitPrice = feeCopeWith.getUnitPrice() == null ? new BigDecimal("0") : feeCopeWith.getUnitPrice();//单价
                BigDecimal amount = count.multiply(unitPrice);
                feeCopeWith.setAmount(amount);
            });
            //在保存
            feeCopeWithService.saveOrUpdateBatch(feeCopeWiths);
        }

        //单独添加保存柜子，不在这里保存柜子了
//        if(CollUtil.isNotEmpty(oceanCounterForms)){
//            List<OceanCounter> oceanCounterList = new ArrayList<>();
//            oceanCounterForms.forEach(oceanCounterForm -> {
//                OceanCounter oceanCounter = ConvertUtil.convert(oceanCounterForm, OceanCounter.class);
//                oceanCounter.setObId(obId);
//                oceanCounter.setStatus("1");//状态(0无效 1有效)
//                oceanCounter.setCreateTime(LocalDateTime.now());
//                oceanCounterList.add(oceanCounter);
//            });
//            //2.保存提单对应的柜子
//            QueryWrapper<OceanCounter> oceanCounterQueryWrapper = new QueryWrapper<>();
//            oceanCounterQueryWrapper.eq("ob_id", obId);
//            oceanCounterService.remove(oceanCounterQueryWrapper);
//            oceanCounterService.saveOrUpdateBatch(oceanCounterList);
//        }

        OceanBillVO oceanBillVO = ConvertUtil.convert(oceanBill, OceanBillVO.class);

        //3.保存提单关联任务
        List<BillTaskRelevanceVO> billTaskRelevanceVOS = billTaskRelevanceService.savebillTaskRelevance(oceanBill);

        //4.保存提单到配载 ocean_conf_detail
        OceanConfDetail oceanConfDetail = new OceanConfDetail();
        oceanConfDetail.setOrderId(orderConfId);//配载id
        oceanConfDetail.setIdCode(obId.intValue());//oceanBill.getId();//提单id
        oceanConfDetail.setTypes(2);//类型1报价 2提单
        oceanConfDetail.setStatus("1");//状态(0无效 1有效)

        QueryWrapper<OceanConfDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderConfId);//配载id
        queryWrapper.eq("id_code", obId);//提单id
        queryWrapper.eq("types", 2);//分类区分当前是报价或提单(1报价 2提单)
        oceanConfDetailService.remove(queryWrapper);
        oceanConfDetailService.saveOrUpdate(oceanConfDetail);

        return CommonResult.success(oceanBillVO);
    }

    @Override
    public CommonResult<OceanBillVO> findOceanBillById(Long id) {
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(id);
        return CommonResult.success(oceanBillVO);
    }

    @Override
    public List<BillClearanceInfoVO> findBillClearanceInfoByBillId(Long billId) {
        List<BillClearanceInfoVO> billClearanceInfoVOS = oceanBillMapper.findBillClearanceInfoByBillId(billId);
        billClearanceInfoVOS.forEach(billClearanceInfoVO -> {
            Long b_id = billClearanceInfoVO.getId();
            //统计箱数
            Integer total = billClearanceInfoMapper.findClearanceInfoCaseTotalBybid(b_id);
            billClearanceInfoVO.setCartons(total);
            //文件展示
            String templateUrl = billClearanceInfoVO.getTemplateUrl();
            if(StrUtil.isNotEmpty(templateUrl)){
                try {
                    List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                    billClearanceInfoVO.setTemplateUrls(templateUrls);

                    String showTemplateUrl = "";
                    for (int i=0; i<templateUrls.size(); i++){
                        TemplateUrlVO templateUrlVO = templateUrls.get(i);
                        String fileName = templateUrlVO.getFileName();
                        if(i==0){
                            showTemplateUrl = fileName;
                        }else{
                            showTemplateUrl += "," + fileName;
                        }
                    }
                    billClearanceInfoVO.setTemplateUrl(showTemplateUrl);

                } catch (Exception e) {
                    billClearanceInfoVO.setTemplateUrls(new ArrayList<>());
                }
            }else{
                billClearanceInfoVO.setTemplateUrls(new ArrayList<>());
            }

        });




        return billClearanceInfoVOS;
    }

    @Override
    public List<BillCustomsInfoVO> findBillCustomsInfoByBillId(Long billId) {
        List<BillCustomsInfoVO> billCustomsInfoVOS = oceanBillMapper.findBillCustomsInfoByBillId(billId);
        billCustomsInfoVOS.forEach(billCustomsInfoVO -> {
            Long b_id = billCustomsInfoVO.getId();
            //统计箱数
            Integer total = billCustomsInfoMapper.findCustomsInfoCaseTotalBybid(b_id);
            billCustomsInfoVO.setCartons(total);
            //文件展示
            String templateUrl = billCustomsInfoVO.getTemplateUrl();
            if(StrUtil.isNotEmpty(templateUrl)){
                try {
                    List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                    billCustomsInfoVO.setTemplateUrls(templateUrls);

                    String showTemplateUrl = "";
                    for (int i=0; i<templateUrls.size(); i++){
                        TemplateUrlVO templateUrlVO = templateUrls.get(i);
                        String fileName = templateUrlVO.getFileName();
                        if(i==0){
                            showTemplateUrl = fileName;
                        }else{
                            showTemplateUrl += "," + fileName;
                        }
                    }
                    billCustomsInfoVO.setTemplateUrl(showTemplateUrl);

                } catch (Exception e) {
                    billCustomsInfoVO.setTemplateUrls(new ArrayList<>());
                }
            }else{
                billCustomsInfoVO.setTemplateUrls(new ArrayList<>());
            }

        });
        return billCustomsInfoVOS;
    }

    @Override
    public List<OceanCounterVO> findOceanCounterByObId(Long obId) {
        List<OceanCounterVO> oceanCounterVOS = oceanBillMapper.findOceanCounterByObId(obId);
        return oceanCounterVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BillClearanceInfoVO saveBillClearanceInfo(BillClearanceInfoForm form) {
        BillClearanceInfo billClearanceInfo = ConvertUtil.convert(form, BillClearanceInfo.class);
        Integer billId = billClearanceInfo.getBillId();
        if(ObjectUtil.isEmpty(billId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单id不能为空");
        }
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(Long.valueOf(billId));
        if(ObjectUtil.isEmpty(oceanBillVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单不存在");
        }
        String billNo = oceanBillVO.getOrderId();//提单号(ocean_bill order_id)

        List<TemplateUrlVO> templateUrls = form.getTemplateUrls();
        String s = JSONUtil.toJsonStr(templateUrls);
        billClearanceInfo.setTemplateUrl(s);
        billClearanceInfo.setBillNo(billNo);
        //1.保存-(提单)清关信息表
        billClearanceInfoService.saveOrUpdate(billClearanceInfo);
        Long b_id = billClearanceInfo.getId();//提单对应清关信息id(bill_clearance_info id)

        //2.保存-提单对应清关箱号信息
        List<ClearanceInfoCaseForm> clearanceInfoCaseForms = form.getClearanceInfoCases();
        List<ClearanceInfoCase> clearanceInfoCases = ConvertUtil.convertList(clearanceInfoCaseForms, ClearanceInfoCase.class);
        QueryWrapper<ClearanceInfoCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", b_id);
        clearanceInfoCaseService.remove(queryWrapper);
        if(CollUtil.isNotEmpty(clearanceInfoCases)){
            clearanceInfoCases.forEach(clearanceInfoCase -> {
                clearanceInfoCase.setBId(b_id);//提单对应清关信息id(bill_clearance_info id)
                clearanceInfoCase.setBName(billClearanceInfo.getFileName());
                clearanceInfoCase.setBillId(billId);
                clearanceInfoCase.setBillNo(billNo);
            });
            clearanceInfoCaseService.saveOrUpdateBatch(clearanceInfoCases);
        }
        BillClearanceInfoVO billClearanceInfoVO = ConvertUtil.convert(billClearanceInfo, BillClearanceInfoVO.class);
        return billClearanceInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BillCustomsInfoVO saveBillCustomsInfo(BillCustomsInfoForm form) {
        BillCustomsInfo billCustomsInfo = ConvertUtil.convert(form, BillCustomsInfo.class);
        Integer billId = billCustomsInfo.getBillId();//提单id(ocean_bill id)
        if(ObjectUtil.isEmpty(billId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单id不能为空");
        }
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(Long.valueOf(billId));
        if(ObjectUtil.isEmpty(oceanBillVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单不存在");
        }
        String billNo = oceanBillVO.getOrderId();//提单号(ocean_bill order_id)

        List<TemplateUrlVO> templateUrls = form.getTemplateUrls();
        String s = JSONUtil.toJsonStr(templateUrls);
        billCustomsInfo.setTemplateUrl(s);
        billCustomsInfo.setBillNo(billNo);
        //1.保存-(提单)报关信息表
        billCustomsInfoService.saveOrUpdate(billCustomsInfo);
        Long b_id = billCustomsInfo.getId();//提单对应报关信息id(bill_customs_info id)

        //2.保存-提单对应报关箱号信息
        QueryWrapper<CustomsInfoCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", b_id);
        customsInfoCaseService.remove(queryWrapper);
        List<CustomsInfoCaseForm> customsInfoCaseForms = form.getCustomsInfoCases();
        if(CollUtil.isNotEmpty(customsInfoCaseForms)){
            List<CustomsInfoCase> customsInfoCases = ConvertUtil.convertList(customsInfoCaseForms, CustomsInfoCase.class);
            customsInfoCases.forEach(customsInfoCase -> {
                customsInfoCase.setBId(b_id);//提单对应报关信息id(bill_customs_info id)
                customsInfoCase.setBName(billCustomsInfo.getFileName());
                customsInfoCase.setBillId(billId);
                customsInfoCase.setBillNo(billNo);
            });
            customsInfoCaseService.saveOrUpdateBatch(customsInfoCases);
        }
        BillCustomsInfoVO billCustomsInfoVO = ConvertUtil.convert(billCustomsInfo, BillCustomsInfoVO.class);
        return billCustomsInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OceanCounterVO saveOceanCounter(OceanCounterForm form) {
        Long obId = form.getObId();//提单id(ocean_bill id)
        if(ObjectUtil.isEmpty(obId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单id不能为空");
        }
        OceanCounter oceanCounter = ConvertUtil.convert(form, OceanCounter.class);
        //1.保存(提单)柜子
        oceanCounterService.saveOrUpdate(oceanCounter);
        Long counterId = oceanCounter.getId();
        OceanCounterVO oceanCounterVO = ConvertUtil.convert(oceanCounter, OceanCounterVO.class);

        //2.保存(提单)柜子对应-费用信息
        List<FeeCopeWithForm> feeCopeWithList = form.getFeeCopeWithList();
        //先刪除
        QueryWrapper<FeeCopeWith> qwFeeCopeWith = new QueryWrapper<>();
        qwFeeCopeWith.eq("qie", counterId);
        qwFeeCopeWith.eq("business_type", 2);//业务类型(1提单费用 2柜子费用)
        if(CollUtil.isNotEmpty(feeCopeWithList)){
            List<FeeCopeWith> feeCopeWiths = ConvertUtil.convertList(feeCopeWithList, FeeCopeWith.class);
            feeCopeWiths.forEach(feeCopeWith -> {
                feeCopeWith.setQie(counterId.intValue());//提单id(ocean_bill id)/柜子id(ocean_counter id) 这里是柜子id
                feeCopeWith.setBusinessType(2);//业务类型(1提单费用 2柜子费用)
                //计算 总金额=数量 * 单价
                Integer c = feeCopeWith.getCount() == null ? 0 : feeCopeWith.getCount();//数量
                BigDecimal count = new BigDecimal(c.toString());
                BigDecimal unitPrice = feeCopeWith.getUnitPrice() == null ? new BigDecimal("0") : feeCopeWith.getUnitPrice();//单价
                BigDecimal amount = count.multiply(unitPrice);
                feeCopeWith.setAmount(amount);
            });
            //在保存
            feeCopeWithService.saveOrUpdateBatch(feeCopeWiths);
        }
        return oceanCounterVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBillClearanceInfo(BillClearanceInfoIdForm form) {
        Long id = form.getId();
        //1.删除-(提单)清关信息表
        billClearanceInfoService.removeById(id);
        //2.删除-提单对应清关箱号信息
        QueryWrapper<ClearanceInfoCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", id);
        clearanceInfoCaseService.remove(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBillCustomsInfo(BillCustomsInfoIdForm form) {
        Long id = form.getId();
        //1.删除-(提单)报关信息表
        billCustomsInfoService.removeById(id);
        //2.删除-提单对应报关箱号信息
        QueryWrapper<CustomsInfoCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", id);
        customsInfoCaseService.remove(queryWrapper);
    }

    /**
     *
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delOceanCounter(OceanCounterIdForm form) {
        Long counterId = form.getId();
        //1.查询柜子下，所有的，柜子箱号信息 id
        List<Long> counterCaseInfoIds = oceanCounterMapper.findCounterCaseInfoIdByCounterId(counterId);
        //2.查询柜子下，所有的，柜子清单信息 id
        List<Long> counterListInfoIds = oceanCounterMapper.findCounterListInfoIdByCounterId(counterId);
        //3.查询柜子 id
        Long id = oceanCounterMapper.findOceanCounterIdById(counterId);

        //1.删除-柜子箱号信息
        counterCaseInfoService.removeByIds(counterCaseInfoIds);
        //2.删除-柜子清单信息表
        counterListInfoService.removeByIds(counterListInfoIds);
        //3.删除-柜子
        oceanCounterService.removeById(id);
    }

    @Override
    public IPage<ConfCaseVO> findConfCaseByPage(ConfCaseForm form) {
        Long billId = form.getBillId();
        OrderConfVO orderConfVO = oceanBillMapper.findOrderConfByBillId(billId);
        form.setConfId(orderConfVO.getId());

        //定义分页参数
        Page<ConfCaseVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<ConfCaseVO> pageInfo = oceanBillMapper.findConfCaseByPage(page, form);
        return pageInfo;
    }

    @Override
    public BillClearanceInfoVO findBillClearanceInfoById(Long id) {
        //1.(提单)清关信息表
        BillClearanceInfoVO billClearanceInfoVO = billClearanceInfoService.findBillClearanceInfoById(id);
        String templateUrl = billClearanceInfoVO.getTemplateUrl();
        if(StrUtil.isNotEmpty(templateUrl)){
            try {
                List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                billClearanceInfoVO.setTemplateUrls(templateUrls);
            } catch (Exception e) {
                billClearanceInfoVO.setTemplateUrls(new ArrayList<>());
            }
        }else{
            billClearanceInfoVO.setTemplateUrls(new ArrayList<>());
        }
        Long b_id = billClearanceInfoVO.getId();//提单对应清关信息id(bill_clearance_info id)
        //2.提单对应清关箱号信息
        List<ClearanceInfoCaseVO> clearanceInfoCaseVOS =  billClearanceInfoService.findClearanceInfoCase(b_id);
        billClearanceInfoVO.setClearanceInfoCases(clearanceInfoCaseVOS);
        return billClearanceInfoVO;
    }

    @Override
    public BillCustomsInfoVO findBillCustomsInfoById(Long id) {
        //1.(提单)报关信息表
        BillCustomsInfoVO billCustomsInfoVO = billCustomsInfoService.findBillCustomsInfoById(id);
        String templateUrl = billCustomsInfoVO.getTemplateUrl();
        if(StrUtil.isNotEmpty(templateUrl)){
            try {
                List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                billCustomsInfoVO.setTemplateUrls(templateUrls);
            } catch (Exception e) {
                billCustomsInfoVO.setTemplateUrls(new ArrayList<>());
            }
        }else{
            billCustomsInfoVO.setTemplateUrls(new ArrayList<>());
        }
        Long b_id = billCustomsInfoVO.getId();
        //2.提单对应报关箱号信息
        List<CustomsInfoCaseVO> customsInfoCaseVOS = billCustomsInfoService.findCustomsInfoCase(b_id);
        billCustomsInfoVO.setCustomsInfoCases(customsInfoCaseVOS);
        return billCustomsInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CounterListInfoVO saveCounterListInfo(CounterListInfoForm form) {
        CounterListInfo counterListInfo = ConvertUtil.convert(form, CounterListInfo.class);
        Long counterId = counterListInfo.getCounterId();//柜子id(ocean_counter id)
        if(ObjectUtil.isEmpty(counterId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子id不能为空");
        }
        OceanCounter oceanCounter = oceanCounterService.getById(counterId);
        if(ObjectUtil.isEmpty(oceanCounter)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子不存在");
        }
        Long billId = oceanCounter.getObId();//提单id
        String cntrNo = oceanCounter.getCntrNo();//柜号(ocean_counter cntr_no)
        OceanBillVO oceanBillVO = oceanBillMapper.findOceanBillById(Long.valueOf(billId));
        if(ObjectUtil.isEmpty(oceanBillVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提单不存在");
        }
        String billNo = oceanBillVO.getOrderId();//提单号(ocean_bill order_id)

        List<TemplateUrlVO> templateUrls = form.getTemplateUrls();
        String s = JSONUtil.toJsonStr(templateUrls);
        counterListInfo.setTemplateUrl(s);
        counterListInfo.setCntrNo(cntrNo);
        //1.保存-柜子清单信息表
        counterListInfoService.saveOrUpdate(counterListInfo);
        Long b_id = counterListInfo.getId();//柜子清单信息表(counter_list_info id)
        //2.保存-柜子箱号信息
        List<CounterCaseInfoForm> counterCaseInfoForms = form.getCounterCaseInfos();
        List<CounterCaseInfo> counterCaseInfos = ConvertUtil.convertList(counterCaseInfoForms, CounterCaseInfo.class);
        QueryWrapper<CounterCaseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", b_id);
        counterCaseInfoService.remove(queryWrapper);
        if(CollUtil.isNotEmpty(counterCaseInfos)){
            counterCaseInfos.forEach(counterCaseInfo -> {
                counterCaseInfo.setBId(b_id);//提单对应清关信息id(bill_clearance_info id)
                counterCaseInfo.setBName(counterListInfo.getFileName());
                counterCaseInfo.setBillId(billId.intValue());
                counterCaseInfo.setBillNo(billNo);
            });
            counterCaseInfoService.saveOrUpdateBatch(counterCaseInfos);
        }
        //3.保存-提单关联订单(任务通知表)
        /**
         * 1).根据关系查询：提单关联运单信息  （这里是最新最全的数据）
         * 提单 -->  提单的柜子  -->  柜子清单  -->  柜子的箱子  --> 箱子的订单  --> 订单(运单)
         */
        List<BillOrderRelevance> billOrderList = oceanBillMapper.findBillOrderByBillId(billId);
        /**
         * 2).根据提单id，查询 提单关联订单(任务通知表)  （这里是数据库保存的历史数据）
         */
        List<BillOrderRelevance> billOrderRelevanceList = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
        /**
         * 3).billOrderList 和 billOrderRelevanceList 进行合并，找出删除的数据
         */
        List<Integer> delIds = new ArrayList<>();//要删除的ids
        List<Integer> notDelIds = new ArrayList<>();//不删除的ids
        List<BillOrderRelevance> billOrderRelevances = new ArrayList<>();//要保存的数据
        if(CollUtil.isNotEmpty(billOrderList)){
            for (int i=0; i<billOrderList.size(); i++) {
                BillOrderRelevance billOrder = billOrderList.get(i);
                Integer new_billId = billOrder.getBillId();
                Long new_orderId = billOrder.getOrderId();
                for(int j=0; j<billOrderRelevanceList.size(); j++){
                    BillOrderRelevance billOrderRelevance = billOrderRelevanceList.get(j);
                    Integer db_billId = billOrderRelevance.getBillId();
                    Long db_orderId = billOrderRelevance.getOrderId();
                    if(new_billId.equals(db_billId) && new_orderId.equals(db_orderId) ){
                        billOrder.setId(billOrderRelevance.getId());//主键id 更新数据id
                        billOrder.setIsInform(billOrderRelevance.getIsInform());
                        billOrder.setCreateTime(billOrderRelevance.getCreateTime());
                        notDelIds.add(billOrderRelevance.getId());
                    }
                }
                billOrderRelevances.add(billOrder);
            }
        }else{
            billOrderRelevanceList.forEach(billOrderRelevance -> {
                delIds.add(billOrderRelevance.getId());
            });
        }
        if(CollUtil.isNotEmpty(delIds)){
            //要删除的ids
            billOrderRelevanceService.removeByIds(delIds);
        }
        if(CollUtil.isNotEmpty(notDelIds)){
            //删除其他的ids，过滤掉 不删除的ids
            QueryWrapper<BillOrderRelevance> qw = new QueryWrapper<>();
            qw.notIn("id", notDelIds);
            qw.eq("bill_id", billId);
            billOrderRelevanceService.remove(qw);
        }
        if(CollUtil.isNotEmpty(billOrderRelevances)){
            //保存的数据
            billOrderRelevanceService.saveOrUpdateBatch(billOrderRelevances);
        }
        CounterListInfoVO counterListInfoVO = ConvertUtil.convert(counterListInfo, CounterListInfoVO.class);
        return counterListInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delCounterListInfo(CounterListInfoIdForm form) {
        Long id = form.getId();
        CounterListInfoVO counterListInfoVO = counterListInfoMapper.findCounterListInfoById(id);
        Long billId = counterListInfoVO.getBillId();
        //1.删除-柜子清单信息表
        counterListInfoService.removeById(id);
        //2.删除-柜子箱号信息
        QueryWrapper<CounterCaseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b_id", id);
        counterCaseInfoService.remove(queryWrapper);

        //3.保存-提单关联订单(任务通知表)
        /**
         * 1).根据关系查询：提单关联运单信息  （这里是最新最全的数据）
         * 提单 -->  提单的柜子  -->  柜子清单  -->  柜子的箱子  --> 箱子的订单  --> 订单(运单)
         */
        List<BillOrderRelevance> billOrderList = oceanBillMapper.findBillOrderByBillId(billId);
        /**
         * 2).根据提单id，查询 提单关联订单(任务通知表)  （这里是数据库保存的历史数据）
         */
        List<BillOrderRelevance> billOrderRelevanceList = billOrderRelevanceMapper.findBillOrderRelevanceByBillId(billId);
        /**
         * 3).billOrderList 和 billOrderRelevanceList 进行合并，找出删除的数据
         */
        List<Integer> delIds = new ArrayList<>();//要删除的ids
        List<Integer> notDelIds = new ArrayList<>();//不删除的ids
        List<BillOrderRelevance> billOrderRelevances = new ArrayList<>();//要保存的数据
        if(CollUtil.isNotEmpty(billOrderList)){
            for (int i=0; i<billOrderList.size(); i++) {
                BillOrderRelevance billOrder = billOrderList.get(i);
                Integer new_billId = billOrder.getBillId();
                Long new_orderId = billOrder.getOrderId();
                for(int j=0; j<billOrderRelevanceList.size(); j++){
                    BillOrderRelevance billOrderRelevance = billOrderRelevanceList.get(j);
                    Integer db_billId = billOrderRelevance.getBillId();
                    Long db_orderId = billOrderRelevance.getOrderId();
                    if(new_billId.equals(db_billId) && new_orderId.equals(db_orderId) ){
                        billOrder.setId(billOrderRelevance.getId());//主键id 更新数据id
                        billOrder.setIsInform(billOrderRelevance.getIsInform());
                        billOrder.setCreateTime(billOrderRelevance.getCreateTime());
                        notDelIds.add(billOrderRelevance.getId());
                    }
                }
                billOrderRelevances.add(billOrder);
            }
        }else{
            billOrderRelevanceList.forEach(billOrderRelevance -> {
                delIds.add(billOrderRelevance.getId());
            });
        }
        if(CollUtil.isNotEmpty(delIds)){
            //要删除的ids
            billOrderRelevanceService.removeByIds(delIds);
        }
        if(CollUtil.isNotEmpty(notDelIds)){
            //删除其他的ids，过滤掉 不删除的ids
            QueryWrapper<BillOrderRelevance> qw = new QueryWrapper<>();
            qw.notIn("id", notDelIds);
            qw.eq("bill_id", billId);
            billOrderRelevanceService.remove(qw);
        }
        if(CollUtil.isNotEmpty(billOrderRelevances)){
            //保存的数据
            billOrderRelevanceService.saveOrUpdateBatch(billOrderRelevances);
        }

    }

    @Override
    public CounterListInfoVO findCounterListInfoById(Long id) {
        //1.柜子清单信息表
        CounterListInfoVO counterListInfoVO = counterListInfoService.findCounterListInfoById(id);
        if(ObjectUtil.isEmpty(counterListInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "柜子清单不存在");
        }
        String templateUrl = counterListInfoVO.getTemplateUrl();
        if(StrUtil.isNotEmpty(templateUrl)){
            try {
                List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                counterListInfoVO.setTemplateUrls(templateUrls);
            } catch (Exception e) {
                counterListInfoVO.setTemplateUrls(new ArrayList<>());
            }
        }else{
            counterListInfoVO.setTemplateUrls(new ArrayList<>());
        }

        Long b_id = counterListInfoVO.getId();//柜子清单信息表(counter_list_info id)
        //2.柜子箱号信息
        List<CounterCaseInfoVO> counterCaseInfoVOS = counterListInfoService.findCounterCaseInfo(b_id);
        counterListInfoVO.setCounterCaseInfos(counterCaseInfoVOS);
        return counterListInfoVO;
    }

    @Override
    public List<CounterListInfoVO> findCounterListInfoByCounterId(Long counterId) {
        List<CounterListInfoVO> counterListInfoVOS = counterListInfoMapper.findCounterListInfoByCounterId(counterId);
        counterListInfoVOS.forEach(counterListInfoVO -> {
            Long b_id = counterListInfoVO.getId();//b_id 柜子清单信息表(counter_list_info id)
            //统计箱数
            Integer total = counterListInfoMapper.findCounterCaseInfoTotalBybid(b_id);
            counterListInfoVO.setCartons(total);
            //文件展示
            String templateUrl = counterListInfoVO.getTemplateUrl();
            if(StrUtil.isNotEmpty(templateUrl)){
                try {
                    List<TemplateUrlVO> templateUrls = JSON.parseObject(templateUrl, new TypeReference<List<TemplateUrlVO>>() {});
                    counterListInfoVO.setTemplateUrls(templateUrls);

                    String showTemplateUrl = "";
                    for (int i=0; i<templateUrls.size(); i++){
                        TemplateUrlVO templateUrlVO = templateUrls.get(i);
                        String fileName = templateUrlVO.getFileName();
                        if(i==0){
                            showTemplateUrl = fileName;
                        }else{
                            showTemplateUrl += "," + fileName;
                        }
                    }
                    counterListInfoVO.setTemplateUrl(showTemplateUrl);

                } catch (Exception e) {
                    counterListInfoVO.setTemplateUrls(new ArrayList<>());
                }
            }else{
                counterListInfoVO.setTemplateUrls(new ArrayList<>());
            }
        });
        return counterListInfoVOS;
    }
}
