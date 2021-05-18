package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountBalance;
import com.jayud.mall.model.po.AccountReceivable;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.jayud.mall.model.po.TradingRecord;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.model.vo.domain.AuthUser;
import com.jayud.mall.service.*;
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
 * 应收对账单表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-17
 */
@Service
public class AccountReceivableServiceImpl extends ServiceImpl<AccountReceivableMapper, AccountReceivable> implements IAccountReceivableService {

    @Autowired
    AccountReceivableMapper accountReceivableMapper;
    @Autowired
    ReceivableBillMasterMapper receivableBillMasterMapper;
    @Autowired
    ReceivableBillDetailMapper receivableBillDetailMapper;
    @Autowired
    AccountBalanceMapper accountBalanceMapper;
    @Autowired
    CurrencyInfoMapper currencyInfoMapper;
    @Autowired
    BaseService baseService;
    @Autowired
    IReceivableBillMasterService receivableBillMasterService;
    @Autowired
    ITradingRecordService tradingRecordService;
    @Autowired
    IAccountBalanceService accountBalanceService;

    @Override
    public IPage<AccountReceivableVO> findAccountReceivableByPage(QueryAccountReceivableForm form) {
        //定义分页参数
        Page<AccountReceivableVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<AccountReceivableVO> pageInfo = accountReceivableMapper.findAccountReceivableByPage(page, form);

        //将币种信息转换为map，城市cid为键，币种信息为值
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));

        List<AccountReceivableVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(accountReceivableVO -> {
                /*
                应收金额(前端：对账单金额)
                结算金额
                已收金额(前端：已付款金额)
                未收金额(前端：待付款金额)
                */
                List<String> receivableAmount = new ArrayList<>();//应收金额(前端：对账单金额)
                List<String> balanceAmount = new ArrayList<>();//结算金额
                List<String> receivedAmount = new ArrayList<>();//已收金额(前端：已付款金额)
                List<String> uncalledAmount = new ArrayList<>();//未收金额(前端：待付款金额)

                //查询对账单下的账单
                Long accountReceivableId = accountReceivableVO.getId();//应收对账单id(account_receivable id)
                List<ReceivableBillMasterVO> receivableBillMasterVOS = receivableBillMasterMapper.findReceivableBillMasterByAccountReceivableId(accountReceivableId);

                //查询账单下的所有的明细费用,根据币种汇总金额
                receivableBillMasterVOS.forEach(receivableBillMasterVO -> {
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

                });


                //汇总所有账单的费用，显示到对账单 应收金额、结算金额、已收金额、未收金额
                Map<Integer, BigDecimal> receivableAmountMap = new HashMap<>();//应收金额(前端：对账单金额)
                Map<Integer, BigDecimal> balanceAmountMap = new HashMap<>();//结算金额
                Map<Integer, BigDecimal> receivedAmountMap = new HashMap<>();//已收金额(前端：已付款金额)
                Map<Integer, BigDecimal> uncalledAmountMap = new HashMap<>();//未收金额(前端：待付款金额)
                for (int i=0; i<receivableBillMasterVOS.size(); i++){
                    ReceivableBillMasterVO receivableBillMasterVO = receivableBillMasterVOS.get(i);
                    Integer status = receivableBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
                    List<AmountVO> amountVOS = receivableBillMasterVO.getAmountVOS();//账单下的费用,根据币种分组，汇总金额
                    amountVOS.forEach(amountVO -> {
                        Integer cid = amountVO.getCid();
                        BigDecimal amount = amountVO.getAmount();
                        //应收金额(前端：对账单金额)
                        BigDecimal bigDecimal = receivableAmountMap.get(cid);
                        if(ObjectUtil.isEmpty(bigDecimal)){
                            receivableAmountMap.put(cid, amount);
                        }else{
                            BigDecimal amountSum = bigDecimal.add(amount);
                            receivableAmountMap.put(cid, amountSum);
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
                    if(status == 1){
                        //1已付款 已收金额
                        amountVOS.forEach(amountVO -> {
                            Integer cid = amountVO.getCid();
                            BigDecimal amount = amountVO.getAmount();
                            //已收金额(前端：已付款金额)
                            BigDecimal bigDecimal = receivedAmountMap.get(cid);
                            if(ObjectUtil.isEmpty(bigDecimal)){
                                receivedAmountMap.put(cid, amount);
                            }else{
                                BigDecimal amountSum = bigDecimal.add(amount);
                                receivedAmountMap.put(cid, amountSum);
                            }
                        });
                    }else if(status == 0){
                        //0未付款 未收金额
                        amountVOS.forEach(amountVO -> {
                            Integer cid = amountVO.getCid();
                            BigDecimal amount = amountVO.getAmount();
                            //未收金额(前端：待付款金额)
                            BigDecimal bigDecimal = uncalledAmountMap.get(cid);
                            if(ObjectUtil.isEmpty(bigDecimal)){
                                uncalledAmountMap.put(cid, amount);
                            }else{
                                BigDecimal amountSum = bigDecimal.add(amount);
                                uncalledAmountMap.put(cid, amountSum);
                            }
                        });
                    }
                }

                //应收金额(前端：对账单金额)
                for (Map.Entry<Integer, BigDecimal> entry : receivableAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    receivableAmount.add(amountFormat);
                }
                //结算金额
                for (Map.Entry<Integer, BigDecimal> entry : balanceAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    balanceAmount.add(amountFormat);
                }
                //已收金额(前端：已付款金额)
                for (Map.Entry<Integer, BigDecimal> entry : receivedAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    receivedAmount.add(amountFormat);
                }
                //未收金额(前端：待付款金额)
                for (Map.Entry<Integer, BigDecimal> entry : uncalledAmountMap.entrySet()) {
                    Integer cid = entry.getKey();
                    BigDecimal amount = entry.getValue();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    uncalledAmount.add(amountFormat);
                }
                accountReceivableVO.setReceivableAmount(receivableAmount);//应收金额(前端：对账单金额)
                accountReceivableVO.setBalanceAmount(balanceAmount);//结算金额
                accountReceivableVO.setReceivedAmount(receivedAmount);//已收金额(前端：已付款金额)
                accountReceivableVO.setUncalledAmount(uncalledAmount);//未收金额(前端：待付款金额)

            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<AccountReceivableVO> lookDetail(Long id) {
        AccountReceivableVO accountReceivableVO = accountReceivableMapper.findAccountReceivableById(id);
        if(ObjectUtil.isEmpty(accountReceivableVO)){
            return CommonResult.error(-1, "对账单不存在");
        }
        List<CurrencyInfoVO> currencyInfoVOList = currencyInfoMapper.allCurrencyInfo();
        //将币种信息转换为map，cid为键，币种信息为值
        Map<Long, CurrencyInfoVO> cidMap = currencyInfoVOList.stream().collect(Collectors.toMap(CurrencyInfoVO::getId, c -> c));
        //客户的账户余额
        Integer customerId = accountReceivableVO.getCustomerId();
        List<AccountBalanceVO> accountBalanceVOS = accountBalanceMapper.findAccountBalanceByCustomerId(customerId);
        List<String> accountBalanceList = new ArrayList<>();//客户账户余额
        accountBalanceVOS.forEach(accountBalanceVO -> {
            String amountFormat = accountBalanceVO.getAmountFormat();
            accountBalanceList.add(amountFormat);
        });
        accountReceivableVO.setAccountBalanceList(accountBalanceList);

        /**
         * 对账单总金额、已付款金额、待付款金额
         */
        List<String> receivableAmount = new ArrayList<>();//应收金额(前端：对账单金额、对账单总金额)
        List<String> receivedAmount = new ArrayList<>();//已收金额(前端：已付款金额)
        List<String> uncalledAmount = new ArrayList<>();//未收金额(前端：待付款金额)

        //查询对账单下的账单
        Long accountReceivableId = accountReceivableVO.getId();
        List<ReceivableBillMasterVO> receivableBillMasterVOS = receivableBillMasterMapper.findReceivableBillMasterByAccountReceivableId(accountReceivableId);
        //查询账单下的所有的明细费用,根据币种汇总金额
        receivableBillMasterVOS.forEach(receivableBillMasterVO -> {
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

            /**
             * 详细:
             * 1.账单金额 结算金额
             * 2.账单金额 已付款金额
             */
            //1.账单金额 结算金额
            List<String> billAmountList = new ArrayList<>();//账单金额(bill_amount)
            List<String> balanceAmountList = new ArrayList<>();//结算金额(balance_amount)
            //2.账单金额 已付款金额
            List<String> receivableAmount2 = new ArrayList<>();//应收金额(前端：账单金额)
            List<String> receivedAmount2 = new ArrayList<>();//已收金额(前端：已付款金额)

            //账单金额(bill_amount)、结算金额(balance_amount)、应收金额(前端：账单金额)
            amountVOS.forEach(amountVO -> {
                Integer cid = amountVO.getCid();
                BigDecimal amount = amountVO.getAmount();
                String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                billAmountList.add(amountFormat);
                balanceAmountList.add(amountFormat);
                receivableAmount2.add(amountFormat);
            });
            Integer status = receivableBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
            if(status == 1){
                //已收金额(前端：已付款金额)
                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
                    receivedAmount2.add(amountFormat);
                });
            }
            receivableBillMasterVO.setBillAmountList(billAmountList);//账单金额(bill_amount)
            receivableBillMasterVO.setBalanceAmountList(balanceAmountList);//结算金额(balance_amount)
            receivableBillMasterVO.setReceivableAmount(receivableAmount2);//应收金额(前端：账单金额)
            receivableBillMasterVO.setReceivedAmount(receivedAmount2);//已收金额(前端：已付款金额)
        });

        //汇总所有账单的费用，显示到对账单 对账单总金额、已付款金额、待付款金额
        Map<Integer, BigDecimal> receivableAmountMap = new HashMap<>();//应收金额(前端：对账单金额、对账单总金额)
        Map<Integer, BigDecimal> receivedAmountMap = new HashMap<>();//已收金额(前端：已付款金额)
        Map<Integer, BigDecimal> uncalledAmountMap = new HashMap<>();//未收金额(前端：待付款金额)

        for (int i=0; i<receivableBillMasterVOS.size(); i++){
            ReceivableBillMasterVO receivableBillMasterVO = receivableBillMasterVOS.get(i);
            Integer status = receivableBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
            List<AmountVO> amountVOS = receivableBillMasterVO.getAmountVOS();//账单下的费用,根据币种分组，汇总金额
            amountVOS.forEach(amountVO -> {
                Integer cid = amountVO.getCid();
                BigDecimal amount = amountVO.getAmount();
                //应收金额(前端：对账单金额)
                BigDecimal bigDecimal = receivableAmountMap.get(cid);
                if(ObjectUtil.isEmpty(bigDecimal)){
                    receivableAmountMap.put(cid, amount);
                }else{
                    BigDecimal amountSum = bigDecimal.add(amount);
                    receivableAmountMap.put(cid, amountSum);
                }
            });
            if(status == 1){
                //1已付款 已收金额
                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    //已收金额(前端：已付款金额)
                    BigDecimal bigDecimal = receivedAmountMap.get(cid);
                    if(ObjectUtil.isEmpty(bigDecimal)){
                        receivedAmountMap.put(cid, amount);
                    }else{
                        BigDecimal amountSum = bigDecimal.add(amount);
                        receivedAmountMap.put(cid, amountSum);
                    }
                });
            }else if(status == 0){
                //0未付款 未收金额
                amountVOS.forEach(amountVO -> {
                    Integer cid = amountVO.getCid();
                    BigDecimal amount = amountVO.getAmount();
                    //未收金额(前端：待付款金额)
                    BigDecimal bigDecimal = uncalledAmountMap.get(cid);
                    if(ObjectUtil.isEmpty(bigDecimal)){
                        uncalledAmountMap.put(cid, amount);
                    }else{
                        BigDecimal amountSum = bigDecimal.add(amount);
                        uncalledAmountMap.put(cid, amountSum);
                    }
                });
            }
        }
        //应收金额(前端：对账单金额)
        for (Map.Entry<Integer, BigDecimal> entry : receivableAmountMap.entrySet()) {
            Integer cid = entry.getKey();
            BigDecimal amount = entry.getValue();
            String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
            receivableAmount.add(amountFormat);
        }
        //已收金额(前端：已付款金额)
        for (Map.Entry<Integer, BigDecimal> entry : receivedAmountMap.entrySet()) {
            Integer cid = entry.getKey();
            BigDecimal amount = entry.getValue();
            String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
            receivedAmount.add(amountFormat);
        }
        //未收金额(前端：待付款金额)
        for (Map.Entry<Integer, BigDecimal> entry : uncalledAmountMap.entrySet()) {
            Integer cid = entry.getKey();
            BigDecimal amount = entry.getValue();
            String amountFormat = amount.toString()+" "+cidMap.get(Long.valueOf(cid)).getCurrencyName();
            uncalledAmount.add(amountFormat);
        }
        accountReceivableVO.setReceivableAmount(receivableAmount);
        accountReceivableVO.setReceivedAmount(receivedAmount);
        accountReceivableVO.setUncalledAmount(uncalledAmount);
        //对账单关联的账单(应收账单list)
        accountReceivableVO.setReceivableBillMasterVOS(receivableBillMasterVOS);
        return CommonResult.success(accountReceivableVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult createRecMonthlyStatement(MonthlyStatementForm form) {
        LocalDateTime monthlyStatement = form.getMonthlyStatement();//月结时间
        LocalDateTime firstday = monthlyStatement.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime lastDay = monthlyStatement.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        //TODO 验证当前月结时间（账单日期），是否可以月结
        List<ReceivableBillMaster> verifyReceivableBillMasters = receivableBillMasterMapper.verifyReceivableBillMasterByCreateTime(firstday, lastDay);
        if(CollUtil.isNotEmpty(verifyReceivableBillMasters)){
            return CommonResult.error(-1, "当前账期已经月结，无法创建对账单");
        }

        //TODO 查询可以月结的，应收对账单
        List<ReceivableBillMaster> receivableBillMasterList = receivableBillMasterMapper.findReceivableBillMasterByCreateTime(firstday, lastDay);

        Map<String, List<ReceivableBillMaster>> stringListMap = groupListByLegalPersonIdAndCustomerId(receivableBillMasterList);

        // entrySet遍历，在键和值都需要时使用（最常用）
        for (Map.Entry<String, List<ReceivableBillMaster>> entry : stringListMap.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            String key = entry.getKey();
            String[] split = key.split("&");//PersonIdAndCustomerId
            String legalPersonId = split[0];//法人id
            String customerId = split[1];//客户id

            //对账单编号(应收)
            String dzdNo = NumberGeneratedUtils.getOrderNoByCode2("dzd_no_rec");//对账单编号

            //1.保存应收对账单
            AccountReceivable accountReceivable = new AccountReceivable();
            accountReceivable.setDzdNo(dzdNo);//对账单编号
            accountReceivable.setLegalPersonId(Long.valueOf(legalPersonId));//法人id(legal_person id)
            accountReceivable.setCustomerId(Integer.valueOf(customerId));//客户ID(customer id)
            accountReceivable.setPaymentDaysStart(firstday);//账期开始时间
            accountReceivable.setPaymentDaysEnd(lastDay);//账期结束时间
            accountReceivable.setStatus(0);//状态(0待核销 1核销完成)
            accountReceivable.setCreateTime(LocalDateTime.now());//制单时间
            this.saveOrUpdate(accountReceivable);

            Long accountReceivableId = accountReceivable.getId();//应收对账单id(account_receivable id)

            List<ReceivableBillMaster> receivableBillMasters = entry.getValue();
            receivableBillMasters.forEach(receivableBillMaster -> {
                receivableBillMaster.setAccountReceivableId(accountReceivableId);
            });
            receivableBillMasterService.saveOrUpdateBatch(receivableBillMasters);
        }
        return CommonResult.success("生成应收月结账单(创建应收对账单)，成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult verificationBill(Long id) {
        AuthUser user = baseService.getUser();

        //查询应收账单主单
        //应收账单主单 receivable_bill_master id
        ReceivableBillMasterVO receivableBillMasterVO = receivableBillMasterMapper.findReceivableBillById(id);
        if(ObjectUtil.isEmpty(receivableBillMasterVO)){
            return CommonResult.error(-1, "该账单不存在，无法核销");
        }

        Integer status = receivableBillMasterVO.getStatus();//账单状态(0未付款 1已付款)
        if(status != 0){
            return CommonResult.error(-1, "该账单状态不正确，无法核销");
        }
        Long accountReceivableId = receivableBillMasterVO.getAccountReceivableId();//应收对账单id(account_receivable id)
        if(ObjectUtil.isEmpty(accountReceivableId)){
            return CommonResult.error(-1, "该账单没有月结，生成对账单，不能核销");
        }

        //查询应收账单客户余额
        Integer customerId = receivableBillMasterVO.getCustomerId();
        List<AccountBalanceVO> accountBalanceVOS = accountBalanceMapper.findAccountBalanceByCustomerId(customerId);
        if(CollUtil.isEmpty(accountBalanceVOS)){
            return CommonResult.error(-1, "客户账户没有余额，不能核销");
        }

        //查询核销应收账单的费用明细
        List<ReceivableBillDetailVO> receivableBillDetailVOS = receivableBillDetailMapper.findReceivableBillDetailByBillMasterId(id);
        if(CollUtil.isEmpty(receivableBillDetailVOS)){
            return CommonResult.error(-1, "核销的账单没有不存在费用明细，不能核销");
        }
        //根据币种id，对费用进行分组
        Map<String, List<ReceivableBillDetailVO>> stringListMap = groupListByCid(receivableBillDetailVOS);

        // entrySet遍历，在键和值都需要时使用（最常用）
        for (Map.Entry<String, List<ReceivableBillDetailVO>> entry : stringListMap.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            String cid = entry.getKey();
            List<ReceivableBillDetailVO> receivableBillDetailVOList = entry.getValue();
            BigDecimal amountSum = new BigDecimal("0");
            for (int i=0; i<receivableBillDetailVOList.size(); i++){
                ReceivableBillDetailVO receivableBillDetailVO = receivableBillDetailVOList.get(i);
                BigDecimal amount = receivableBillDetailVO.getAmount();
                amountSum = amountSum.add(amount);
            }
            //查询客户对应币种的余额
            AccountBalanceVO accountBalanceVO = accountBalanceMapper.findAccountBalanceByCustomerIdAndCid(Long.valueOf(customerId), Long.valueOf(cid));
            if(ObjectUtil.isEmpty(accountBalanceVO)){
                return CommonResult.error(-1, "核销客户，核销对应的币种不存在");
            }
            BigDecimal amount = accountBalanceVO.getAmount();
            if(amount.compareTo(amountSum) == -1){
                return CommonResult.error(-1, "核销客户,核销对应的币种余额不足");
            }

            //1.创建交易记录 支出 trading_record
            TradingRecord tradingRecord = new TradingRecord();

            tradingRecord.setCustomerId(Long.valueOf(customerId));//客户ID(customer id)
            String tradingNo = NumberGeneratedUtils.getOrderNoByCode2("TradingNo_ZF");//交易单号(支付)
            tradingRecord.setTradingNo(tradingNo);//交易单号
            tradingRecord.setTradingType(2);//交易类型(1充值 2支付)
            tradingRecord.setAmount(amountSum);//金额
            tradingRecord.setCid(Long.valueOf(cid));//币种(currency_info id)
            /*
                serialNumber varchar(100)   null comment '交易流水号',
                voucher_url  text           null comment '交易凭证(url)',
             */
            tradingRecord.setStatus("2");//状态(0待审核 1审核通过 2审核不通过)
            tradingRecord.setRemark("支付账单:"+receivableBillMasterVO.getBillCode());//交易备注
            /*
            creator      int(10)        null comment '创建人(customer id)',
             */
            tradingRecord.setCreateTime(LocalDateTime.now());//创建时间(交易时间)(充值时间)
            tradingRecord.setAuditor(user.getId().intValue());//审核人(system_user id)
            tradingRecord.setAuditTime(LocalDateTime.now());//审核时间
            tradingRecordService.saveOrUpdate(tradingRecord);

            //2.修改客户账户余额
            BigDecimal subtract = amount.subtract(amountSum);//账户余额 - 支出余额
            AccountBalance accountBalance = ConvertUtil.convert(accountBalanceVO, AccountBalance.class);
            accountBalance.setAmount(subtract);//重新计算并设置余额
            accountBalanceService.saveOrUpdate(accountBalance);

        }
        //3.修改应收账单主单状态（设置核销人、核销日期）
        ReceivableBillMaster receivableBillMaster = ConvertUtil.convert(receivableBillMasterVO, ReceivableBillMaster.class);
        receivableBillMaster.setStatus(1);//账单状态(0未付款 1已付款)
        receivableBillMaster.setVerificationUserId(user.getId());//核销人(system_user id)
        receivableBillMaster.setVerificationTime(LocalDateTime.now());//核销日期
        receivableBillMasterService.saveOrUpdate(receivableBillMaster);

        //4.检查 应收对账单 下的 应收账单，是否全部核销，是全部核销，则修改 应收对账单 状态
        List<ReceivableBillMasterVO> receivableBillMasterVOS = receivableBillMasterMapper.verifyReceivableBillMasterByAccountReceivableId(accountReceivableId);
        if(CollUtil.isEmpty(receivableBillMasterVOS)){
            //receivableBillMasterVOS 为空，说明应收对账单下的应收账单已经全部核销，修改 应收对账单 状态
            AccountReceivableVO accountReceivableVO = accountReceivableMapper.findAccountReceivableById(accountReceivableId);
            AccountReceivable accountReceivable = ConvertUtil.convert(accountReceivableVO, AccountReceivable.class);
            accountReceivable.setStatus(1);//状态(0待核销 1核销完成)
            this.saveOrUpdate(accountReceivable);
        }

        String billCode = receivableBillMasterVO.getBillCode();
        return CommonResult.success("账单编号："+billCode+",核销成功");
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

    /**
     * 根据`法人id`，`客户id`，对list进行分组
     * @param receivableBillMasterList 应收对账单
     * @return
     */
    public Map<String, List<ReceivableBillMaster>> groupListByLegalPersonIdAndCustomerId(List<ReceivableBillMaster> receivableBillMasterList) {
        Map<String, List<ReceivableBillMaster>> map = new HashMap<>();
        for (ReceivableBillMaster receivableBillMaster : receivableBillMasterList) {
            String key = receivableBillMaster.getLegalPersonId()+"&"+receivableBillMaster.getCustomerId();//法人id + 客户id
            List<ReceivableBillMaster> tmpList = map.get(key);
            if (CollUtil.isEmpty(tmpList)) {
                tmpList = new ArrayList<>();
                tmpList.add(receivableBillMaster);
                map.put(key, tmpList);
            } else {
                tmpList.add(receivableBillMaster);
            }
        }
        return map;
    }


}
