package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.AccountBalanceMapper;
import com.jayud.mall.mapper.AccountReceivableMapper;
import com.jayud.mall.mapper.ReceivableBillDetailMapper;
import com.jayud.mall.mapper.ReceivableBillMasterMapper;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountBalance;
import com.jayud.mall.model.po.AccountReceivable;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.jayud.mall.model.po.TradingRecord;
import com.jayud.mall.model.vo.AccountBalanceVO;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.ReceivableBillDetailVO;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
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
        return pageInfo;
    }

    @Override
    public CommonResult<AccountReceivableVO> lookDetail(Long id) {
        AccountReceivableVO accountReceivableVO = accountReceivableMapper.findAccountReceivableById(id);
        if(accountReceivableVO == null){
            return CommonResult.error(-1, "对账单不存在");
        }
        Integer customerId = accountReceivableVO.getCustomerId();
        //客户的账户余额
        List<AccountBalanceVO> accountBalanceVOS = accountBalanceMapper.findAccountBalanceByCustomerId(customerId);
        List<String> accountBalanceList = new ArrayList<>();//客户账户余额
        accountBalanceVOS.forEach(accountBalanceVO -> {
            String amountFormat = accountBalanceVO.getAmountFormat();
            accountBalanceList.add(amountFormat);
        });
        accountReceivableVO.setAccountBalanceList(accountBalanceList);

        //账单明细
        List<ReceivableBillMasterVO>  receivableBillMasterVOS = receivableBillMasterMapper.findReceivableBillMasterByAccountReceivableId(id);
        if(CollUtil.isNotEmpty(receivableBillMasterVOS)){
            receivableBillMasterVOS.forEach(receivableBillMasterVO -> {
                Long billMasterId = receivableBillMasterVO.getId();
                List<ReceivableBillDetailVO> receivableBillDetailVOS = receivableBillDetailMapper.findReceivableBillDetailByBillMasterId(billMasterId);
                List<String> billAmountList = new ArrayList<>();
                List<String> balanceAmountList = new ArrayList<>();
                receivableBillDetailVOS.forEach(receivableBillDetailVO -> {
                    String billAmount = receivableBillDetailVO.getBillAmount();//账单金额
                    String balanceAmount = receivableBillDetailVO.getBalanceAmount();//结算金额
                    billAmountList.add(billAmount);
                    balanceAmountList.add(balanceAmount);
                });
                receivableBillMasterVO.setBillAmountList(billAmountList);
                receivableBillMasterVO.setBalanceAmountList(balanceAmountList);
            });
        }
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
