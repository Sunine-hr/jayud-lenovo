package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.AccountReceivableMapper;
import com.jayud.mall.mapper.ReceivableBillMasterMapper;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountReceivable;
import com.jayud.mall.model.po.ReceivableBillMaster;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.service.IAccountReceivableService;
import com.jayud.mall.service.IReceivableBillMasterService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    IReceivableBillMasterService receivableBillMasterService;

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
        List<ReceivableBillMasterVO>  receivableBillMasterVOS = receivableBillMasterMapper.findReceivableBillMasterByAccountReceivableId(id);
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
