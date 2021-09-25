package com.jayud.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.AddLockOrderForm;
import com.jayud.finance.mapper.LockOrderMapper;
import com.jayud.finance.po.LockOrder;
import com.jayud.finance.service.ILockOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 锁单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2021-09-22
 */
@Service
public class LockOrderServiceImpl extends ServiceImpl<LockOrderMapper, LockOrder> implements ILockOrderService {

    @Override
    public void saveOrUpdate(AddLockOrderForm form) {
        LockOrder lockOrder = ConvertUtil.convert(form, LockOrder.class);
        lockOrder.setStartTime(form.getTimes().get(0));
        if (form.getTimes().size() > 1) {
            lockOrder.setEndTime(form.getTimes().get(1));
        }
        if (lockOrder.getId() == null) {
            lockOrder.setCreateTime(LocalDateTime.now()).setCreateUser(UserOperator.getToken());
        } else {
            lockOrder.setUpdateTime(LocalDateTime.now()).setUpdateUser(UserOperator.getToken());
        }
        this.saveOrUpdate(lockOrder);
    }

    @Override
    public List<LockOrder> getByCondition(LockOrder lockOrder) {
        return this.baseMapper.selectList(new QueryWrapper<>(lockOrder));
    }

    /**
     * 检查是否存在锁单区间
     *
     * @param type
     * @param accountTerm
     * @return
     */
    @Override
    public boolean checkLockingInterval(int type, String accountTerm) {
        QueryWrapper<LockOrder> condition = new QueryWrapper<>();
        condition.lambda().le(LockOrder::getStartTime, accountTerm)
                .ge(LockOrder::getEndTime, accountTerm).eq(LockOrder::getType, type)
                .eq(LockOrder::getStatus, StatusEnum.ENABLE.getCode());
        return this.count(condition) > 0;
    }
}
