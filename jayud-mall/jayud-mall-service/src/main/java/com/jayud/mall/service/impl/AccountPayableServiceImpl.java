package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.PaymentBillForm;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.po.AccountPayable;
import com.jayud.mall.model.po.PayBillMaster;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.IAccountPayableService;
import com.jayud.mall.service.IPayBillMasterService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 应付对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class AccountPayableServiceImpl extends ServiceImpl<AccountPayableMapper, AccountPayable> implements IAccountPayableService {

    @Autowired
    AccountPayableMapper accountPayableMapper;
    @Autowired
    PayBillMasterMapper payBillMasterMapper;
    @Autowired
    PayBillDetailMapper payBillDetailMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;
    @Autowired
    CurrencyRateMapper currencyRateMapper;
    @Autowired
    IPayBillMasterService payBillMasterService;
    @Autowired
    BaseService baseService;

    @Override
    public IPage<AccountPayableVO> findAccountPayableByPage(QueryAccountPayableForm form) {
        //定义分页参数
        Page<AccountPayableVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<AccountPayableVO> pageInfo = accountPayableMapper.findAccountPayableByPage(page, form);

        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，城市cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

        List<AccountPayableVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(accountPayableVO -> {
                /*
                应付金额        payable_amount
                结算金额        balance_amount
                已付金额        paid_amount
                未付金额        unpaid_amount
                 */
                List<String> payableAmount = new ArrayList<>();//应付金额
                List<String> balanceAmount = new ArrayList<>();//结算金额
                List<String> paidAmount = new ArrayList<>();//已付金额
                List<String> unpaidAmount = new ArrayList<>();//未付金额

                //查询对账单下的账单
                Long accountPayableId = accountPayableVO.getId();//应付对账单id(account_payable id)
                List<PayBillMasterVO> payBillMasterVOS = payBillMasterMapper.findPayBillMasterByAccountPayableId(accountPayableId);

                //查询账单下的所有的明细费用,根据币种汇总金额
                payBillMasterVOS.forEach(payBillMasterVO -> {
                    Long billMasterId = payBillMasterVO.getId();
                    List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
                    Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);
                    List<AmountVO> amountVOS = new ArrayList<>();//账单下的费用,根据币种分组，汇总金额
                    for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
                        String cid = entry.getKey();
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
                });

                //汇总所有账单的费用，显示到对账单 应付金额、结算金额、已付金额、未付金额
                Map<Integer, BigDecimal> payableAmountMap = new HashMap<>();//应付金额
                Map<Integer, BigDecimal> balanceAmountMap = new HashMap<>();//结算金额
                Map<Integer, BigDecimal> paidAmountMap = new HashMap<>();//已付金额
                Map<Integer, BigDecimal> unpaidAmountMap = new HashMap<>();//未付金额
                for (int i=0; i<payBillMasterVOS.size(); i++){
                    PayBillMasterVO payBillMasterVO = payBillMasterVOS.get(i);
                    Integer status = payBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
                    List<AmountVO> amountVOS = payBillMasterVO.getAmountVOS();//账单下的费用,根据币种分组，汇总金额
                    amountVOS.forEach(amountVO -> {
                        Integer cid = amountVO.getCid();
                        BigDecimal amount = amountVO.getAmount();
                        //应付金额
                        BigDecimal bigDecimal = payableAmountMap.get(cid);
                        if(ObjectUtil.isEmpty(bigDecimal)){
                            payableAmountMap.put(cid, amount);
                        }else{
                            BigDecimal amountSum = bigDecimal.add(amount);
                            payableAmountMap.put(cid, amountSum);
                        }
                        //结算金额
                        BigDecimal bigDecimal1 = balanceAmountMap.get(cid);
                        if(ObjectUtil.isEmpty(bigDecimal1)){
                            balanceAmountMap.put(cid, amount);
                        }else{
                            BigDecimal amountSum = bigDecimal1.add(amount);
                            balanceAmountMap.put(cid, amountSum);
                        }
                    });
                    if(status == 1) {
                        //1已付款 已付金额
                        amountVOS.forEach(amountVO -> {
                            Integer cid = amountVO.getCid();
                            BigDecimal amount = amountVO.getAmount();
                            //已付金额
                            BigDecimal bigDecimal = paidAmountMap.get(cid);
                            if(ObjectUtil.isEmpty(bigDecimal)){
                                paidAmountMap.put(cid, amount);
                            }else{
                                BigDecimal amountSum = bigDecimal.add(amount);
                                paidAmountMap.put(cid, amountSum);
                            }
                        });
                    }else if(status == 0){
                        //0未付款 未付金额
                        amountVOS.forEach(amountVO -> {
                            Integer cid = amountVO.getCid();
                            BigDecimal amount = amountVO.getAmount();
                            BigDecimal bigDecimal = unpaidAmountMap.get(cid);
                            if(ObjectUtil.isEmpty(bigDecimal)){
                                unpaidAmountMap.put(cid, amount);
                            }else{
                                BigDecimal amountSum = bigDecimal.add(amount);
                                unpaidAmountMap.put(cid, amountSum);
                            }
                        });
                    }
                }

                //应付金额
                for (Map.Entry<Integer, BigDecimal> entry : payableAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    payableAmount.add(amountFormat);
                }
                //结算金额
                List<BigDecimal> balanceAmountCNY = new ArrayList<>();//统一装换为人民币
                for (Map.Entry<Integer, BigDecimal> entry : balanceAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    if(cid == 1){
                        balanceAmountCNY.add(amount);
                    }else{
                        Integer dcid = Integer.valueOf(cid);//本币(currency_info id)
                        Integer ocid = 1;//他币(currency_info id) 代表 人民币
                        CurrencyRateVO currencyRateVO = currencyRateMapper.findCurrencyRateByDcidAndOcid(dcid, ocid);
                        BigDecimal exchangeRate = currencyRateVO.getExchangeRate();
                        //转换为人民币
                        BigDecimal amountCNY = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        balanceAmountCNY.add(amountCNY);
                    }
                }
                BigDecimal balanceAmountSum = new BigDecimal("0");
                for (int i=0; i<balanceAmountCNY.size(); i++){
                    BigDecimal bigDecimal = balanceAmountCNY.get(i);
                    balanceAmountSum = balanceAmountSum.add(bigDecimal);
                }
                String balanceAmountTemp = balanceAmountSum.toString()+" "+"人民币";
                balanceAmount.add(balanceAmountTemp);

                //已付金额
                List<BigDecimal> paidAmountCNY = new ArrayList<>();
                for (Map.Entry<Integer, BigDecimal> entry : paidAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    if(cid == 1){
                        paidAmountCNY.add(amount);
                    }else{
                        Integer dcid = Integer.valueOf(cid);//本币(currency_info id)
                        Integer ocid = 1;//他币(currency_info id) 代表 人民币
                        CurrencyRateVO currencyRateVO = currencyRateMapper.findCurrencyRateByDcidAndOcid(dcid, ocid);
                        BigDecimal exchangeRate = currencyRateVO.getExchangeRate();
                        //转换为人民币
                        BigDecimal amountCNY = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        paidAmountCNY.add(amountCNY);
                    }
                }
                BigDecimal paidAmountSum = new BigDecimal("0");
                for (int i=0; i<paidAmountCNY.size(); i++){
                    BigDecimal bigDecimal = paidAmountCNY.get(i);
                    paidAmountSum = paidAmountSum.add(bigDecimal);
                }
                String paidAmountTemp = paidAmountSum.toString()+" "+"人民币";
                paidAmount.add(paidAmountTemp);

                //未付金额
                List<BigDecimal> unpaidAmountCNY = new ArrayList<>();
                for (Map.Entry<Integer, BigDecimal> entry : unpaidAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    if(cid == 1){
                        unpaidAmountCNY.add(amount);
                    }else{
                        Integer dcid = Integer.valueOf(cid);//本币(currency_info id)
                        Integer ocid = 1;//他币(currency_info id) 代表 人民币
                        CurrencyRateVO currencyRateVO = currencyRateMapper.findCurrencyRateByDcidAndOcid(dcid, ocid);
                        BigDecimal exchangeRate = currencyRateVO.getExchangeRate();
                        //转换为人民币
                        BigDecimal amountCNY = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                        unpaidAmountCNY.add(amountCNY);
                    }
                }
                BigDecimal unpaidAmountSum = new BigDecimal("0");
                for (int i=0; i<unpaidAmountCNY.size(); i++){
                    BigDecimal bigDecimal = unpaidAmountCNY.get(i);
                    unpaidAmountSum = unpaidAmountSum.add(bigDecimal);
                }
                String unpaidAmountTemp = unpaidAmountSum.toString()+" "+"人民币";
                unpaidAmount.add(unpaidAmountTemp);

                accountPayableVO.setPayableAmount(payableAmount);//应付金额
                accountPayableVO.setBalanceAmount(balanceAmount);//结算金额
                accountPayableVO.setPaidAmount(paidAmount);//已付金额
                accountPayableVO.setUnpaidAmount(unpaidAmount);//未付金额
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<AccountPayableVO> lookDetail(Long id) {
        AccountPayableVO accountPayableVO = accountPayableMapper.findAccountPayableById(id);
        if(ObjectUtil.isEmpty(accountPayableVO)){
            return CommonResult.error(-1, "对账单不存在");
        }
        List<PayBillMasterVO> payBillMasterVOS = payBillMasterMapper.findPayBillMasterByAccountPayableId(id);

        payBillMasterVOS.forEach(payBillMasterVO -> {
            Long billMasterId = payBillMasterVO.getId();
            List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);

            List<String> billAmount = new ArrayList<>();//账单金额
            List<BigDecimal> balanceAmountCNY = new ArrayList<>();
            List<String> balanceAmount = new ArrayList<>();//结算金额
            //根据 币种id，分组
            Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);
            // entrySet遍历，在键和值都需要时使用（最常用）
            for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
                String cid = entry.getKey();//币种id
                List<PayBillDetailVO> payBillDetailVOList = entry.getValue();
                BigDecimal amountSum = new BigDecimal("0");
                for (int i=0; i<payBillDetailVOList.size(); i++){
                    PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(i);
                    BigDecimal amount = payBillDetailVO.getAmount();
                    amountSum = amountSum.add(amount);
                }
                PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(0);
                String currencyName = payBillDetailVO.getCurrencyName();
                //账单金额，不需要进行汇率换算
                String billAmountTemp =  amountSum.toString()+" "+currencyName;
                billAmount.add(billAmountTemp);
                //结算金额，统一转换为人民币结算 cid=1 代表为 人民币
                if(cid.equals("1")){
                    balanceAmountCNY.add(amountSum);
                }else{
                    Integer dcid = Integer.valueOf(cid);//本币(currency_info id)
                    Integer ocid = 1;//他币(currency_info id)
                    CurrencyRateVO currencyRateVO = currencyRateMapper.findCurrencyRateByDcidAndOcid(dcid, ocid);
                    BigDecimal exchangeRate = currencyRateVO.getExchangeRate();
                    //转换为人民币
                    BigDecimal amountCNY = amountSum.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                    balanceAmountCNY.add(amountCNY);
                }
            }

            BigDecimal balanceAmountSum = new BigDecimal("0");
            for (int i=0; i<balanceAmountCNY.size(); i++){
                BigDecimal bigDecimal = balanceAmountCNY.get(i);
                balanceAmountSum = balanceAmountSum.add(bigDecimal);
            }
            String balanceAmountTemp = balanceAmountSum.toString()+" "+"人民币";
            balanceAmount.add(balanceAmountTemp);

            payBillMasterVO.setBillAmount(billAmount);
            payBillMasterVO.setBalanceAmount(balanceAmount);

        });
        accountPayableVO.setPayBillMasterVOS(payBillMasterVOS);
        return CommonResult.success(accountPayableVO);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult createPayMonthlyStatement(MonthlyStatementForm form) {
        LocalDateTime monthlyStatement = form.getMonthlyStatement();//月结时间
        LocalDateTime firstday = monthlyStatement.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime lastDay = monthlyStatement.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        //TODO 验证当前月结时间（账单日期），是否可以月结
        List<PayBillMaster> verifyPayBillMaster = payBillMasterMapper.verifyPayBillMasterByCreateTime(firstday, lastDay);
        if(CollUtil.isNotEmpty(verifyPayBillMaster)){
            return CommonResult.error(-1, "当前账期已经月结，无法创建对账单");
        }
        //TODO 查询可以月结的，应付对账单
        List<PayBillMaster> payBillMasterList = payBillMasterMapper.findPayBillMasterByCreateTime(firstday, lastDay);

        Map<String, List<PayBillMaster>> stringListMap = groupListByLegalPersonIdAndSupplierId(payBillMasterList);

        // entrySet遍历，在键和值都需要时使用（最常用）
        for (Map.Entry<String, List<PayBillMaster>> entry : stringListMap.entrySet()) {
            String key = entry.getKey();
            String[] split = key.split("&");//PersonIdAndCustomerId
            String legalPersonId = split[0];//法人id
            String supplierId = split[1];//供应商id

            //对账单编号(应付)
            String dzdNo = NumberGeneratedUtils.getOrderNoByCode2("dzd_no_pay");//对账单编号

            //1.创建并保存应付对账单
            AccountPayable accountPayable = new AccountPayable();
            accountPayable.setDzdNo(dzdNo);//对账单编号
            accountPayable.setLegalPersonId(Long.valueOf(legalPersonId));//法人主体id(legal_entity id)
            accountPayable.setSupplierId(Integer.valueOf(supplierId));//供应商id(supplier_info id)
            accountPayable.setPaymentDaysStart(firstday);//账期开始时间
            accountPayable.setPaymentDaysEnd(lastDay);//账期结束时间
            accountPayable.setStatus(0);//状态(0未付款 1已付款)
            accountPayable.setCreateTime(LocalDateTime.now());//制单时间
            this.saveOrUpdate(accountPayable);

            Long accountPayableId = accountPayable.getId();//应付对账单id(account_payable id)
            //2.修改对应的应收账单，设置应收对账单id （PS:应收账单 和 应收对账单 不一样）
            List<PayBillMaster> payBillMasters = entry.getValue();
            payBillMasters.forEach(payBillMaster -> {
                payBillMaster.setAccountPayableId(accountPayableId);
            });
            payBillMasterService.saveOrUpdateBatch(payBillMasters);
        }
        return CommonResult.success("生成应付月结账单(创建应付对账单)，成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult paymentBill(PaymentBillForm form) {
        AuthUser user = baseService.getUser();
        Long id = form.getId();//ids， 应付账单主单 pay_bill_master id
        //根据ids，查询 应付账单主单 pay_bill_master id
        PayBillMasterVO payBillMasterVO = payBillMasterMapper.findPayBillMasterById(id);
        if(ObjectUtil.isEmpty(payBillMasterVO)){
            return CommonResult.error(-1, "账单不存在");
        }
        //验证，状态是否为0未付款  账单状态(0未付款 1已付款)
        Integer status = payBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
        if(status != 0){
            //payBillMasterVOList 不为空，代表存在状态不为0未付款 的账单，验证不通过
            return CommonResult.error(-1, "账单状态不正确，无法付款");
        }

        Long billMasterId = payBillMasterVO.getId();
        List<PayBillDetailVO> payBillDetailVOS = payBillDetailMapper.findPayBillDetailByBillMasterId(billMasterId);
        Map<String, List<PayBillDetailVO>> stringListMap = groupListByCid(payBillDetailVOS);

        List<BigDecimal> balanceAmountCNY = new ArrayList<>();
        for (Map.Entry<String, List<PayBillDetailVO>> entry : stringListMap.entrySet()) {
            String cid = entry.getKey();//币种id
            List<PayBillDetailVO> payBillDetailVOList = entry.getValue();
            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<payBillDetailVOList.size(); i++){
                PayBillDetailVO payBillDetailVO = payBillDetailVOList.get(i);
                BigDecimal amount = payBillDetailVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            //结算金额，统一转换为人民币结算 cid=1 代表为 人民币
            if(cid.equals("1")){
                balanceAmountCNY.add(amountSum);
            }else{
                Integer dcid = Integer.valueOf(cid);//本币(currency_info id)
                Integer ocid = 1;//他币(currency_info id)
                CurrencyRateVO currencyRateVO = currencyRateMapper.findCurrencyRateByDcidAndOcid(dcid, ocid);
                BigDecimal exchangeRate = currencyRateVO.getExchangeRate();
                //转换为人民币
                BigDecimal amountCNY = amountSum.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_UP);
                balanceAmountCNY.add(amountCNY);
            }
        }

        BigDecimal balanceAmountSum = new BigDecimal("0");
        for (int i=0; i<balanceAmountCNY.size(); i++){
            BigDecimal bigDecimal = balanceAmountCNY.get(i);
            balanceAmountSum = balanceAmountSum.add(bigDecimal);
        }

        payBillMasterVO.setBalanceAmountCNY(balanceAmountSum);//结算金额(数值)
        payBillMasterVO.setBalanceAmountCid(1);//结算金额(币种id) 默认为1 代表人民币


        BigDecimal amountFront = form.getAmount();//前端输入的金额
        Integer cidFront = form.getCid();//前端选择的币种id

        BigDecimal balanceAmount = payBillMasterVO.getBalanceAmountCNY();//后台（结算金额）
        Integer cid = payBillMasterVO.getBalanceAmountCid();
        if(balanceAmount.compareTo(amountFront) != 0){
            return CommonResult.error(-1, "输入的付款金额与后台计算的结算金额不一致");
        }
        if(cidFront != cid){
            return CommonResult.error(-1, "选择的币种与结算币种不一致");
        }

        //1.修改保存 应付账单主单 改状态
        PayBillMaster payBillMaster = ConvertUtil.convert(payBillMasterVO, PayBillMaster.class);
        payBillMaster.setStatus(1);//账单状态(0未付款 1已付款)
        payBillMaster.setPayerId(user.getId());//付款人(system_user id)
        payBillMaster.setPaymentTime(LocalDateTime.now());//付款日期

        List<TemplateUrlVO> voucherUrls = form.getVoucherUrls();
        if(CollUtil.isNotEmpty(voucherUrls)){
            String s = JSONObject.toJSONString(voucherUrls);
            payBillMaster.setVoucherUrl(s);//交易凭证(url)
        }
        payBillMasterService.saveOrUpdate(payBillMaster);

        //2.判断 应付对账单下 应付账单 是否全部已付款，是，修改应付对账单状态
        Long accountPayableId = payBillMasterVO.getAccountPayableId();
        List<PayBillMasterVO> payBillMasterVOS = payBillMasterMapper.verifyPayBillMasterByAccountPayableId(accountPayableId);
        if(CollUtil.isEmpty(payBillMasterVOS)){
            //payBillMasterVOS 为空，代表不存在未付款的 应付对账单
            AccountPayableVO accountPayableVO = accountPayableMapper.findAccountPayableById(accountPayableId);
            AccountPayable accountPayable = ConvertUtil.convert(accountPayableVO, AccountPayable.class);
            accountPayable.setStatus(1);//状态(0未付款 1已付款)
            this.saveOrUpdate(accountPayable);
        }
        return CommonResult.success("账单(应付账单)，付款成功");
    }

    /**
     * 根据`法人id`，`供应商id`，对list进行分组
     * @param payBillMasterList 应付对账单
     * @return
     */
    public Map<String, List<PayBillMaster>> groupListByLegalPersonIdAndSupplierId(List<PayBillMaster> payBillMasterList) {
        Map<String, List<PayBillMaster>> map = new HashMap<>();
        for (PayBillMaster payBillMaster : payBillMasterList) {
            String key = payBillMaster.getLegalPersonId()+"&"+payBillMaster.getSupplierId();//法人id + 供应商id
            List<PayBillMaster> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(payBillMaster);
                map.put(key, tmpList);
            } else {
                tmpList.add(payBillMaster);
            }
        }
        return map;
    }


}
