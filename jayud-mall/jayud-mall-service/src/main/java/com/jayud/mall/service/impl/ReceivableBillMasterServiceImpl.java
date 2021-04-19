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
import com.jayud.mall.mapper.ReceivableBillDetailMapper;
import com.jayud.mall.mapper.ReceivableBillMasterMapper;
import com.jayud.mall.model.bo.QueryReceivableBillMasterForm;
import com.jayud.mall.model.bo.ReceivableBillForm;
import com.jayud.mall.model.bo.ReceivableBillMasterForm;
import com.jayud.mall.model.po.OrderCopeReceivable;
import com.jayud.mall.model.po.ReceivableBillDetail;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderCopeReceivableService;
import com.jayud.mall.service.IReceivableBillDetailService;
import com.jayud.mall.service.IReceivableBillMasterService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 应收账单主单 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Service
public class ReceivableBillMasterServiceImpl extends ServiceImpl<ReceivableBillMasterMapper, ReceivableBillMaster> implements IReceivableBillMasterService {

    @Autowired
    ReceivableBillMasterMapper receivableBillMasterMapper;
    @Autowired
    ReceivableBillDetailMapper receivableBillDetailMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;
    @Autowired
    IReceivableBillDetailService receivableBillDetailService;
    @Autowired
    IOrderCopeReceivableService orderCopeReceivableService;

    @Override
    public CommonResult<ReceivableBillMasterVO> createReceivableBill(ReceivableBillForm form) {
        ReceivableBillMasterVO receivableBillMasterVO = new ReceivableBillMasterVO();
        String billCode = NumberGeneratedUtils.getOrderNoByCode2("rec_bill_code");//账单编号（应付账单编号）
        receivableBillMasterVO.setBillCode(billCode);//账单编号
        Long orderId = form.getId();
        receivableBillMasterVO.setOrderId(orderId);//订单id
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        Integer customerId = orderInfoVO.getCustomerId();//客戶id
        String company = orderInfoVO.getCompany();//客户名称（公司名称）
        receivableBillMasterVO.setCustomerId(customerId);//客戶id
        receivableBillMasterVO.setCustomerName(company);//客户名称（公司名称）
        List<OrderCopeReceivableVO> orderCopeReceivableVOS = form.getOrderCopeReceivableVOS();
        BigDecimal amount = new BigDecimal("0");
        List<ReceivableBillDetailVO> receivableBillDetailVOS = new ArrayList<>();
        for(int i=0; i<orderCopeReceivableVOS.size(); i++){
            OrderCopeReceivableVO orderCopeReceivableVO = orderCopeReceivableVOS.get(i);
            Long orderCopeReceivableId = orderCopeReceivableVO.getId();
            BigDecimal amount1 = orderCopeReceivableVO.getAmount();
            amount = amount.add(amount1);

            ReceivableBillDetailVO receivableBillDetailVO = ConvertUtil.convert(orderCopeReceivableVO, ReceivableBillDetailVO.class);
            receivableBillDetailVO.setId(null);
            receivableBillDetailVO.setBillMasterId(null);
            receivableBillDetailVO.setOrderReceivableId(orderCopeReceivableId);
            receivableBillDetailVOS.add(receivableBillDetailVO);
        }

        receivableBillMasterVO.setAmount(amount);//金额
        Integer cid = orderCopeReceivableVOS.get(0).getCid();
        receivableBillMasterVO.setCid(cid);//币种(currency_info id)

        String currencyName = orderCopeReceivableVOS.get(0).getCurrencyName();//币种名称
        String amountFormat = amount+" "+currencyName;
        receivableBillMasterVO.setAmountFormat(amountFormat);//账单金额(格式化)

        receivableBillMasterVO.setReceivableBillDetailVOS(receivableBillDetailVOS);//应收账单明细list
        return CommonResult.success(receivableBillMasterVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<ReceivableBillMasterVO> affirmReceivableBill(ReceivableBillMasterForm form) {
        //1.保存应收账单主单
        ReceivableBillMaster receivableBillMaster = ConvertUtil.convert(form, ReceivableBillMaster.class);
        this.saveOrUpdate(receivableBillMaster);

        Long billMasterId = receivableBillMaster.getId();

        List<ReceivableBillDetailVO> receivableBillDetailVOS = form.getReceivableBillDetailVOS();
        List<ReceivableBillDetail> receivableBillDetails = ConvertUtil.convertList(receivableBillDetailVOS, ReceivableBillDetail.class);
        List<Long> orderReceivableIds = new ArrayList<>();
        receivableBillDetails.forEach(receivableBillDetail -> {
            receivableBillDetail.setBillMasterId(billMasterId);
            Long orderReceivableId = receivableBillDetail.getOrderReceivableId();
            orderReceivableIds.add(orderReceivableId);
        });
        //2.保存应收账单明细
        QueryWrapper<ReceivableBillDetail> receivableBillDetailQueryWrapper = new QueryWrapper<>();
        receivableBillDetailQueryWrapper.eq("bill_master_id", billMasterId);
        receivableBillDetailQueryWrapper.in("order_receivable_id", orderReceivableIds);
        receivableBillDetailService.remove(receivableBillDetailQueryWrapper);
        receivableBillDetailService.saveOrUpdateBatch(receivableBillDetails);
        //3.修改反写订单应收明细状态
        QueryWrapper<OrderCopeReceivable> orderCopeReceivableQueryWrapper = new QueryWrapper<>();
        orderCopeReceivableQueryWrapper.in("id", orderReceivableIds);
        List<OrderCopeReceivable> orderCopeReceivables = orderCopeReceivableService.list(orderCopeReceivableQueryWrapper);
        orderCopeReceivables.forEach(orderCopeReceivable -> {
            orderCopeReceivable.setStatus(1);//状态(0未生成账单 1已生成账单)
        });
        orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivables);

        ReceivableBillMasterVO receivableBillMasterVO = ConvertUtil.convert(receivableBillMaster, ReceivableBillMasterVO.class);
        List<ReceivableBillDetailVO> receivableBillDetailVOList = receivableBillDetailMapper.findReceivableBillDetailByBillMasterId(billMasterId);
        receivableBillMasterVO.setReceivableBillDetailVOS(receivableBillDetailVOList);

        return CommonResult.success(receivableBillMasterVO);
    }

    @Override
    public IPage<ReceivableBillMasterVO> findReceivableBillMasterByPage(QueryReceivableBillMasterForm form) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        //定义分页参数
        Page<ReceivableBillMasterVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<ReceivableBillMasterVO> pageInfo = receivableBillMasterMapper.findReceivableBillMasterByPage(page, form);
        List<ReceivableBillMasterVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(receivableBillMasterVO -> {

                List<String> billAmountList = new ArrayList<>();//账单金额(bill_amount)
                Long billMasterId = receivableBillMasterVO.getId();
                List<ReceivableBillDetailVO> receivableBillDetailVOS = receivableBillDetailMapper.findReceivableBillDetailByBillMasterId(billMasterId);
                Map<String, List<ReceivableBillDetailVO>> stringListMap = groupListByCid(receivableBillDetailVOS);
                List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
                for (Map.Entry<String, List<ReceivableBillDetailVO>> entry : stringListMap.entrySet()) {
                    String cid = entry.getKey();
                    List<ReceivableBillDetailVO> receivableBillDetailVOList = entry.getValue();
                    BigDecimal amountSum = new BigDecimal("0");
                    for (int i=0; i<receivableBillDetailVOList.size(); i++){
                        ReceivableBillDetailVO receivableBillDetailVO = receivableBillDetailVOList.get(i);
                        BigDecimal amount = receivableBillDetailVO.getAmount();
                        amountSum = amountSum.add(amount);
                    }
                    AmountVO amountVO = new AmountVO();
                    amountVO.setAmount(amountSum);//金额
                    amountVO.setCid(Integer.valueOf(cid));
                    amountVOS.add(amountVO);
                }
                receivableBillMasterVO.setAmountVOS(amountVOS);

                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    String amountFormat = amount.toString() + " " + cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    billAmountList.add(amountFormat);
                });
                receivableBillMasterVO.setBillAmountList(billAmountList);//账单金额(bill_amount)
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<ReceivableBillMasterVO> lookDetail(Long id) {
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        ReceivableBillMasterVO receivableBillMasterVO = receivableBillMasterMapper.findReceivableBillById(id);
        if(receivableBillMasterVO == null){
            return CommonResult.error(-1, "账单不存在");
        }
        List<String> billAmountList = new ArrayList<>();//账单金额(bill_amount)
        Long billMasterId = receivableBillMasterVO.getId();
        List<ReceivableBillDetailVO> receivableBillDetailVOS = receivableBillDetailMapper.findReceivableBillDetailByBillMasterId(billMasterId);
        Map<String, List<ReceivableBillDetailVO>> stringListMap = groupListByCid(receivableBillDetailVOS);
        List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
        for (Map.Entry<String, List<ReceivableBillDetailVO>> entry : stringListMap.entrySet()) {
            String cid = entry.getKey();
            List<ReceivableBillDetailVO> receivableBillDetailVOList = entry.getValue();
            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<receivableBillDetailVOList.size(); i++){
                ReceivableBillDetailVO receivableBillDetailVO = receivableBillDetailVOList.get(i);
                BigDecimal amount = receivableBillDetailVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            AmountVO amountVO = new AmountVO();
            amountVO.setAmount(amountSum);//金额
            amountVO.setCid(Integer.valueOf(cid));
            amountVOS.add(amountVO);
        }
        receivableBillMasterVO.setAmountVOS(amountVOS);

        amountVOS.forEach(amountVO -> {
            Integer cid = amountVO.getCid();
            BigDecimal amount = amountVO.getAmount();
            String amountFormat = amount.toString() + " " + cidMap.get(Long.valueOf(cid)).getCurrencyName();
            billAmountList.add(amountFormat);
        });
        receivableBillMasterVO.setBillAmountList(billAmountList);//账单金额(bill_amount)
        receivableBillMasterVO.setReceivableBillDetailVOS(receivableBillDetailVOS);
        return CommonResult.success(receivableBillMasterVO);
    }


    /**
     * 根据币种id，对应收账单费用进行分组
     * @param receivableBillDetailVOS 应收账单费用明细
     * @return
     */
    public Map<String, List<ReceivableBillDetailVO>> groupListByCid(List<ReceivableBillDetailVO> receivableBillDetailVOS) {
        Map<String, List<ReceivableBillDetailVO>> map = new HashMap<>();
        for (ReceivableBillDetailVO receivableBillDetailVO : receivableBillDetailVOS) {
            String key = receivableBillDetailVO.getCid().toString();
            List<ReceivableBillDetailVO> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(receivableBillDetailVO);
                map.put(key, tmpList);
            } else {
                tmpList.add(receivableBillDetailVO);
            }
        }
        return map;
    }

}