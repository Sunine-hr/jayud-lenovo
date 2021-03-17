package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.AccountPayableMapper;
import com.jayud.mall.mapper.PayBillMasterMapper;
import com.jayud.mall.model.bo.QueryAccountPayableForm;
import com.jayud.mall.model.po.AccountPayable;
import com.jayud.mall.model.vo.AccountPayableVO;
import com.jayud.mall.model.vo.PayBillMasterVO;
import com.jayud.mall.service.IAccountPayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
