package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.mall.mapper.AccountReceivableMapper;
import com.jayud.mall.mapper.ReceivableBillMasterMapper;
import com.jayud.mall.model.bo.QueryAccountReceivableForm;
import com.jayud.mall.model.po.AccountReceivable;
import com.jayud.mall.model.vo.AccountReceivableVO;
import com.jayud.mall.model.vo.ReceivableBillMasterVO;
import com.jayud.mall.service.IAccountReceivableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
