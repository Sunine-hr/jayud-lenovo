package com.jayud.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jayud.finance.bo.AddLockOrderForm;
import com.jayud.finance.po.LockOrder;

import java.util.List;

/**
 * <p>
 * 锁单表 服务类
 * </p>
 *
 * @author chuanmei
 * @since 2021-09-22
 */
public interface ILockOrderService extends IService<LockOrder> {

    void saveOrUpdate(AddLockOrderForm form);

    List<LockOrder> getByCondition(LockOrder lockOrder);

    boolean checkLockingInterval(int type, String accountTerm, int model);
}
