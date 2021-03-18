package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.AccountPayableMapper;
import com.jayud.mall.mapper.PayBillMasterMapper;
import com.jayud.mall.model.bo.MonthlyStatementForm;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.po.AccountPayable;
import com.jayud.mall.model.po.PayBillMaster;
import com.jayud.mall.model.vo.AccountPayableVO;
import com.jayud.mall.model.vo.PayBillMasterVO;
import com.jayud.mall.service.IAccountPayableService;
import com.jayud.mall.service.IPayBillMasterService;
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
    IPayBillMasterService payBillMasterService;

    @Override
    public IPage<AccountPayableVO> findAccountPayableByPage(QueryAccountPayableForm form) {
        //定义分页参数
        Page<AccountPayableVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<AccountPayableVO> pageInfo = accountPayableMapper.findAccountPayableByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult<AccountPayableVO> lookDetail(Long id) {
        AccountPayableVO accountPayableVO = accountPayableMapper.findAccountPayableById(id);
        if(ObjectUtil.isEmpty(accountPayableVO)){
            return CommonResult.error(-1, "对账单不存在");
        }
        List<PayBillMasterVO> payBillMasterVOS = payBillMasterMapper.findPayBillMasterByAccountPayableId(id);
        accountPayableVO.setPayBillMasterVOS(payBillMasterVOS);
        return CommonResult.success(accountPayableVO);
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
            accountPayable.setLegalPersonId(Long.valueOf(legalPersonId));//法人id(legal_person id)
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
