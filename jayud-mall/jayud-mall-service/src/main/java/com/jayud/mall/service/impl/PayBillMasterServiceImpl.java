package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.PayBillDetailMapper;
import com.jayud.mall.mapper.PayBillMasterMapper;
import com.jayud.mall.model.bo.PayBillForm;
import com.jayud.mall.model.bo.PayBillMasterForm;
import com.jayud.mall.model.po.OrderCopeWith;
import com.jayud.mall.model.po.PayBillDetail;
import com.jayud.mall.model.po.PayBillMaster;
import com.jayud.mall.model.vo.OrderCopeWithVO;
import com.jayud.mall.model.vo.PayBillDetailVO;
import com.jayud.mall.model.vo.PayBillMasterVO;
import com.jayud.mall.service.IOrderCopeWithService;
import com.jayud.mall.service.IPayBillDetailService;
import com.jayud.mall.service.IPayBillMasterService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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

}
