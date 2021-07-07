package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.CurrencyInfoMapper;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.PayBillDetailMapper;
import com.jayud.mall.mapper.PayBillMasterMapper;
import com.jayud.mall.model.bo.PayBillForm;
import com.jayud.mall.model.bo.PayBillMasterForm;
import com.jayud.mall.model.bo.QueryPayBillMasterForm;
import com.jayud.mall.model.po.OrderCopeWith;
import com.jayud.mall.model.po.PayBillDetail;
import com.jayud.mall.model.po.PayBillMaster;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderCopeWithService;
import com.jayud.mall.service.IPayBillDetailService;
import com.jayud.mall.service.IPayBillMasterService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 应付账单主单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Service
public class PayBillMasterServiceImpl extends ServiceImpl<PayBillMasterMapper, PayBillMaster> implements IPayBillMasterService {

    @Autowired
    PayBillMasterMapper payBillMasterMapper;
    @Autowired
    PayBillDetailMapper payBillDetailMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;
    @Autowired
    IPayBillDetailService payBillDetailService;
    @Autowired
    IOrderCopeWithService orderCopeWithService;


    @Override
    public CommonResult<PayBillMasterVO> createPayBill(PayBillForm form) {

        PayBillMasterVO payBillMasterVO = new PayBillMasterVO();
        String billCode = NumberGeneratedUtils.getOrderNoByCode2("pay_bill_code");//账单编号（应付账单编号）
        payBillMasterVO.setBillCode(billCode);//账单编号
        Long orderId = form.getId();
        payBillMasterVO.setOrderId(orderId);//订单id

        //供应商名称
        List<OrderCopeWithVO> orderCopeWithVOS = form.getOrderCopeWithVOS();
        Set<Map<Integer, String>> set = new HashSet<>();
        orderCopeWithVOS.forEach(orderCopeWithVO -> {
            Map<Integer, String> map = new HashMap<>();
            Integer supplierId = orderCopeWithVO.getSupplierId();
            String supplierName = orderCopeWithVO.getSupplierName();
            map.put(supplierId, supplierName);
            set.add(map);
        });
        if(set.size() > 1){
            return CommonResult.error(-1, "生成应付对账单的供应商必须相同!");
        }
        Iterator ite = set.iterator();
        while(ite.hasNext()){
            Object obj = ite.next();
            Map<Integer, String> map = (Map<Integer, String>) obj;
            Iterator<Integer> iter = map.keySet().iterator();
            while(iter.hasNext()){
                Integer key = iter.next();
                String value = map.get(key);
                payBillMasterVO.setSupplierId(key);//供应商id(supplier_info id)
                payBillMasterVO.setSupplierName(value);//供应商名称(supplier_info company_name)
            }
        }
        BigDecimal amount = new BigDecimal("0");
        List<PayBillDetailVO> payBillDetailVOS = new ArrayList<>();
        for(int i=0; i<orderCopeWithVOS.size(); i++){
            OrderCopeWithVO orderCopeReceivableVO = orderCopeWithVOS.get(i);
            Long orderCopeReceivableId = orderCopeReceivableVO.getId();
            BigDecimal amount1 = orderCopeReceivableVO.getAmount();
            amount = amount.add(amount1);

            PayBillDetailVO payBillDetailVO = ConvertUtil.convert(orderCopeReceivableVO, PayBillDetailVO.class);
            payBillDetailVO.setId(null);
            payBillDetailVO.setBillMasterId(null);
            payBillDetailVO.setOrderPayId(orderCopeReceivableId);
            payBillDetailVOS.add(payBillDetailVO);
        }

        payBillMasterVO.setAmount(amount);//金额
        Integer cid = orderCopeWithVOS.get(0).getCid();
        payBillMasterVO.setCid(cid);//币种(currency_info id)
        String currencyName = orderCopeWithVOS.get(0).getCurrencyName();//币种名称
        String amountFormat = amount+" "+currencyName;
        payBillMasterVO.setAmountFormat(amountFormat);
        payBillMasterVO.setPayBillDetailVOS(payBillDetailVOS);//应收账单明细list
        return CommonResult.success(payBillMasterVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<PayBillMasterVO> affirmPayBill(PayBillMasterForm form) {
        //1.保存应付账单主单
        PayBillMaster payBillMaster = ConvertUtil.convert(form, PayBillMaster.class);
        this.saveOrUpdate(payBillMaster);

        Long billMasterId = payBillMaster.getId();

        List<PayBillDetailVO> payBillDetailVOS = form.getPayBillDetailVOS();
        List<PayBillDetail> payBillDetails = ConvertUtil.convertList(payBillDetailVOS, PayBillDetail.class);
        List<Long> orderPayIds = new ArrayList<>();
        payBillDetails.forEach(payBillDetail -> {
            payBillDetail.setBillMasterId(billMasterId);
            Long orderPayId = payBillDetail.getOrderPayId();
            orderPayIds.add(orderPayId);
        });
        //2.保存应付账单明细
        QueryWrapper<PayBillDetail> payBillDetailQueryWrapper = new QueryWrapper<>();
        payBillDetailQueryWrapper.eq("bill_master_id", billMasterId);
        payBillDetailQueryWrapper.in("order_pay_id", orderPayIds);
        payBillDetailService.remove(payBillDetailQueryWrapper);
        payBillDetailService.saveOrUpdateBatch(payBillDetails);
        //3.修改反写订单应付明细状态
        QueryWrapper<OrderCopeWith> orderCopeWithQueryWrapper = new QueryWrapper<>();
        orderCopeWithQueryWrapper.in("id", orderPayIds);
        List<OrderCopeWith> orderCopeWiths = orderCopeWithService.list(orderCopeWithQueryWrapper);
        orderCopeWiths.forEach(orderCopeWith -> {
            orderCopeWith.setStatus(1);//状态(0未生成账单 1已生成账单)
        });
        orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);

        PayBillMasterVO payBillMasterVO = ConvertUtil.convert(payBillMaster, PayBillMasterVO.class);
        List<PayBillDetailVO> payBillDetailVOList = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
        payBillMasterVO.setPayBillDetailVOS(payBillDetailVOList);

        return CommonResult.success(payBillMasterVO);
    }

    @Override
    public IPage<PayBillMasterVO> findPayBillMasterByPage(QueryPayBillMasterForm form) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        //定义分页参数
        Page<PayBillMasterVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<PayBillMasterVO> pageInfo = payBillMasterMapper.findPayBillMasterByPage(page, form);
        List<PayBillMasterVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(payBillMasterVO -> {
                Long billMasterId = payBillMasterVO.getId();
                List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
                List<String> billAmount = new ArrayList<>();//账单金额(bill_amount)
                List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
                //根据 币种id，分组
                Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);
                for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
                    String cid = entry.getKey();//币种id
                    List<PayBillDetailVO> payBillDetailVOList = entry.getValue();
                    BigDecimal amountSum = new BigDecimal("0");
                    for (int i=0; i<payBillDetailVOList.size(); i++){
                        PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(i);
                        BigDecimal amount = payBillDetailVO.getAmount();
                        amountSum = amountSum.add(amount);
                    }
                    AmountVO amountVO = new AmountVO();
                    amountVO.setAmount(amountSum);//金额
                    amountVO.setCid(Integer.valueOf(cid));
                    amountVOS.add(amountVO);
                }
                payBillMasterVO.setAmountVOS(amountVOS);

                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    String amountFormat = amount.toString() + " " + cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    billAmount.add(amountFormat);
                });
                payBillMasterVO.setBillAmount(billAmount);//账单金额(bill_amount)
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<PayBillMasterVO> lookDetail(Long id) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        PayBillMasterVO payBillMasterVO = payBillMasterMapper.findPayBillMasterById(id);
        if(payBillMasterVO == null){
            return CommonResult.error(-1, "没有找到应付账单");
        }
        //List<PayBillDetailVO> payBillDetailVOList = payBillDetailMapper.findPayBillDetailByBillMasterId(id);

        Long billMasterId = payBillMasterVO.getId();
        List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
        List<String> billAmount = new ArrayList<>();//账单金额(bill_amount)
        List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
        //根据 币种id，分组
        Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);
        for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
            String cid = entry.getKey();//币种id
            List<PayBillDetailVO> payBillDetailVOList = entry.getValue();
            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<payBillDetailVOList.size(); i++){
                PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(i);
                BigDecimal amount = payBillDetailVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            AmountVO amountVO = new AmountVO();
            amountVO.setAmount(amountSum);//金额
            amountVO.setCid(Integer.valueOf(cid));
            amountVOS.add(amountVO);
        }
        payBillMasterVO.setAmountVOS(amountVOS);

        amountVOS.forEach(amountVO -> {
            Integer cid = amountVO.getCid();
            BigDecimal amount = amountVO.getAmount();
            String amountFormat = amount.toString() + " " + cidMap.get(Long.valueOf(cid)).getCurrencyName();
            billAmount.add(amountFormat);
        });
        payBillMasterVO.setBillAmount(billAmount);//账单金额(bill_amount)
        payBillMasterVO.setPayBillDetailVOS(payBillDetailVOS);
        return CommonResult.success(payBillMasterVO);
    }

    @Override
    public List<PayBillMasterVO> findPayBillMasterByOrderId(Long orderId) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        List<PayBillMasterVO> payBillMasterList = payBillMasterMapper.findPayBillMasterByOrderId(orderId);
        if(CollUtil.isNotEmpty(payBillMasterList)){
            payBillMasterList.forEach(payBillMasterVO -> {
                Long billMasterId = payBillMasterVO.getId();
                List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
                List<String> billAmount = new ArrayList<>();//账单金额(bill_amount)
                List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
                //根据 币种id，分组
                Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);
                for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
                    String cid = entry.getKey();//币种id
                    List<PayBillDetailVO> payBillDetailVOList = entry.getValue();
                    BigDecimal amountSum = new BigDecimal("0");
                    for (int i=0; i<payBillDetailVOList.size(); i++){
                        PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(i);
                        BigDecimal amount = payBillDetailVO.getAmount();
                        amountSum = amountSum.add(amount);
                    }
                    AmountVO amountVO = new AmountVO();
                    amountVO.setAmount(amountSum);//金额
                    amountVO.setCid(Integer.valueOf(cid));
                    amountVOS.add(amountVO);
                }
                payBillMasterVO.setAmountVOS(amountVOS);

                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    String amountFormat = amount.toString() + " " + cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    billAmount.add(amountFormat);
                });
                payBillMasterVO.setBillAmount(billAmount);//账单金额(bill_amount)
            });
        }
        return payBillMasterList;
    }

    /**
     * 根据币种id，分组
     * @param payBillDetailVOS 应付费用明细
     * @return
     */
    private Map<String, List<PayBillDetailVO>> groupListByCid(List<PayBillDetailVO> payBillDetailVOS) {
        Map<String, List<PayBillDetailVO>> map = new HashMap<>();
        for (PayBillDetailVO payBillDetailVO : payBillDetailVOS) {
            String key = payBillDetailVO.getCid().toString();//币种id
            List<PayBillDetailVO> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(payBillDetailVO);
                map.put(key, tmpList);
            } else {
                tmpList.add(payBillDetailVO);
            }
        }
        return map;
    }

}
